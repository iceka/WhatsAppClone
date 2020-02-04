package com.iceka.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.models.CountryCallingCode;

import java.util.Locale;

public class ContohActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contoh);

        ImageView imageView = findViewById(R.id.flag_img);
        TextView textView = findViewById(R.id.bebeas);

        String[] list = this.getResources().getStringArray(R.array.CountryCodes);
        String[] g = list[0].split(",");
        String pngName = g[1].trim().toLowerCase();
//        imageView.setImageResource(getResources().getIdentifier("drawable/" + pngName, null, getPackageName()));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("country_codes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.i("MYTAG", "info : " + snapshot.getKey());
                    CountryCallingCode countryCallingCode = snapshot.getValue(CountryCallingCode.class);
                    Log.i("MYTAG", "name : " + countryCallingCode.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int flagOffset = 0x1F1E6;
        int asciiOffset = 0x41;
        int coba = 0x1F60A;

        String beb = new String(Character.toChars(coba));

        String country = "SZ";


        int firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset;
        int secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset;

        String flag = new String(Character.toChars(firstChar))
                + new String(Character.toChars(secondChar));

        textView.setText(flag);
    }

    private String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();

        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
}
