package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.adapters.AdapterCountryCallingCode;
import com.iceka.whatsappclone.models.CountryCallingCode;

import java.util.ArrayList;
import java.util.List;

public class SelectCountryCallingCodeActivity extends AppCompatActivity {

    private RecyclerView mRvCountry;
    private Toolbar mToolbar;

    private List<CountryCallingCode> countryCallingCodeList = new ArrayList<>();

    private DatabaseReference mCountryCodeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country_calling_code);

        mRvCountry = findViewById(R.id.rv_country_calling_code);
        mToolbar = findViewById(R.id.toolbar_select_calling_code);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mCountryCodeReference = firebaseDatabase.getReference().child("country_codes");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRvCountry.setLayoutManager(layoutManager);
        mRvCountry.setItemAnimator(new DefaultItemAnimator());
        mRvCountry.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        getData();
    }

    private void getData() {
        mCountryCodeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CountryCallingCode countryCallingCode = snapshot.getValue(CountryCallingCode.class);
                    countryCallingCodeList.add(countryCallingCode);
                    AdapterCountryCallingCode adapter = new AdapterCountryCallingCode(getApplicationContext(), countryCallingCodeList);
                    mRvCountry.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_country_calling_code, menu);
        return true;
    }
}
