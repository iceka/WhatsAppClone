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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final User currentUser = contactList.get(position);

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users");
        userReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                holder.mContactItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ChatRoomActivity.class);
                        intent.putExtra(ChatRoomActivity.EXTRAS_USER, user);
                        intent.putExtra("userUid", currentUser.getUid());
                        mContext.startActivity(intent);
//                        Toast.makeText(mContext, "last : " + user.getLastSeen(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                final User user = dataSnapshot.getValue(User.class);
//                holder.mTvUsername.setText(user.getUsername());
//                holder.mContactItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, ChatRoomActivity.class);
//                        intent.putExtra(ChatRoomActivity.EXTRAS_USER, user);
//                        intent.putExtra("userUid", currentUser.getUid());
////                        mContext.startActivity(intent);
////                        Toast.makeText(mContext, "username : " + user.getUsername(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Toast.makeText(mContext, "tes : " + user.getUsername(), Toast.LENGTH_SHORT).show();



        holder.mTvUsername.setText(currentUser.getUsername());
        holder.mTvAbout.setText(currentUser.getAbout());
        holder.mAvatar.setVisibility(View.VISIBLE);
        Glide.with(holder.mAvatar.getContext())
                .load(currentUser.getPhotoUrl())
                .into(holder.mAvatar);
//        holder.mContactItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                User user = contactList.get(position);
//                Intent intent = new Intent(mContext, ChatRoomActivity.class);
//                intent.putExtra("userUid", user.getUid());
//                mContext.startActivity(intent);
//            }
//        });
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
