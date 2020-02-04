package com.iceka.whatsappclone.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.ShowMyStatusActivity;
import com.iceka.whatsappclone.StatusTextActivity;
import com.iceka.whatsappclone.adapters.StatusAdapter;
import com.iceka.whatsappclone.models.Status;
import com.iceka.whatsappclone.models.StatusItem;
import com.iceka.whatsappclone.models.StatusText;
import com.iceka.whatsappclone.models.Viewed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusTabFragment extends Fragment {

    private DatabaseReference mStatusReference;

    private String myId;
    BroadcastReceiver broadcastReceiver;

    private TextView mTimeStatus;
    private RelativeLayout layout;
    private CircularStatusView mCircularStatusCount;
    private RecyclerView mRecentStatusRv;
    private RecyclerView mViewedStatusRv;


    private static final String TAG = "MYTAG";

    private List<StatusItem> statusItemList = new ArrayList<>();
    private List<StatusItem> statusItemListViewed = new ArrayList<>();
    private List<Status> statusList = new ArrayList<>();
    private List<Status> statusListViewed = new ArrayList<>();
    private List<String> viewedList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_tab, container, false);
        setHasOptionsMenu(true);

        CircleImageView avatar = rootView.findViewById(R.id.avatar_user_status);
        mTimeStatus = rootView.findViewById(R.id.tv_time_status);
        layout = rootView.findViewById(R.id.layout_self_status);
        mCircularStatusCount = rootView.findViewById(R.id.circular_status_count);
        mRecentStatusRv = rootView.findViewById(R.id.rv_recent_updates_status);
        mViewedStatusRv = rootView.findViewById(R.id.rv_viewed_updates_status);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager viewedLayoutManager = new LinearLayoutManager(getContext());
        mRecentStatusRv.setLayoutManager(layoutManager);
        mViewedStatusRv.setLayoutManager(viewedLayoutManager);
//        mRecentStatusRv.setItemAnimator(new DefaultItemAnimator());

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mStatusReference = FirebaseDatabase.getInstance().getReference().child("status");
        myId = mFirebaseAuth.getCurrentUser().getUid();

        getMyStatus();
        getOtherStatus();
        getViewed();
        checkStatusViewed();

        Glide.with(getActivity())
                .load(mFirebaseUser.getPhotoUrl())
                .into(avatar);

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
                                Log.i(TAG, "viewed to : " + viewedList);
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
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        StatusText statusText = snapshot.getValue(StatusText.class);
                        mCircularStatusCount.setVisibility(View.VISIBLE);
                        assert statusText != null;
                        long timeFromServer = statusText.getTimestamp();
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(timeFromServer * 1000);
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
                            startActivity(new Intent(getContext(), StatusTextActivity.class));
                        }
                    });
                    mCircularStatusCount.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "tidak bisa cuk ", Toast.LENGTH_SHORT).show();
                    }
                    mTimeStatus.setText("Tap to add status");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOtherStatus() {
        mStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusItemList.clear();
                statusList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()) {
                        Status status = snapshot.getValue(Status.class);
                        if (!status.getUid().equals(myId)) {
                            statusList.add(status);
                        }
                        StatusAdapter adapter = new StatusAdapter(getActivity(), statusList);
                        mRecentStatusRv.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkStatusExpire() {
        mStatusReference.child(myId).child("typeStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StatusText statusText = snapshot.getValue(StatusText.class);
                    List<Long> timeList = new ArrayList<>();
                    timeList.add(statusText.getTimestamp());
                    for (long tesa : timeList) {
                        tesa = tesa * 1000 + TimeUnit.MILLISECONDS.convert(10, TimeUnit.HOURS);
                        long timeNow = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000;
                        if (tesa <= timeNow) {
                            snapshot.getRef().removeValue();
                            Log.i(TAG, "remove ");
                        }
                        Log.i(TAG, "Timestampnya : " + tesa);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkStatusViewed() {
        mStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Status status = snapshot.getValue(Status.class);
//                   Log.i("MYTAG", "hasil sblm : " + status.getUid());
                    mStatusReference.child(status.getUid()).child("allseen").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Viewed viewed = snapshot1.getValue(Viewed.class);
                                if (viewed.getUid().equals(myId)) {
                                    String cek = viewed.getUid();
                                    Log.i("MYTAG", "hasil : " + status.getUid());
                                    for (int i = 0; i < statusList.size(); i++) {
                                        if (statusList.get(i).getUid().equals(status.getUid())) {
                                            Log.i("MYTAG", "cobasaja : " + statusList.get(i).getUid());
                                            statusList.remove(i);
                                        }
                                    }
                                    statusListViewed.add(status);

                                    StatusAdapter adapter = new StatusAdapter(getActivity(), statusListViewed);
                                    mViewedStatusRv.setAdapter(adapter);
                                    Toast.makeText(getContext(), "TRUE", Toast.LENGTH_SHORT).show();
                                }

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
