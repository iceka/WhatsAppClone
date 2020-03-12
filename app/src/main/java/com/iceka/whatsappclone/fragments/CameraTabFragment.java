package com.iceka.whatsappclone.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iceka.whatsappclone.EditStatusActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.adapters.GalleryAdapter;
import com.iceka.whatsappclone.utils.RunTimePermissions;

import java.io.File;
import java.util.ArrayList;

public class CameraTabFragment extends Fragment {

    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private int flashType = 1;

    private RunTimePermissions mRuntimePermission;

    private RecyclerView mRecyclerView;
    private TextureView mTextureView;
    private ImageView mFlash;
    private ImageView mCapture;
    private ImageView mSwitch;

    private CameraX.LensFacing mLensFacing = CameraX.LensFacing.BACK;
    private ImageCapture mImageCapture = null;
    private FlashMode flashMode = FlashMode.AUTO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_camera, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_image_galery);
        mTextureView = rootView.findViewById(R.id.textureview);
        mFlash = rootView.findViewById(R.id.img_flash);
        mCapture = rootView.findViewById(R.id.img_capture);
        mSwitch = rootView.findViewById(R.id.img_switch);

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        } else {
            startCamera();
        }

        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));

        ArrayList<String> images = new ArrayList<>();

        if (images.isEmpty()) {
            images = getAllImages();
            GalleryAdapter adapter = new GalleryAdapter(getContext(), images);
            mRecyclerView.setAdapter(adapter);
        }

        mFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashToggle();
                Log.i("MYTAG", "flash no : " + flashType);
                setFlashIcon();
            }
        });

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

    private void startCamera() {
        bindCamera();

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if (mLensFacing == CameraX.LensFacing.FRONT) {
                    mLensFacing = CameraX.LensFacing.BACK;
                } else {
                    mLensFacing = CameraX.LensFacing.FRONT;
                }
                try {
                    CameraX.getCameraWithLensFacing(mLensFacing);
                    CameraX.unbindAll();
                    bindCamera();
                } catch (CameraInfoUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
                mImageCapture.setFlashMode(flashMode);
                mImageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        Intent intent = new Intent(getContext(), EditStatusActivity.class);
                        intent.putExtra("from_activity", getActivity().getClass().getSimpleName());
                        intent.putExtra("file", file.getAbsolutePath());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic captured failed : " + message;
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void bindCamera() {
        CameraX.unbindAll();

        Rational aspectRatio = new Rational(mTextureView.getWidth(), mTextureView.getHeight());
        Size screenSize = new Size(mTextureView.getWidth(), mTextureView.getHeight());

        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetResolution(screenSize)
                .setLensFacing(mLensFacing)
                .build();

        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) mTextureView.getParent();
                parent.removeView(mTextureView);
                parent.addView(mTextureView, 0);
                mTextureView.setSurfaceTexture(output.getSurfaceTexture());
            }
        });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .setLensFacing(mLensFacing)
                .build();

        mImageCapture = new ImageCapture(imageCaptureConfig);

        CameraX.bindToLifecycle(this, preview, mImageCapture);
    }

    private void setFlashIcon() {
        if (flashType == 1) {
            mFlash.setImageResource(R.drawable.ic_flash_auto_white_24dp);
            flashMode = FlashMode.AUTO;

        } else if (flashType == 2) {
            mFlash.setImageResource(R.drawable.ic_flash_on_white_24dp);
            flashMode = FlashMode.ON;

        } else if (flashType == 3) {
            mFlash.setImageResource(R.drawable.ic_flash_off_white_24dp);
            flashMode = FlashMode.OFF;

        }
    }

    private void flashToggle() {
        if (flashType == 1) {
            flashType = 2;
        } else if (flashType == 2) {
            flashType = 3;
        } else if (flashType == 3) {
            flashType = 1;
        }
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
