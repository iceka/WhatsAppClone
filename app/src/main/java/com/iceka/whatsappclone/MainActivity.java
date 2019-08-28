package com.iceka.whatsappclone;

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
import com.iceka.whatsappclone.adapters.TabAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mFabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
//        mFabTop = (FloatingActionButton) findViewById(R.id.fab_top);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//        }
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabAdapter = new TabAdapter(this, getSupportFragmentManager());
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


//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0) {
//                    Toast.makeText(MainActivity.this, "contoh we", Toast.LENGTH_SHORT).show();
//                    mFab.hide();
//                    mToolbar.hideOverflowMenu();
//                } else {
//                    mFab.show();
//                    fabSettings();
//                    mFab.show();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        fabSettings();


//        if (mViewPager.getCurrentItem() == 1) {
//            mFabTop.hide();
//            mFabBottom.setImageResource(R.drawable.ic_comment_white_24dp);
//        }
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                switch (position) {
//                    case 0:
//                        mFabTop.hide();
//                        mFabBottom.hide();
//                        break;
//                    case 1:
//                        mFabTop.hide();
//                        mFabBottom.show();
//                        mFabBottom.setImageResource(R.drawable.ic_comment_white_24dp);
//                        break;
//                    case 2:
//                        mFabTop.show();
//                        mFabBottom.show();
//                        mFabBottom.setImageResource(R.drawable.ic_camera_alt_white_24dp);
//                        break;
//                    default:
//                        mFabTop.hide();
//                        mFabBottom.show();
//                        mFabBottom.setImageResource(R.drawable.ic_phone_black_white_24dp);
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
