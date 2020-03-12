package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.models.CountryCallingCode;

public class InputPhoneNumberActivity extends AppCompatActivity {

    private EditText mEtPhoneNumber;
    private EditText mCountryCode;
    private EditText mEtCountryName;

    private String mPhoneNumber;
    private static final int COUNTRY_REQUEST = 1;
    private String countryDialCode;
    private String countryName;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mCountryCodesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone_number);

        Toolbar mToolbar = findViewById(R.id.toolbar_input_number);
        mEtPhoneNumber = findViewById(R.id.et_phone_number);
        mCountryCode = findViewById(R.id.et_country_code);
        Button mNext = findViewById(R.id.bt_next_input_number);
        mEtCountryName = findViewById(R.id.et_country_name);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mCountryCodesReference = firebaseDatabase.getReference().child("country_codes");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mEtPhoneNumber.requestFocus();


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneNumber = "+" + mCountryCode.getText().toString() + mEtPhoneNumber.getText().toString();
                if (mEtPhoneNumber.getText().toString().length() < 9) {
                    new AlertDialog.Builder(InputPhoneNumberActivity.this)
                            .setMessage("The phone number you entered is too short for the country: "
                                    + mEtCountryName.getText().toString()
                                    + "\n\nInclude your area code if you haven't.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                } else {
                    showProgressDialog();
                }
            }
        });


        mEtCountryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCountryCallingCodeActivity.class);
                intent.putExtra("countrySelected", mEtCountryName.getText().toString());
                startActivityForResult(intent, COUNTRY_REQUEST);
            }
        });

        mProgressDialog = new ProgressDialog(this);

        getData();
    }

    private void getData() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneDefaultLocation = telephonyManager.getNetworkCountryIso().toUpperCase();

        Query myQuery = mCountryCodesReference.orderByChild("code").equalTo(phoneDefaultLocation);
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CountryCallingCode countryCallingCode = snapshot.getValue(CountryCallingCode.class);
                    if (countryDialCode != null) {
                        String tes = countryDialCode.replace("+", "");
                        mEtCountryName.setText(countryName);
                        mCountryCode.setText(tes);
                    } else {
                        String tes = countryCallingCode.getDial_code().replace("+", "");
                        mEtCountryName.setText(countryCallingCode.getName());
                        mCountryCode.setText(tes);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InputPhoneNumberActivity.this);
        builder.setMessage("We will be verifying the phone number:" + "\n\n" + mPhoneNumber + "\n\nIs this OK, or would you like to edit the number?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(InputPhoneNumberActivity.this, PhoneVerifyActivity.class);
                        intent.putExtra("phonenumber", mPhoneNumber);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUNTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                countryName = data.getStringExtra("countryName");
                countryDialCode = data.getStringExtra("countryDialCode");
                String tes = countryDialCode.replace("+", "");
                mEtCountryName.setText(countryName);
                mCountryCode.setText(tes);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input_phone_number, menu);
        return true;
    }

    private void showProgressDialog() {
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Connecting");
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int loading = 0;
                while (loading < 100) {
                    try {
                        Thread.sleep(150);
                        loading += 20;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mProgressDialog.dismiss();
                InputPhoneNumberActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();

                    }
                });
            }
        }).start();
    }

}
