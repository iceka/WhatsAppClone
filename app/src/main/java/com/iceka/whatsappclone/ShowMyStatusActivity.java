package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.StatusFlipperAdapter;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowMyStatusActivity extends AppCompatActivity {

    private AdapterViewFlipper mViewFlipper;
    private Toolbar mToolbar;
    private CircleImageView mAvatar;
    private TextView mUsername;
    private TextView mTime;
    private ProgressBar[] mProgressBar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mStatusReference;

    private List<StatusItem> statusItemList = new ArrayList<>();

    private int flipperCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_status);

        mViewFlipper = findViewById(R.id.my_status_view_flipper);
        mAvatar = findViewById(R.id.my_status_avatar);
        mUsername = findViewById(R.id.my_status_username);
        mTime = findViewById(R.id.my_status_time);
        mToolbar = findViewById(R.id.my_status_toolbar);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mStatusReference = mFirebaseDatabase.getReference().child("status");

        mToolbar.bringToFront();

        getMyStatus();

    }

    private void getMyStatus() {
        mStatusReference.child(mFirebaseUser.getUid()).child("statusItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StatusItem statusItem = snapshot.getValue(StatusItem.class);
                    statusItemList.add(statusItem);
                    StatusFlipperAdapter adapter = new StatusFlipperAdapter(getApplicationContext(), statusItemList);
                    mViewFlipper.setAdapter(adapter);
                    mViewFlipper.setFlipInterval(2500);
                    mViewFlipper.startFlipping();
                    flipperCount = mViewFlipper.getCount();
                    Toast.makeText(ShowMyStatusActivity.this, "count : " + flipperCount, Toast.LENGTH_SHORT).show();
                }
                mProgressBar = new ProgressBar[flipperCount];
                for (int i = 0; i < flipperCount; i++) {
                    mProgressBar[i] = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                    mProgressBar[i].setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    Utility.setMargins(mProgressBar[i]);
                    Utility.setProgressMax(mProgressBar[i]);
                    mProgressBar[i].getProgress();
                    ViewGroup mViewGroup = findViewById(R.id.my_status_parent_progressbar);
                    mViewGroup.addView(mProgressBar[i]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
