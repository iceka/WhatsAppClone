package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Chat;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends ArrayAdapter<Chat> {

    private TextView username;
    private TextView message;
    private CircleImageView avatar;

    public ChatAdapter(Context context, ArrayList<Chat> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_chats, parent, false);
        }

        Chat currentChat = getItem(position);

        username = (TextView) listItemView.findViewById(R.id.tv_username);
        message = (TextView) listItemView.findViewById(R.id.tv_message);
        avatar = (CircleImageView) listItemView.findViewById(R.id.avatar_user);

        username.setText(currentChat.getUsername());
        message.setText(currentChat.getMessage());
        avatar.setImageResource(currentChat.getImageResourceId());

        return listItemView;
    }
}
