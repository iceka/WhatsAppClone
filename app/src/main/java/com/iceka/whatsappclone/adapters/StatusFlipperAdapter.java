package com.iceka.whatsappclone.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.StatusText;

import java.util.List;

public class StatusFlipperAdapter extends BaseAdapter {

    private List<StatusItem> statusItemList;
    private Context mContext;
    private ProgressBar[] mProgressBar;

    public StatusFlipperAdapter(Context context, List<StatusItem> statusItems) {
        this.mContext = context;
        this.statusItemList = statusItems;
    }

    @Override
    public int getCount() {
        return statusItemList.size();
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
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status_text, viewGroup, false);

        ImageView image = view.findViewById(R.id.img_status_picture);
        RelativeLayout layout = view.findViewById(R.id.layout_item_status_text);
        TextView text = view.findViewById(R.id.tv_text_item_status);

        StatusItem statusItem = statusItemList.get(i);

        Log.i("MYTAG", "TYPE : " + statusItem.getType());
        if (statusItem.getType().equals("image")) {
            image.setVisibility(View.VISIBLE);
            Glide.with(mContext.getApplicationContext())
                    .load(statusItem.getUrl())
                    .into(image);
        } else if (statusItem.getType().equals("text")) {
            image.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(statusItem.getBackgroundColor());
            text.setText(statusItem.getText());
            Toast.makeText(mContext, "text: " + statusItem.getText(), Toast.LENGTH_SHORT).show();
        }
//        StatusText statusText = statusTextList.get(i);
//        Toast.makeText(mContext, "posisi : " + statusText.getText(), Toast.LENGTH_SHORT).show();
//        Log.i("MyTag","text : " + statusText.getText());
//        RelativeLayout layout = view.findViewById(R.id.layout_item_status_text);
//        TextView text = view.findViewById(R.id.tv_text_item_status);
//        layout.setBackgroundColor(statusText.getBackgroundColor());
//        text.setText(statusText.getText());
//        mProgressBar = new ProgressBar[statusTextList.size()];
//        for (int e = 0; i < statusTextList.size(); e++) {
//            mProgressBar[e] = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
//            mProgressBar[e].setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//
//            mProgressBar[e].getProgress();
//            ViewGroup mViewGroup = view.findViewById(R.id.parent_progress_bar_layout);
//            mViewGroup.addView(mProgressBar[e]);
//        }

        return view;
    }
}
