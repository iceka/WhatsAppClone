package com.iceka.whatsappclone.tes;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.Status;
import com.iceka.whatsappclone.models.StatusText;

import java.util.ArrayList;
import java.util.List;

public class TesActivity extends AppCompatActivity {

    private List<Status> statusList = new ArrayList<>();
    private List<StatusText> statusTextList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tes_activity_tes);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.tesRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final DatabaseReference statusReference = firebaseDatabase.getReference().child("status");
        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Status status = snapshot.getValue(Status.class);
//                    StatusText statusText = status.getTypeStatus();
                    statusReference.child(snapshot.getKey()).child("typeStatus").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                StatusText statusText1 = snapshot1.getValue(StatusText.class);
                                statusTextList.add(statusText1);
                                Log.i("MYTAG", "Cobalagi : " + statusText1.getText());
                                TesAdapter adapter = new TesAdapter(getApplicationContext(), statusTextList);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    Log.i("MYTAG", "COBS : " + snapshot.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
