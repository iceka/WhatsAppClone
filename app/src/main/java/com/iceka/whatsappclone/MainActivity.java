package com.iceka.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.iceka.whatsappclone.adapters.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabBottom;
    private FloatingActionButton mFabTop;
    private static boolean fabTopVisible = false;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
        mFabTop = (FloatingActionButton) findViewById(R.id.fab_top);
        mToolbar = (Toolbar) findViewById(R.id.appbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

//        mFabTop.show();

        mTabAdapter = new TabAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
//        mViewPager.setCurrentItem(1);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_alt_white_24dp);

        mTabLayout.getTabAt(1).select();

        LinearLayout layout = ((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layout.setLayoutParams(layoutParams);

        mFabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        if (mViewPager.getCurrentItem() == 1) {
            mFabTop.hide();
            mFabBottom.setImageResource(R.drawable.ic_comment_white_24dp);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mFabTop.hide();
//                        mFabBottom.hide();
//                        mFabBottom.setImageResource(R.drawable.ic_comment_white_24dp);
                        break;
                    case 1:
                        mFabBottom.show();
                        mFabTop.hide();
                        mFabBottom.setImageResource(R.drawable.ic_comment_white_24dp);
                        break;
                    case 2:
                        mFabTop.show();
                        mFabBottom.show();
                        mFabBottom.setImageResource(R.drawable.ic_camera_alt_white_24dp);
                        break;
                    default:
                        mFabBottom.setImageResource(R.drawable.ic_phone_black_white_24dp);
                        mFabTop.hide();
                        mFabBottom.show();
                        break;
                }
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
