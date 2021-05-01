package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView editText1;
    private TextView editText2;
    private TextView editText3;
    private ImageView profile;
    private Button menu;
    private Button navigation;
    String profileImageUrl;

    DatabaseReference firebaseDatabase;

    FirebaseUser user;
    String userId = null;
    String rankingName = null;
    Long stepCount = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();




        editText1 = findViewById(R.id.profileName);
        editText2 = findViewById(R.id.profileRank);
        editText3 = findViewById(R.id.profileStep);
        profile = findViewById(R.id.proFile);
        menu = findViewById(R.id.menu);
        navigation = findViewById(R.id.btn_Navigation);

        user =FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("users").child(userId).child("userName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            String userName = task.getResult().getValue().toString();
                            editText1.setText(userName);
                                               }
                                           }
                                       });
        firebaseDatabase.child("users").child(userId).child("profileImageUrl")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            profileImageUrl = task.getResult().getValue().toString();
                            Glide.with(profileActivity.this).load(profileImageUrl).into(profile);
                        }
                    }
                });


                        // 메뉴 버튼은 만보기가 구현된 후 intent 할 예정

                        navigation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(profileActivity.this, BottomNavigation.class);
                                startActivity(intent);
                                finish();
                            }
                        });

    }

}
