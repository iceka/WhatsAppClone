package com.iceka.whatsappclone.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.CameraActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.ShowMyStatusActivity;
import com.iceka.whatsappclone.adapters.StatusAdapter;
import com.iceka.whatsappclone.models.Status;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.Viewed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusTabFragment extends Fragment {

    private static final String TAG = "MYTAG";
    private BroadcastReceiver broadcastReceiver;
    private DatabaseReference mStatusReference;
    private FirebaseUser mFirebaseUser;
    private String myId;

    private CircleImageView mAvatar;
    private TextView mTimeStatus;
    private RelativeLayout layout;
    private CircularStatusView mCircularStatusCount;
    private RecyclerView mRecentStatusRv;
    private RecyclerView mViewedStatusRv;
    private LinearLayout mLayoutRecentStatus;
    private LinearLayout mLayoutViewedStatus;

    private List<Status> statusList = new ArrayList<>();
    private List<Status> statusListViewed = new ArrayList<>();
    private List<String> viewedList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_tab, container, false);
        setHasOptionsMenu(true);

        mAvatar = rootView.findViewById(R.id.avatar_user_status);
        mTimeStatus = rootView.findViewById(R.id.tv_time_status);
        layout = rootView.findViewById(R.id.layout_self_status);
        mCircularStatusCount = rootView.findViewById(R.id.circular_status_count);
        mRecentStatusRv = rootView.findViewById(R.id.rv_recent_updates_status);
        mViewedStatusRv = rootView.findViewById(R.id.rv_viewed_updates_status);
        mLayoutRecentStatus = rootView.findViewById(R.id.layout_recent_updates_status);
        mLayoutViewedStatus = rootView.findViewById(R.id.layout_viewed_updates_status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager viewedLayoutManager = new LinearLayoutManager(getContext());
        mRecentStatusRv.setLayoutManager(layoutManager);
        mViewedStatusRv.setLayoutManager(viewedLayoutManager);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mStatusReference = FirebaseDatabase.getInstance().getReference().child("status");
        myId = mFirebaseAuth.getCurrentUser().getUid();

        getMyStatus();
        getOtherStatus();
        getViewed();
        checkStatusViewed();

        if (statusList.size() == 0) {
            mLayoutRecentStatus.setVisibility(View.GONE);
        }


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    checkStatusExpire();
                }
            }
        };

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        return rootView;
    }

    private void getViewed() {
        mStatusReference.child(myId).child("statusItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference tesref = dataSnapshot.getRef().child("viewed");
                    tesref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Viewed viewed1 = snapshot1.getValue(Viewed.class);
                                viewedList.add(viewed1.getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMyStatus() {
        mStatusReference.child(myId).child("statusItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    mCircularStatusCount.setPortionsCount(count);
                    DatabaseReference cek = dataSnapshot.getRef();
                    Query query = cek.orderByKey().limitToLast(1);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (dataSnapshot.exists()) {
                                    StatusItem statusItem = snapshot.getValue(StatusItem.class);
                                    byte[] byteArray = Base64.decode(statusItem.getThumbnail(), Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    Glide.with(getContext())
                                            .load(bitmap)
                                            .into(mAvatar);
                                } else {
                                    Glide.with(getContext())
                                            .load(mFirebaseUser.getPhotoUrl())
                                            .into(mAvatar);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        StatusItem statusItem = snapshot.getValue(StatusItem.class);
                        mCircularStatusCount.setVisibility(View.VISIBLE);
                        assert statusItem != null;
                        long timeFromServer = statusItem.getTimestamp();
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(timeFromServer);
                        long co = calendar.getTimeInMillis();
                        DateFormat.format("M/dd/yyyy", calendar);
                        CharSequence now = DateUtils.getRelativeTimeSpanString(co, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
                        mTimeStatus.setText(now);
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getContext(), ShowMyStatusActivity.class));
                            }
                        });
                    }
                } else {
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getContext(), CameraActivity.class));
                        }
                    });
                    Glide.with(getContext())
                            .load(mFirebaseUser.getPhotoUrl())
                            .into(mAvatar);
                    mCircularStatusCount.setVisibility(View.GONE);
                    mTimeStatus.setText("Tap to add status");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOtherStatus() {
        final List<Integer> countList = new ArrayList<>();
        mStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusList.clear();
                countList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Status status = snapshot.getValue(Status.class);
                            if (!status.getUid().equals(myId) && status.getStatuscount() > 0) {
                                countList.add(status.getStatuscount());
                                statusList.add(status);

                            }
                            StatusAdapter adapter = new StatusAdapter(getActivity(), statusList);
                            mRecentStatusRv.setAdapter(adapter);

                        }
                    }

                    int sum = 0;
                    for (int num : countList) {
                        sum = sum + num;
                    }

                    if (sum <= 0) {
                        mLayoutRecentStatus.setVisibility(View.GONE);
                    } else {
                        mLayoutRecentStatus.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkStatusExpire() {
        mStatusReference.child(myId).child("statusItem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StatusItem statusItem = snapshot.getValue(StatusItem.class);
                    List<Long> timeList = new ArrayList<>();
                    timeList.add(statusItem.getTimestamp());
                    for (long tesa : timeList) {
                        tesa = tesa + TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS);
                        long timeNow = System.currentTimeMillis();
                        if (tesa <= timeNow) {
                            snapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = mStatusReference.child(myId).child("statusItem").orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StatusItem statusItem1 = snapshot.getValue(StatusItem.class);
                    long tes = statusItem1.getTimestamp() + TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS);
                    long now = System.currentTimeMillis();
                    if (tes <= now) {
                        mStatusReference.child(myId).child("allseen").removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkStatusViewed() {
        statusListViewed.clear();
        mStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusListViewed.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Status status = snapshot.getValue(Status.class);
                    mStatusReference.child(status.getUid()).child("allseen").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    Viewed viewed = snapshot1.getValue(Viewed.class);
                                    if (viewed.getUid().equals(myId)) {
                                        for (int i = 0; i < statusList.size(); i++) {
                                            if (statusList.get(i).getUid().equals(status.getUid())) {
                                                statusList.remove(i);
                                            }
                                        }
                                        if (statusList.size() == 0) {
                                            mLayoutRecentStatus.setVisibility(View.GONE);
                                        }
                                        statusListViewed.add(status);
                                        StatusAdapter adapter = new StatusAdapter(getActivity(), statusListViewed);
                                        mViewedStatusRv.setAdapter(adapter);
                                    }
                                }
                                mLayoutViewedStatus.setVisibility(View.VISIBLE);
                            } else {
                                mLayoutViewedStatus.setVisibility(View.GONE);
//                                statusListViewed.clear();
                            }
//                            else {
//                                for (int i = 0; i < statusListViewed.size(); i++) {
//                                    statusListViewed.remove(i);
//                                }
//                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_status, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

}
