package com.iceka.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceka.whatsappclone.models.StatusItem;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

public class EditStatusActivity extends AppCompatActivity {

    private ImageView mImageView;
    private EditText mEditText;
    private FloatingActionButton mFab;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mStatusReference;
    private StorageReference mStatusStorageReference;

    private Uri imageSource;

    private String imageResourceFromString;
    private String previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        mImageView = findViewById(R.id.preview_image);
        mEditText = findViewById(R.id.et_caption);
        mFab = findViewById(R.id.fab_send_status);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mStatusReference = mFirebaseDatabase.getReference().child("status");
        mStatusStorageReference = mFirebaseStorage.getReference().child("status").child(mFirebaseUser.getUid());

        imageSource = getIntent().getData();
        imageResourceFromString = getIntent().getStringExtra("file");
        previousActivity = getIntent().getStringExtra("from_activity");

        if (imageSource == null) {
            Glide.with(this)
                    .load(imageResourceFromString)
                    .into(mImageView);
        } else if (imageResourceFromString == null) {
            Glide.with(this)
                    .load(imageSource)
                    .into(mImageView);
        }



        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMediaStatus();
            }
        });
    }

    private void sendMediaStatus() {
        final long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        final long expireTime = timestamp + TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);

        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bitmap = mImageView.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        final String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        final StorageReference mediaReference = mStatusStorageReference.child(imageSource.getLastPathSegment());
        mediaReference.putFile(imageSource).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mediaReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference newRef = mStatusReference.child(mFirebaseUser.getUid()).child("statusItem").push();
                        StatusItem statusItem = new StatusItem(newRef.getKey(), "image", uri.toString(), mEditText.getText().toString(), timestamp, expireTime, encoded, null);
                        newRef.setValue(statusItem);
                        finish();
                    }
                });

            }
        });
    }
}
