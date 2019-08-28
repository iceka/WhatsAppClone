package com.iceka.whatsappclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.ChatRoomAdapter;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.iceka.whatsappclone.ChatRoomActivity.EXTRAS_USER;
import static com.iceka.whatsappclone.ChatRoomActivity.idFromContact;

public class ChatFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mConversationReference;

    private String id;
    private String userUid;
    private String chatId;

    private EditText mMessageText;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;

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
        ListView listView = view.findViewById(R.id.listview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
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

        mDatabaseReference = mFirebaseDatabase.getReference().child("chats").child(chatId);
        mConversationReference = mFirebaseDatabase.getReference().child("conversation").child(mFirebaseUser.getUid());

//        final DatabaseReference addConversation = mFirebaseDatabase.getReference().child("users").child(mFirebaseUser.getUid()).child("conversation");

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat(mMessageText.getText().toString(), mFirebaseUser.getUid(), id);
                mDatabaseReference.push().setValue(chat);
                mMessageText.setText("");

//                mConversationReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (mConversationReference.child(chatId).toString() == chatId){
//                                Conversation conversation = new Conversation(mFirebaseUser.getUid(), id);
//                                mConversationReference.child(chatId).setValue(conversation);
//                                Toast.makeText(getContext(), "Aneh", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getContext(), "Teuing", Toast.LENGTH_SHORT).show();
//                            }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                Conversation conversation = new Conversation(mFirebaseUser.getUid(), id);
                mConversationReference.child(chatId).setValue(conversation);

//                addConversation.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        addConversation.removeEventListener(this);
//                        List<String> list = new ArrayList<>();
//                        if (dataSnapshot.getValue() == null) {
//                            list.add(chatId);
//                        } else {
//                            if (dataSnapshot.getValue() instanceof List && ((List) dataSnapshot.getValue()).size() > 0) {
//                                list = (List<String>) dataSnapshot.getValue();
//                                list.add(chatId);
//                            }
//                        }
//
//                        addConversation.setValue(list);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

            }
        });

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                chatList.add(chat);
                adapters = new ChatRoomAdapter(getActivity(), chatList);
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
