package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.schema.RecruitBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddRecruitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseUser            user = null;
    String                  userId = null;
    String                  contentId = null;
    FirebaseFirestore       firebaseFirestore = null;
    DatabaseReference       firebaseDatabase;
    RecruitBoard            recruitBoard;
    String                  userName;

    EditText                title,
                            content,
                            recruitMonth,
                            recruitDay;
    TextView                address;
    Button                  ok,
                            cancel;

    Spinner                 spinnerMember;
    ArrayAdapter<String>    adapterMember;
    int                     recruitMemberNumber;
    String                  day;
    String                  month;
    String                  userAddress;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recruit);

        title               = findViewById(R.id.recruit_title);
        content             = findViewById(R.id.recruit_content);
        address             = findViewById(R.id.recruit_address);
        ok                  = findViewById(R.id.recruit_ok);
        cancel              = findViewById(R.id.recruit_cancel);
        spinnerMember       = findViewById(R.id.spinner_meeting_number);
        recruitMonth        = findViewById(R.id.recruit_month);
        recruitDay          = findViewById(R.id.recruit_day);

        // set spinner
        final String[] meetingNumbers = {"2명", "3명", "4명"};

        adapterMember = new ArrayAdapter<>(this
                , android.R.layout.simple_spinner_item, meetingNumbers);
        adapterMember.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMember.setAdapter(adapterMember);

        // select item
        spinnerMember.setOnItemSelectedListener(this);

        contentId = getIntent().getStringExtra("contentId");
        user      = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

            month           = recruitMonth.getText().toString();
            day             = recruitDay.getText().toString();

            firebaseDatabase.child("users").child(userId).child("address")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                // 성공
                                Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                                String userAddress = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());

                                address.setText(userAddress);
                            }
                        }
                    });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseDatabase.child("users").child(userId).child("userName")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Map<String, Object> data = new HashMap<>();
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    } else {
                                        Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                                        userName = Objects.requireNonNull(task.getResult().getValue()).toString();

                                        firebaseDatabase.child("users").child(userId).child("address")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        Map<String, Object> data = new HashMap<>();
                                                        if (!task.isSuccessful()) {
                                                            Log.e("firebase", "Error getting data", task.getException());
                                                        } else {
                                                            Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                                                            userAddress = Objects.requireNonNull(task.getResult().getValue()).toString();

                                                            // 주소는 아직 안 넣음
                                                            recruitBoard = new RecruitBoard(
                                                                    userId,
                                                                    userName,
                                                                    title.getText().toString(),
                                                                    content.getText().toString(),
                                                                    System.currentTimeMillis(),
                                                                    month,
                                                                    day,
                                                                    recruitMemberNumber,
                                                                    1,
                                                                    userAddress,
                                                                    userId
                                                            );

                                                            data.put("userId", userId);
                                                            data.put("userName", userName);
                                                            data.put("title", title.getText().toString());
                                                            data.put("content", content.getText().toString());
                                                            data.put("recruitCreate", System.currentTimeMillis());
                                                            data.put("month", recruitMonth.getText().toString());
                                                            data.put("day", recruitDay.getText().toString());
                                                            data.put("totalMember", recruitMemberNumber);
                                                            data.put("nowMember", 1);
                                                            data.put("clickCount", recruitBoard.getClickCount());
                                                            data.put("deadLine", recruitBoard.isDeadlineCheck());
                                                            data.put("chatUserId1", userId);
                                                            data.put("address", userAddress);

                                                            if (contentId != null) {
                                                                DocumentReference documentReference = firebaseFirestore.collection("recruitment").document(contentId);
                                                                documentReference.set(data)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d("AddRecruitActivity", "success modification");
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w("AddRecruitActivity", "Error writing document", e);
                                                                            }
                                                                        });
                                                            } else {
                                                                firebaseFirestore.collection("recruitment")
                                                                        .add(data)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                documentReference.set(data);
                                                                                Log.d("documentId", "문서 id = " + documentReference.getId());
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(AddRecruitActivity.this, "모집글 작성 실패", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) == "2명") {
            recruitMemberNumber = 2;
        } else if (parent.getItemAtPosition(position) == "3명") {
            recruitMemberNumber = 3;
        } else {
            recruitMemberNumber = 4;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        recruitMemberNumber = 2;
    }
}