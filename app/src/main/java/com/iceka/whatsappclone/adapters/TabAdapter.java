package com.iceka.whatsappclone.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iceka.whatsappclone.fragments.CallsTabFragment;
import com.iceka.whatsappclone.fragments.CameraTabFragment;
import com.iceka.whatsappclone.fragments.ChatTabFragment;
import com.iceka.whatsappclone.fragments.StatusTabFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CameraTabFragment();
            case 1:
                return new ChatTabFragment();
            case 2:
                return new StatusTabFragment();
            default:
                return new CallsTabFragment();

        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return null;
        } else if (position == 1) {
            return "Chats";
        } else if (position == 2) {
            return "Status";
        } else {
            return "Calls";
        }
    }
}
