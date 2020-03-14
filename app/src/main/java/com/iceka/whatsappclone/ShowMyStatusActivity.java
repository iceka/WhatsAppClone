package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.StatusFlipperAdapter;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.Viewed;
import com.iceka.whatsappclone.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowMyStatusActivity extends AppCompatActivity {

    private AdapterViewFlipper mAdapterViewFlipper;
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
    private ObjectAnimator animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_status);

        mAdapterViewFlipper = findViewById(R.id.my_status_view_flipper);
        mAvatar = findViewById(R.id.my_status_avatar);
        mUsername = findViewById(R.id.my_status_username);
        mTime = findViewById(R.id.my_status_time);
        mToolbar = findViewById(R.id.my_status_toolbar);
//        mSeenCount = findViewById(R.id.tv_seen_count);

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
                    mAdapterViewFlipper.setAdapter(adapter);
                    mAdapterViewFlipper.setFlipInterval(2500);
                    flipperCount = mAdapterViewFlipper.getCount();
                    mAdapterViewFlipper.startFlipping();
                }
                mProgressBar = new ProgressBar[flipperCount];
                for (int i = 0; i < flipperCount; i++) {
                    mProgressBar[i] = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                    mProgressBar[i].setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    Utility.setMargins(mProgressBar[i]);
                    Utility.setProgressMax(mProgressBar[i]);
                    mProgressBar[i].setMax(100 * 100);
                    mProgressBar[i].getProgress();
                    ViewGroup mViewGroup = findViewById(R.id.my_status_parent_progressbar);
                    mViewGroup.addView(mProgressBar[i]);
                    mAdapterViewFlipper.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                            setProgressAnimate(mProgressBar[mAdapterViewFlipper.getDisplayedChild()]);
                            StatusItem statusItem = statusItemList.get(mAdapterViewFlipper.getDisplayedChild());
                            if (mAdapterViewFlipper.getDisplayedChild() == mAdapterViewFlipper.getCount() - 1) {
                                mAdapterViewFlipper.stopFlipping();
                                animation.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        finish();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                            }
                            animation.start();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProgressAnimate(ProgressBar pb) {
        animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), 100 * 100);
        animation.setDuration(2500);
        animation.setInterpolator(new LinearInterpolator());
    }

    private void setMargins(View view) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(0, 0, 8, 0);
            view.requestLayout();
        }
    }

}
