package com.iceka.whatsappclone.tes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Status;
import com.iceka.whatsappclone.models.StatusText;

import java.util.List;

public class TesAdapter extends RecyclerView.Adapter<TesAdapter.MyViewHolder> {

    private Context mContext;
    private List<StatusText> statusList;

    public TesAdapter(Context context, List<StatusText> statuses) {
        this.mContext = context;
        this.statusList = statuses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tes_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StatusText currentStatus = statusList.get(position);
        holder.textView.setText(currentStatus.getText());
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvTextStatus);
        }
    }
}
