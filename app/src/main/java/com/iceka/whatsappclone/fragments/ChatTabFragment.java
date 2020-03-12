package com.iceka.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.adapters.ChatListAdapter;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayout mStartChatLayout;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mConversationReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private ChatListAdapter mAdapter;

    private List<User> userList = new ArrayList<>();
    private List<Conversation> conversationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_tab, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyvlerview_chat_tab);
        mStartChatLayout = rootView.findViewById(R.id.layout_start_chat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mConversationReference = mFirebaseDatabase.getReference().child("conversation").child(mFirebaseUser.getUid());

        getData();

        return rootView;
    }

    private void getData() {
        Query myQuery = mConversationReference.orderByChild("timestamp");
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversationList.clear();
                if (dataSnapshot.exists()) {
                    mStartChatLayout.setVisibility(View.GONE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Conversation conversation = snapshot.getValue(Conversation.class);
                        conversationList.add(conversation);
                        mAdapter = new ChatListAdapter(getActivity(), conversationList, userList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
