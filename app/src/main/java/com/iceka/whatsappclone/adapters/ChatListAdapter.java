package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.ChatRoomActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private List<Conversation> conversationList;
    private List<User> userList;

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


        User user = userList.get(position);

        holder.username.setText(user.getUsername());
        Glide.with(mContext)
                .load(user.getPhotoUrl())
                .into(holder.avatar);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user1 = userList.get(position);
                Intent intent = new Intent(mContext, ChatRoomActivity.class);
                intent.putExtra("userUid", user1.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        });
        Conversation conversation = conversationList.get(position);
        holder.message.setText(conversation.getLastMessage());
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            message = itemView.findViewById(R.id.tv_message);
            avatar = itemView.findViewById(R.id.avatar_user);
            layout = itemView.findViewById(R.id.layout_user_chat);
        }
    }

}
