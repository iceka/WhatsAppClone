package com.iceka.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.iceka.whatsappclone.adapters.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabBottom;
    private FloatingActionButton mFabTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mFabBottom = (FloatingActionButton)findViewById(R.id.fab_bottom);
//        mFabTop = (FloatingActionButton)findViewById(R.id.fab_top);

        mTabAdapter = new TabAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_alt_white_24dp);

        LinearLayout layout = ((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layout.setLayoutParams(layoutParams);
    }
}
