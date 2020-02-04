package com.iceka.whatsappclone.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.iceka.whatsappclone.models.StatusText;
import com.iceka.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatTabFragment extends Fragment {

    private static final String TAG = "LOG Example";

    private RecyclerView mRecyclerView;
    private LinearLayout mStartChatLayout;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mConversationReference;
    private DatabaseReference mStatusReference;
    private DatabaseReference mUserReference;
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
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mConversationReference = mFirebaseDatabase.getReference().child("conversation").child(mFirebaseUser.getUid());
        mUserReference = mFirebaseDatabase.getReference().child("users");
        mStatusReference = mFirebaseDatabase.getReference().child("status");

        Query myQuery = mConversationReference.orderByChild("timestamp");
//        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                conversationList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Conversation conversation = snapshot.getValue(Conversation.class);
//                    conversationList.add(conversation);
//                    mAdapter = new ChatListAdapter(getActivity(), conversationList, userList);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversationList.clear();
                if (dataSnapshot.exists()) {
                    mStartChatLayout.setVisibility(View.GONE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Conversation conversation = snapshot.getValue(Conversation.class);
                        conversationList.add(conversation);

//                    userList.clear();

//                    key = snapshot.getKey();
//                    personId = snapshot.child("chatWithId").getValue(String.class);
//
//                    Query query = mUserReference.orderByKey().equalTo(personId);
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                User user = snapshot.getValue(User.class);
//                                userList.add(user);
//                            }
                        mAdapter = new ChatListAdapter(getActivity(), conversationList, userList);
                        mRecyclerView.setAdapter(mAdapter);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE, 2);
//        long cutOff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
//        Query oldStatus = mStatusReference.child(mFirebaseUser.getUid()).child("typeStatus").orderByChild("timestamp").startAt(calendar.getTimeInMillis());
//        oldStatus.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    snapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        /*try {
            String data = "2019-09-04T05:03:27.322Z";
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            input.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat output = new SimpleDateFormat("EEE, dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            output.setTimeZone(TimeZone.getDefault());
            Date date = input.parse(data);
            Toast.makeText(getContext(), "date : " + output.format(date), Toast.LENGTH_SHORT).show();output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        return rootView;
    }

}
