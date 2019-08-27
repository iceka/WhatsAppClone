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
    private ChildEventListener mChildEventListener;

    private User mUser;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mUser = bundle.getParcelable(EXTRAS_USER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

//        adapters = new ChatRoomAdapter(getActivity(), R.layout.item_chat_outgoing, chats);
//        listView.setAdapter(adapters);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat(mMessageText.getText().toString(), mFirebaseUser.getUid(), id);
                mDatabaseReference.push().setValue(chat);
                mMessageText.setText("");
            }
        });
//        loadChats();

//        mChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Chat chat = dataSnapshot.getValue(Chat.class);
//                chatList.add(chat);
//
//                Toast.makeText(getContext(), "bisa : " + chatList, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        mDatabaseReference.addChildEventListener(mChildEventListener);


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


//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    chatList.add(chat);
//                }
//                adapters = new ChatRoomAdapter(getContext(), chatList);
//                mRecyclerView.setAdapter(adapters);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        return view;
    }

    private boolean messageFromCurrentUser(Chat chat) {
        String currentUid = mFirebaseUser.getUid();
        return currentUid.equalsIgnoreCase(chat.getSenderUid());
    }

    private void showSendButton() {
        mFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_send_black_24dp));
        mFab.setTag("send_image");
    }

    private void showVoiceButton() {
        mFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_voice_black_24dp));
        mFab.setTag("mic_image");
    }

//    private FirebaseRecyclerAdapter<Chat, RecyclerView.ViewHolder> mAdapter;
//
//    private void loadChats() {
//
//        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
//                .setQuery(mDatabaseReference, Chat.class)
//                .build();
//
//        mAdapter = new FirebaseRecyclerAdapter<Chat, RecyclerView.ViewHolder>(options) {
//
//            private final int TYPE_INCOMING = 1;
//            private final int TYPE_OUTGOING = 2;
//
//            @Override
//            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull Chat chat) {
//                if (messageFromCurrentUser(chat)) {
//                    populateOutgoingViewHolder((OutgoingViewHolder) viewHolder, chat);
//                } else {
//                    populateIncomingViewHolder((IncomingViewHolder) viewHolder, chat);
//                }
//            }
//
//            @NonNull
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view;
//                if (viewType == TYPE_INCOMING) {
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_incoming, parent, false);
//                    return new IncomingViewHolder(view);
//                } else {
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_outgoing, parent, false);
//                    return new OutgoingViewHolder(view);
//                }
//            }
//
//            private void populateIncomingViewHolder(IncomingViewHolder viewHolder, Chat chat) {
//                viewHolder.bindToMessage(chat);
//            }
//
//            private void populateOutgoingViewHolder(OutgoingViewHolder viewHolder, Chat chat) {
//                viewHolder.bindToMessage(chat);
//            }
//
//            class IncomingViewHolder extends RecyclerView.ViewHolder {
//                private TextView messages;
//
//                public IncomingViewHolder(@NonNull View itemView) {
//                    super(itemView);
//                    messages = itemView.findViewById(R.id.tv_chat_incoming);
//                }
//
//                public void bindToMessage(Chat chat) {
//                    messages.setText(chat.getMessage());
//                }
//            }
//
//            class OutgoingViewHolder extends RecyclerView.ViewHolder {
//                private TextView messages;
//
//                public OutgoingViewHolder(@NonNull View itemView) {
//                    super(itemView);
//                    messages = itemView.findViewById(R.id.tv_chat_outgoing);
//                }
//
//                public void bindToMessage(Chat chat) {
//                    messages.setText(chat.getMessage());
//                }
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                super.getItemViewType(position);
//                Chat chat = getItem(position);
//                if (messageFromCurrentUser(chat)) {
//                    return TYPE_OUTGOING;
//                }
//                return TYPE_INCOMING;
//            }
//        };
//
//        mRecyclerView.setAdapter(mAdapter);
//    }

}
