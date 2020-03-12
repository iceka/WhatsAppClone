package com.iceka.whatsappclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.iceka.whatsappclone.fragments.StatusTabFragment;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.Viewed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StatusTextActivity extends AppCompatActivity {

    private EditText mText;
    private FloatingActionButton mFabPost;
    private RelativeLayout mLayout;
    private ImageView mColorChange;

    private DatabaseReference mStatusReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String myId;
    private int i = 0;
    private int p = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_text);

        mText = findViewById(R.id.et_text_status);
        mFabPost = findViewById(R.id.fab_status_text);
        mLayout = findViewById(R.id.layout_text_status);
        mColorChange = findViewById(R.id.icon_color_status);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mStatusReference = mFirebaseDatabase.getReference().child("status");

        myId = mFirebaseUser.getUid();

        mText.requestFocus();

        final int[] colorBackgrounds = StatusTextActivity.this.getResources().getIntArray(R.array.statusBackground);
        int randomColor = colorBackgrounds[new Random().nextInt(colorBackgrounds.length)];
        mLayout.setBackgroundColor(randomColor);
        mColorChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p++;
                if (p < colorBackgrounds.length) {
                    mLayout.setBackgroundColor(colorBackgrounds[p]);
                } else {
                    p = 0;
                    mLayout.setBackgroundColor(colorBackgrounds[p]);
                }

            }
        });

        mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mFabPost.show();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mFabPost.hide();
                }
            }
        });

        mFabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
                finish();
            }
        });

    }

    private void postData() {
        int backgroundColor = ((ColorDrawable) mLayout.getBackground()).getColor();
        long timestamp = System.currentTimeMillis();
        long expireTime = timestamp + TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        List<Viewed> views = new ArrayList<>();
        Viewed viewed = new Viewed();
        DatabaseReference newRef = mStatusReference.child(mFirebaseUser.getUid()).child("statusItem").push();

        mLayout.setDrawingCacheEnabled(true);
        mLayout.buildDrawingCache();
        Bitmap bitmap = mLayout.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

//        StatusText statusText = new StatusText(mText.getText().toString(), timestamp, expireTime,  backgroundColor, null, newRef.getKey());
        StatusItem statusItem = new StatusItem(newRef.getKey(), "text", mText.getText().toString(), timestamp, timestamp, backgroundColor, encoded, null);
        mStatusReference.child(mFirebaseUser.getUid()).child("uid").setValue(mFirebaseUser.getUid());
        mStatusReference.child(mFirebaseUser.getUid()).child("allseen").removeValue();
//        mStatusReference.child(mFirebaseUser.getUid()).child("typeStatus").push().setValue(statusText);
        newRef.setValue(statusItem);
    }

}
