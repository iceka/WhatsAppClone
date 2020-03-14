package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.devlomi.circularstatusview.CircularStatusView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.ShowOtherStatusActivity;
import com.iceka.whatsappclone.models.Status;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {

    private List<Status> statusList;
    private Context mContext;

    public StatusAdapter(Context context, List<Status> statuses) {
        this.mContext = context;
        this.statusList = statuses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference().child("users");
        Status currentStatus = statusList.get(position);
        final String userUid = currentStatus.getUid();

        mUserReference.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                User user = dataSnapshot.getValue(User.class);
                holder.mUsername.setText(user.getUsername());
                Glide.with(holder.mThumbnail.getContext())
                        .load(user.getPhotoUrl())
                        .into(holder.mThumbnail);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShowOtherStatusActivity.class);
                intent.putExtra("uid", userUid);
                mContext.startActivity(intent);
            }
        });

        holder.mStatusCount.setPortionsCount(currentStatus.getStatuscount());
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mThumbnail;
        private CircularStatusView mStatusCount;
        private TextView mUsername;
        private TextView mDate;
        private RelativeLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.thumbnail_status);
            mStatusCount = itemView.findViewById(R.id.circular_status_counts);
            mUsername = itemView.findViewById(R.id.username_status);
            mDate = itemView.findViewById(R.id.date_status);
            mLayout = itemView.findViewById(R.id.layout_other_status);
        }
    }
}
