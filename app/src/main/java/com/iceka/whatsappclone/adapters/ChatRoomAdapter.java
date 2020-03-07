package com.iceka.whatsappclone.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Chat;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_INCOMING = 1;
    private final int TYPE_OUTGOING = 2;
    List<Chat> chatList;
    private Context mContext;

    public ChatRoomAdapter(Context context, List<Chat> chats) {
        this.mContext = context;
        this.chatList = chats;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chatList.get(position);
        if (tes(chat)) {
            return TYPE_OUTGOING;
        }
        return TYPE_INCOMING;
//       return super.getItemViewType(position);

    }

    private boolean tes(Chat chat) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser().getUid().equalsIgnoreCase(chat.getSenderUid())) {
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_INCOMING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_incoming, parent, false);
            return new IncomingViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_outgoing, parent, false);
            return new OutgoingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_INCOMING) {
            IncomingViewHolder holder1 = (IncomingViewHolder) holder;
            configureViewHolderIncoming(holder1, position);
        } else {
            OutgoingViewHolder holder2 = (OutgoingViewHolder) holder;
            configureViewholderOutgoing(holder2, position);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    private void configureViewHolderIncoming(IncomingViewHolder holder, int position) {
        Chat chat = (Chat) chatList.get(position);
        if (chat != null) {
            holder.message.setText(chat.getMessage());
        }
    }

    private void configureViewholderOutgoing(final OutgoingViewHolder holder, int position) {
        Chat chat = (Chat) chatList.get(position);
        if (chat != null) {
            holder.message.setText(chat.getMessage());
            holder.message.post(new Runnable() {
                @Override
                public void run() {
                    int linecount = holder.message.getLineCount();
                    Log.i("MYTAG", "Lines : " + linecount);
                }
            });
        }
    }

    public class IncomingViewHolder extends RecyclerView.ViewHolder {
        private TextView message;

        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_chat_incoming);
        }

    }

    public class OutgoingViewHolder extends RecyclerView.ViewHolder {
        private TextView message;

        public OutgoingViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_chat_outgoing);
        }


    }

}
