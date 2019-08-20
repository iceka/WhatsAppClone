package com.iceka.whatsappclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputPhoneNumberActivity extends AppCompatActivity {
    private TextView mPhoneNumber;
    private Toolbar mToolbar;
    private TextView mTitle;

    private EditText mEtPhoneNumber;
    private EditText mCountryCode;
    private Button mNext;

    private String number;

    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone_number);

        mToolbar = findViewById(R.id.toolbar_input_number);
        mTitle = mToolbar.findViewById(R.id.toolbar_title_input_number);
        mEtPhoneNumber = findViewById(R.id.et_phone_number);
        mCountryCode = findViewById(R.id.et_country_code);
        mNext = findViewById(R.id.bt_next_input_number);


        setSupportActionBar(mToolbar);
        mTitle.setText(mToolbar.getTitle());

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = "+" + mCountryCode.getText().toString() + mEtPhoneNumber.getText().toString();

                showDialog();
                Toast.makeText(InputPhoneNumberActivity.this, "bisa di click: " + number, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        mDialog = new AlertDialog.Builder(InputPhoneNumberActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_phone_verify, null);
        mDialog.setView(dialogView);
        mDialog.setCancelable(true);

        mPhoneNumber = dialogView.findViewById(R.id.tv_phone_number_dialog);
        mPhoneNumber.setText(number);

        mDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(InputPhoneNumberActivity.this, PhoneVerifyActivity.class);
                intent.putExtra("phonenumber", number);
                startActivity(intent);
            }
        });

        mDialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mDialog.show();
    }
}
