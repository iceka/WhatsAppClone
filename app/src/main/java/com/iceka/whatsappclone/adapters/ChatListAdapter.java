package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private List<Chat> chatList;
    private List<Conversation> conversationList;
    private List<User> userList;

    private Context mContext;

    public ChatListAdapter(Context context, List<Chat> chats, List<User> users) {
        this.mContext = context;
        this.userList = users;
        this.chatList = chats;
//        this.conversationList = conversations;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats, parent, false);
        return new MyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Chat chat = chatList.get(position);
        User user = userList.get(position);


        holder.username.setText(user.getUsername());
        holder.message.setText(chat.getMessage());
        Glide.with(mContext)
                .load(user.getPhotoUrl())
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView message;
        private CircleImageView avatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tv_username);
            message = (TextView) itemView.findViewById(R.id.tv_message);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar_user);
        }
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView message;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_message);
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private CircleImageView avatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            avatar = itemView.findViewById(R.id.avatar_user);

        }
    }


}
