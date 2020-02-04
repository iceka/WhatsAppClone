package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.ChatRoomActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private List<Conversation> conversationList;
    private List<User> userList;
    private DatabaseReference mUserReference;

    private Context mContext;

    public ChatListAdapter(Context context, List<Conversation> conversations, List<User> users) {
        this.mContext = context;
        this.userList = users;
        this.conversationList = conversations;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users");

        Conversation conversation = conversationList.get(position);

        String id = conversation.getChatWithId();
        Log.i("listadapter", "id: " + id);
        mUserReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                holder.username.setText(user.getUsername());
                Glide.with(mContext)
                        .load(user.getPhotoUrl())
                        .into(holder.avatar);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Conversation conversation = conversationList.get(position);
                        clearUnreadChat(conversation.getChatWithId());
                        Intent intent = new Intent(mContext, ChatRoomActivity.class);
                        intent.putExtra(ChatRoomActivity.EXTRAS_USER, user);
                        intent.putExtra("userUid", conversation.getChatWithId());
                        intent.putExtra("otherUid", user.getUid());
                        mContext.startActivity(intent);
                        if (user.isOnline()) {
                            Toast.makeText(mContext, "online BOSS : " + user.getLastSeen(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.message.setText(conversation.getLastMessage());
        if (conversation.getUnreadChatCount() == 0) {
            holder.unreadCount.setVisibility(View.GONE);
        } else {
            holder.unreadCount.setText(String.valueOf(conversation.getUnreadChatCount()));
        }

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(conversation.getTimestamp() * 1000);
        long tes = calendar.getTimeInMillis();
        DateFormat.format("M/dd/yyyy", calendar);
        CharSequence now = DateUtils.getRelativeTimeSpanString(tes, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        holder.chatTime.setText(now);

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView message;
        private CircleImageView avatar;
        private RelativeLayout layout;
        private TextView unreadCount;
        private TextView chatTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            message = itemView.findViewById(R.id.tv_message);
            avatar = itemView.findViewById(R.id.avatar_user);
            layout = itemView.findViewById(R.id.layout_user_chat);
            unreadCount = itemView.findViewById(R.id.tv_unread_count);
            chatTime = itemView.findViewById(R.id.tv_chat_time);
        }
    }

    private void clearUnreadChat(String chatWithId) {
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference conversationReference = FirebaseDatabase.getInstance().getReference().child("conversation").child(mFirebaseUser.getUid()).child(chatWithId).child("unreadChatCount");
        conversationReference.setValue(0);
    }

}
