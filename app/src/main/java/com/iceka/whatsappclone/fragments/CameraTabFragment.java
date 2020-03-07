package com.iceka.whatsappclone.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iceka.whatsappclone.CameraActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.adapters.GalleryAdapter;
import com.iceka.whatsappclone.utils.RunTimePermissions;

import java.util.ArrayList;

public class CameraTabFragment extends Fragment {

    private RunTimePermissions mRuntimePermission;
    private RecyclerView mRecyclerView;

    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_CODE_PERMISSIONS = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_camera, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_image_galery);

        CameraActivity cameraActivity = new CameraActivity();
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));

        ArrayList<String> images = new ArrayList<>();

        if (images.isEmpty()) {
            images = getAllImages();
            GalleryAdapter adapter = new GalleryAdapter(getContext(), images);
            mRecyclerView.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permissions not granted by the user. " + allPermissionsGranted(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permissions : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> getAllImages() {
        ArrayList<String> images = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
        };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        try {
            cursor.moveToFirst();
            do {
                images.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<String> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > 0; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }
}
