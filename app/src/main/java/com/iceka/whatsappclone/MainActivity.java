package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.TabAdapter;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabBottom;
    private FloatingActionButton mFabTop;
    private static boolean fabTopVisible = false;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private FloatingActionButton mFab;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.viewpager);
//        mFabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
//        mFabTop = (FloatingActionButton) findViewById(R.id.fab_top);
        mToolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.appbar_layout);
        mFab = findViewById(R.id.fab);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabAdapter = new TabAdapter(this, getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setCurrentItem(1);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_alt_white_24dp);

        LinearLayout layout = ((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layout.setLayoutParams(layoutParams);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());

        fabSettings();

    }

    private void fabSettings() {
        if (mViewPager.getCurrentItem() == 1) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                }
            });
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        mFab.hide();
                        break;
                    case 1:
                        mFab.hide();
                        mFab.setImageResource(R.drawable.ic_comment_white_24dp);
                        mFab.show();
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "view 1", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                            }
                        });
                        break;
                    case 2:
                        mFab.setImageResource(R.drawable.ic_camera_alt_white_24dp);
                        mFab.hide();
                        mFab.show();
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "view 2", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    default:
                        mFab.hide();
                        mFab.setImageResource(R.drawable.ic_phone_black_white_24dp);
                        mFab.show();
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "view 3", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                }

                if (position == 0) {
                    mToolbar.setVisibility(View.GONE);
                    mAppBarLayout.setExpanded(false, true);
                } else {
                    mToolbar.setVisibility(View.VISIBLE);
                    mAppBarLayout.setExpanded(true, true);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userReference.child("online").setValue(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long lastSeen = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        userReference.child("online").setValue(false);
        userReference.child("lastSeen").setValue(lastSeen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
