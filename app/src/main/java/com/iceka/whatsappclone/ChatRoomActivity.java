package com.iceka.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String EXTRAS_USER = "user";
    public static String idFromContact = null;
    private User mUser;

    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        idFromContact = getIntent().getStringExtra("userUid");

        init();
    }

    private void init() {
        setArguments();
        initToolbar();
        showFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_chat_room);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView username = findViewById(R.id.username_chat_room);
        CircleImageView avatar = findViewById(R.id.avatar_chat_room);
        TextView status = findViewById(R.id.last_seen_and_online);

        username.setText(mUser.getUsername());
        Glide.with(getApplicationContext())
                .load(mUser.getPhotoUrl())
                .into(avatar);
//        status.setText(String.valueOf(mUser.getLastSeen()));
        Toast.makeText(this, "seen : " + mUser.getLastSeen(), Toast.LENGTH_SHORT).show();
        if (mUser.isOnline()) {
            status.setText("Online");
        } else {
            status.setText("Offline");
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
