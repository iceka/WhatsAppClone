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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iceka.whatsappclone.ChatRoomActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<User> contactList;
    private Context mContext;

    public ContactAdapter(Context context, List<User> contacts) {
        this.contactList = contacts;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        User currentUser = contactList.get(position);

        holder.mTvUsername.setText(currentUser.getUsername());
        holder.mTvAbout.setText(currentUser.getAbout());
        holder.mAvatar.setVisibility(View.VISIBLE);
        Glide.with(holder.mAvatar.getContext())
                .load(currentUser.getPhotoUrl())
                .into(holder.mAvatar);
        holder.mContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = contactList.get(position);
                Intent intent = new Intent(mContext, ChatRoomActivity.class);
                intent.putExtra("userUid", user.getUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvUsername;
        private TextView mTvAbout;
        private CircleImageView mAvatar;
        private RelativeLayout mContactItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvUsername = itemView.findViewById(R.id.tv_username_contact);
            mTvAbout = itemView.findViewById(R.id.tv_about_contact);
            mAvatar = itemView.findViewById(R.id.avatar_user_contact);
            mContactItem = itemView.findViewById(R.id.item_contact_layout);
        }

    }
}
