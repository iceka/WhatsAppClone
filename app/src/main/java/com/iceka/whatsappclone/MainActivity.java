package com.iceka.whatsappclone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iceka.whatsappclone.adapters.TabAdapter;
import com.iceka.whatsappclone.fragments.CameraTabFragment;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabBottom;
    private FloatingActionButton mFabTop;
    private static boolean fabTopVisible = false;
    private static final int RC_PHOTO_PICKER = 2;
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
        mFabBottom = findViewById(R.id.fab_bottom);
        mFabTop = findViewById(R.id.fab_top);
        mToolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.appbar_layout);
//        mFab = findViewById(R.id.fab);

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
            mFabBottom.setOnClickListener(new View.OnClickListener() {
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
                        mFabTop.hide();
                        mFabBottom.hide();
//                        CameraTabFragment cameraTabFragment = new CameraTabFragment();
//                        cameraTabFragment.startCamera();
                        break;
                    case 1:
                        mFabBottom.hide();
                        mFabBottom.setImageResource(R.drawable.ic_comment_white_24dp);
                        mFabTop.hide();
                        mFabBottom.show();

                        mFabBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                            }
                        });
                        break;
                    case 2:
                        mFabTop.hide();
                        mFabBottom.setImageResource(R.drawable.ic_camera_alt_white_24dp);
                        mFabTop.show();
                        mFabBottom.hide();
                        mFabBottom.show();
                        mFabTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MainActivity.this, StatusTextActivity.class));
                            }
                        });
                        mFabBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/jpeg");
                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                            }
                        });
                        break;
                    default:
                        mFabTop.hide();
                        mFabBottom.hide();
                        mFabBottom.setImageResource(R.drawable.ic_phone_black_white_24dp);
                        mFabBottom.show();
                        mFabBottom.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                Intent intent = new Intent(this, EditStatusActivity.class);
                intent.setData(selectedImage);
//                intent.putExtra("imagenya", selectedImage);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "data is null", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mFirebaseAuth.signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(1);
        }
    }
}
