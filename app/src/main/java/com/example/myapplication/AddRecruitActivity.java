package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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

        contentId = getIntent().getStringExtra("contentId");    // 수정 및 삭제
        user      = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

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
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        userName = task.getResult().getValue().toString();

                                        // 주소는 아직 안 넣음
                                        recruitBoard = new RecruitBoard(
                                                userId,
                                                userName,
                                                title.getText().toString(),
                                                content.getText().toString(),
                                                System.currentTimeMillis(),
                                                recruitMonth.getText().toString(),
                                                recruitDay.getText().toString(),
                                                recruitMemberNumber,
                                                1
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

                                        firebaseFirestore.collection("recruitment")
                                                .add(data)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        documentReference.set(data);
                                                        Toast.makeText(AddRecruitActivity.this, "모집글 작성", Toast.LENGTH_SHORT).show();
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
                                    finish();
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
        } else {
            Toast.makeText(this, "계정 없음", Toast.LENGTH_SHORT).show();
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