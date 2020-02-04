package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.ChatRoomAdapter;
import com.iceka.whatsappclone.models.Chat;
import com.iceka.whatsappclone.models.Conversation;
import com.iceka.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView backButton;
    private TextView username;
    private CircleImageView avatar;
    private TextView status;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private EditText mMessageText;
    private ImageView mAttachPict;

    public static final String EXTRAS_USER = "user";
    public static String idFromContact = null;
    private User mUser;
    private String userUid;
    private String chatId;
    private int unreadCount = 0;
    private String otherUid;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mChatReference;
    private DatabaseReference mConversationReference;
    private DatabaseReference mUserReference;

    private ChatRoomAdapter adapters;

    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        toolbar = findViewById(R.id.toolbar_chat_room);
        setSupportActionBar(toolbar);
        backButton = findViewById(R.id.toolbar_back_button);
        username = findViewById(R.id.username_chat_room);
        avatar = findViewById(R.id.avatar_chat_room);
        status = findViewById(R.id.last_seen_and_online);
        mRecyclerView = findViewById(R.id.rv_chat);
        mFab = findViewById(R.id.fab_chat);
        mMessageText = findViewById(R.id.et_message_chat);
        mAttachPict = findViewById(R.id.img_attach_picture);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        idFromContact = getIntent().getStringExtra("userUid");
        userUid = mFirebaseUser.getUid();

        if (userUid.compareTo(idFromContact) < idFromContact.compareTo(userUid)) {
            chatId = userUid + idFromContact;
        } else {
            chatId = idFromContact + userUid;
        }

        mChatReference = mFirebaseDatabase.getReference().child("chats").child(chatId);
        mConversationReference = mFirebaseDatabase.getReference().child("conversation");
        mUserReference = mFirebaseDatabase.getReference().child("users");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUserDetails();
        sendChatData();
        getChatData();
    }

    private void sendChatData() {
        mMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    showSendButton();
                    mAttachPict.setVisibility(View.GONE);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String contoh = mMessageText.getText().toString();
                            long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                            Chat chat = new Chat(contoh, mFirebaseUser.getUid(), idFromContact, timestamp);
                            mChatReference.push().setValue(chat);
                            mMessageText.setText("");

                            unreadCount = unreadCount + 1;
                            Conversation conversationSender = new Conversation(mFirebaseUser.getUid(), idFromContact, contoh, timestamp);
                            Conversation conversationReceiver = new Conversation(idFromContact, mFirebaseUser.getUid(), contoh, timestamp, unreadCount);

                            DatabaseReference senderReference = mConversationReference.child(mFirebaseUser.getUid()).child(idFromContact);
                            senderReference.setValue(conversationSender);
                            DatabaseReference receiverReference = mConversationReference.child(idFromContact).child(mFirebaseUser.getUid());
                            receiverReference.setValue(conversationReceiver);

                        }
                    });

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    showVoiceButton();
                    mAttachPict.setVisibility(View.VISIBLE);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }
        });
    }

    private void getChatData() {
        mChatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                chatList.add(chat);
                adapters = new ChatRoomAdapter(ChatRoomActivity.this, chatList);
                mRecyclerView.smoothScrollToPosition(chatList.size() - 1);
                mRecyclerView.setAdapter(adapters);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserDetails() {
        mUserReference.child(idFromContact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                Glide.with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .into(avatar);
                long timeFromServer = user.getLastSeen();
                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.setTimeInMillis(timeFromServer * 1000);
                long tes = calendar.getTimeInMillis();
                DateFormat.format("M/dd/yyyy", calendar);
                CharSequence now = DateUtils.getRelativeTimeSpanString(tes, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
//                Toast.makeText(ChatRoomActivity.this, "online gk : " + user.isOnline(), Toast.LENGTH_SHORT).show();
                if (user.isOnline()) {
                    status.setText("Online");
                } else {
                    status.setText("Last seen " + now);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSendButton() {
        mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_send_black_24dp));
        mFab.setTag("send_image");
    }

    private void showVoiceButton() {
        mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_keyboard_voice_black_24dp));
        mFab.setTag("mic_image");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
