package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.iceka.whatsappclone.adapters.GalleryAdapter;

import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity {

    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_CODE_PERMISSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        RecyclerView mRecyclerView = findViewById(R.id.rv_image_galery);

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_image_galery);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, RecyclerView.HORIZONTAL, false));

        ArrayList<String> images = new ArrayList<>();

        if (images.isEmpty()) {
            images = getAllImages();
            GalleryAdapter adapter = new GalleryAdapter(getApplicationContext(), images);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions not granted by the user. " + allPermissionsGranted(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permissions : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
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

        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

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
