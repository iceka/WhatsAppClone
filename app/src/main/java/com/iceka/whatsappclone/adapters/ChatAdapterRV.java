package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Chat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapterRV extends RecyclerView.Adapter<ChatAdapterRV.MyViewHolder> {

    private List<Chat> chatList;
    private Context mContext;

    public ChatAdapterRV(Context context, List<Chat> chats) {
        this.mContext = context;
        this.chatList = chats;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.username.setText(chat.getUsername());
        holder.message.setText(chat.getMessage());
        Glide.with(mContext)
                .load(chat.getImageResourceId())
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


}
