package com.iceka.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.adapters.ChatListAdapter;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mConversationReference;
    private DatabaseReference mChatReference;
    private DatabaseReference mUserReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private ChatListAdapter mAdapter;

    private String personId;
    private String key;

    private List<User> userList = new ArrayList<>();
    private List<Conversation> conversationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_tab, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyvlerview_chat_tab);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mAuth.getCurrentUser();

        mChatReference = mFirebaseDatabase.getReference().child("chats");
        mConversationReference = mFirebaseDatabase.getReference().child("conversation").child(mFirebaseUser.getUid());
        mUserReference = mFirebaseDatabase.getReference().child("users");

//        mUserReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//                    userList.add(user);
//                }
//                mAdapter = new ChatListAdapter(getActivity(), conversationList, userList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        mConversationReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
//                    conversationList.add(conversation);
////                }
//                mAdapter = new ChatListAdapter(getActivity(), conversationList, userList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        userList.clear();

        mConversationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Conversation conversation = snapshot.getValue(Conversation.class);
                    conversationList.add(conversation);
//                    conversationList.clear();

                    key = snapshot.getKey();
                    personId = snapshot.child("chatWithId").getValue(String.class);

                    Query query = mUserReference.orderByKey().equalTo(personId);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                userList.add(user);
                            }
                            mAdapter = new ChatListAdapter(getActivity(), conversationList, userList);
                            mRecyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
