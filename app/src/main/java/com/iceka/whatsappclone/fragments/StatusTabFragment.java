package com.iceka.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iceka.whatsappclone.R;

public class StatusTabFragment extends Fragment {

    private ViewPager mViewPager;
    private FloatingActionButton mFabBottom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_tab, container, false);
        setHasOptionsMenu(true);
        View parentView = inflater.inflate(R.layout.activity_main, container, false);
//        mFabBottom = (FloatingActionButton)parentView.findViewById(R.id.fab_bottom);
//        Toast.makeText(getContext(), "ini view 2", Toast.LENGTH_SHORT).show();

//        mFabBottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Berhasil cuk", Toast.LENGTH_SHORT).show();
//            }
//        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_status, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
