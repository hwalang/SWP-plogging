package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParkActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference().child("park");
    parkAdapter mAdapter;
    ArrayList<parkModel> list;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        recyclerView = findViewById(R.id.park_recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<parkModel>();
        mAdapter = new parkAdapter(list,this);
        recyclerView.setAdapter(mAdapter);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    parkModel model = dataSnapshot.getValue(parkModel.class);
                    list.add(model);
                    String value = dataSnapshot.getValue().toString();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}