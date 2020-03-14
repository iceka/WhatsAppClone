package com.iceka.whatsappclone;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.StatusFlipperAdapter;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.User;
import com.iceka.whatsappclone.models.Viewed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowOtherStatusActivity extends AppCompatActivity {

    private AdapterViewFlipper mAdapterViewFlipper;
    private TextView mSeenCount;
    private Toolbar mToolbar;
    private CircleImageView mAvatarUser;
    private TextView mUsername;
    private TextView mDate;
    private LinearLayout mViewedCount;
    private ProgressBar[] mProgressBar;

    private StatusFlipperAdapter mAdapter;

    private DatabaseReference mStatusReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mUserReference;

    private String myId;
    private int flipperCount;
    private ObjectAnimator animation;
    private String id;

    private List<StatusItem> statusItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_status);

        mAdapterViewFlipper = findViewById(R.id.status_view_flipper);
        mSeenCount = findViewById(R.id.tv_seen_count);
        mToolbar = findViewById(R.id.toolbar_status);
        mViewedCount = findViewById(R.id.layout_viewed_by_status);
        mAvatarUser = findViewById(R.id.avatar_status_user);
        mDate = findViewById(R.id.tv_status_date);
        mUsername = findViewById(R.id.tv_status_username);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        myId = mFirebaseUser.getUid();


        mStatusReference = firebaseDatabase.getReference().child("status");
        mUserReference = firebaseDatabase.getReference().child("users");

        id = getIntent().getStringExtra("uid");
        mToolbar.bringToFront();

        showStatus();
    }

    private void showStatus() {
        mUserReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUsername.setText(user.getUsername());
                Glide.with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .into(mAvatarUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStatusReference.child(id).child("statusItem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final StatusItem statusItem = snapshot.getValue(StatusItem.class);
                    statusItemList.add(statusItem);
                    mAdapter = new StatusFlipperAdapter(getApplicationContext(), statusItemList);
                    mAdapterViewFlipper.setAdapter(mAdapter);
                    mAdapterViewFlipper.setFlipInterval(2500);
                    flipperCount = mAdapterViewFlipper.getCount();
                    mAdapterViewFlipper.startFlipping();
                }
                mProgressBar = new ProgressBar[flipperCount];
                for (int i = 0; i < flipperCount; i++) {
                    mProgressBar[i] = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                    mProgressBar[i].setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    setMargins(mProgressBar[i]);
                    setProgressMax(mProgressBar[i]);
                    mProgressBar[i].getProgress();
                    ViewGroup mViewGroup = findViewById(R.id.parent_progress_bar_layout);
                    mViewGroup.addView(mProgressBar[i]);
                    mAdapterViewFlipper.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(final View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                            setProgressAnimate(mProgressBar[mAdapterViewFlipper.getDisplayedChild()]);
                            final StatusItem statusItem = statusItemList.get(mAdapterViewFlipper.getDisplayedChild());
                            long timeFromServer = statusItem.getTimestamp();
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeFromServer * 1000);
                            long co = calendar.getTimeInMillis();
                            DateFormat.format("M/dd/yyyy", calendar);
                            CharSequence now = DateUtils.getRelativeTimeSpanString(co, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
                            mDate.setText(now);
                            long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                            final Viewed viewed = new Viewed(myId, timestamp);
                            mStatusReference.child(id).child("statusItem").child(statusItem.getId()).child("viewed").orderByKey().startAt(myId).endAt(myId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        mStatusReference.child(id).child("statusItem").child(statusItem.getId()).child("viewed").child(myId).setValue(viewed);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            if (mAdapterViewFlipper.getDisplayedChild() == mAdapterViewFlipper.getCount() - 1) {
                                mAdapterViewFlipper.stopFlipping();
                                animation.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {
                                        long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                                        final Viewed viewed = new Viewed(myId, timestamp);
                                        if (!id.equals(myId)) {
                                            mStatusReference.child(id).child("allseen").orderByKey().startAt(myId).endAt(myId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) {
                                                        mStatusReference.child(id).child("allseen").child(myId).setValue(viewed);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
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

    public void setProgressMax(ProgressBar pb) {
        pb.setMax(100 * 100);
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
