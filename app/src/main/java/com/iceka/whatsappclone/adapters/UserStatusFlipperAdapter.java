package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Status;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserStatusFlipperAdapter extends BaseAdapter {

    private List<Status> statusList;
    private Context mContext;

    public UserStatusFlipperAdapter(Context context, List<Status> statuses) {
        this.mContext = context;
        this.statusList = statuses;
    }

    @Override
    public int getCount() {
        return statusList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_status, viewGroup, false);
        CircleImageView avatar = view.findViewById(R.id.avatar_user_status);
        TextView username = view.findViewById(R.id.username_status);

        Status status = statusList.get(i);

        username.setText(status.getUid());
        return view;
    }
}
