package com.iceka.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.EditText;

import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.User;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String EXTRAS_USER = "user";
    public static String idFromContact = null;
    private User mUser;
    private Chat mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        idFromContact = getIntent().getStringExtra("userUid");

        init();
    }

    private void init() {
        setArguments();
        showFragment();
    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_chat, ChatFragment.newInstance(getIntent().getExtras()));
        fragmentTransaction.commit();
    }

    private void setArguments() {
        Bundle bundle = getIntent().getExtras();
        mUser = bundle.getParcelable(EXTRAS_USER);
        if (mUser != null) {
            setTitle(mUser.getUsername());
        }
    }

}
