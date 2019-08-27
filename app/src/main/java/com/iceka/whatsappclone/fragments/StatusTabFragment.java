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

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iceka.whatsappclone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusTabFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_tab, container, false);
        setHasOptionsMenu(true);

        CircleImageView avatar = (CircleImageView) rootView.findViewById(R.id.avatar_user_status);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toast.makeText(getActivity(), "ini adalah nama nya: " + mFirebaseUser.getPhotoUrl(), Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(mFirebaseUser.getPhotoUrl())
                .into(avatar);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_status, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
