package com.iceka.whatsappclone.fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.utils.RunTimePermissions;

public class CameraTabFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener {

    private RunTimePermissions mRuntimePermission;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_camera, container, false);
//
//        mRuntimePermission = new RunTimePermissions(getActivity());
//        mRuntimePermission.requestPermission(new String[]{Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        }, new RunTimePermissions.RunTimePermissionListener() {
//            @Override
//            public void permissionGranted() {
//                return;
//            }
//
//            @Override
//            public void permissionDenied() {
//
//            }
//        });

        return rootView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mRuntimePermission != null) {
            mRuntimePermission.onRequestPermissionResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
