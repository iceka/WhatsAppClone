package com.iceka.whatsappclone;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iceka.whatsappclone.adapters.ChatRoomAdapter;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.Conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.iceka.whatsappclone.ChatRoomActivity.idFromContact;

public class ChatFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mChatReference;
    private DatabaseReference mConversationReference;

    private String id;
    private String userUid;
    private String chatId;
    private int unreadCount = 0;

    private EditText mMessageText;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private ImageView mAttachPict;

    private ChatRoomAdapter adapters;

    private List<Chat> chatList = new ArrayList<>();


    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mMessageText = view.findViewById(R.id.et_message_chat);
        mFab = view.findViewById(R.id.fab_chat);
        mRecyclerView = view.findViewById(R.id.rv_chat);
        mAttachPict = view.findViewById(R.id.img_attach_picture);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        id = idFromContact;
        userUid = mFirebaseUser.getUid();

        if (userUid.compareTo(id) < id.compareTo(userUid)) {
            chatId = userUid + id;
        } else {
            chatId = id + userUid;
        }

        mChatReference = mFirebaseDatabase.getReference().child("chats").child(chatId);
        mConversationReference = mFirebaseDatabase.getReference().child("conversation");

        mMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    showSendButton();
                    mAttachPict.setVisibility(View.GONE);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String contoh = mMessageText.getText().toString();
                            long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                            Chat chat = new Chat(contoh, mFirebaseUser.getUid(), id, timestamp);
                            mChatReference.push().setValue(chat);
                            mMessageText.setText("");

                            unreadCount = unreadCount + 1;
                            Conversation conversationSender = new Conversation(mFirebaseUser.getUid(), id, contoh, timestamp);
                            Conversation conversationReceiver = new Conversation(id, mFirebaseUser.getUid(), contoh, timestamp, unreadCount);

                            DatabaseReference senderReference = mConversationReference.child(mFirebaseUser.getUid()).child(id);
                            senderReference.setValue(conversationSender);
                            DatabaseReference receiverReference = mConversationReference.child(id).child(mFirebaseUser.getUid());
                            receiverReference.setValue(conversationReceiver);

                        }
                    });

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    showVoiceButton();
                    mAttachPict.setVisibility(View.VISIBLE);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }
        });


        mChatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                chatList.add(chat);
                adapters = new ChatRoomAdapter(getActivity(), chatList);
                mRecyclerView.smoothScrollToPosition(chatList.size() - 1);
                mRecyclerView.setAdapter(adapters);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    private void showSendButton() {
        mFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_send_black_24dp));
        mFab.setTag("send_image");
    }

    private void showVoiceButton() {
        mFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_voice_black_24dp));
        mFab.setTag("mic_image");
    }

}
