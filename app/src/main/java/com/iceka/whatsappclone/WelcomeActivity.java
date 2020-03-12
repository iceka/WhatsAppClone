package com.iceka.whatsappclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button mBtAgreeTos = findViewById(R.id.btn_agree_tos);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mBtAgreeTos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, InputPhoneNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if already authenticated
        if (mFirebaseAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }
    }

    private void onAuthSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
