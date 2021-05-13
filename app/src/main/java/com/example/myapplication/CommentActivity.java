package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.Cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.CertificationAdapter;
import adapter.CommentAdapter;

import static android.content.ContentValues.TAG;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";
    private EditText commentEdit;
    private Button commentSend;

    private CommentAdapter commentAdapter;
    private RecyclerView recyclerview;
    private ArrayList<CertificationBoard.Comment> commentList;

    private FirebaseUser firebaseUser;
    private String userId = null;
    private DatabaseReference firebaseDatabase;
    private String userName;
    private String userProfileUrl;

    private FirebaseFirestore firebaseFirestore;
    private CertificationBoard.Comment commentInfo;

    private String contentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        contentId = getIntent().getStringExtra("contentId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        commentList = new ArrayList<>();

        // 댓글 가져오기: resume
        recyclerview = findViewById(R.id.comment_recyclerview);
        recyclerview.setHasFixedSize(true);



        recyclerview.setLayoutManager(layoutManager);

        // 입력 data
        commentEdit = findViewById(R.id.comment_edit);
        commentSend = findViewById(R.id.comment_btn_send);
        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 작성자 이름 가져오기
                firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseDatabase.child("users").child(userId).child("userName")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    // 성공
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                                    userName = task.getResult().getValue().toString();
                                    firebaseDatabase.child("users").child(userId).child("profileImageUrl")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.e("firebase", "Error getting data", task.getException());
                                                    } else {
                                                        // 성공
                                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));

                                                        userProfileUrl = task.getResult().getValue().toString();
                                                        long timestamp = System.currentTimeMillis();

                                                        commentInfo = new CertificationBoard.Comment(
                                                                userId,
                                                                userProfileUrl,
                                                                userName,
                                                                commentEdit.getText().toString(),
                                                                timestamp);

                                                        Map<String, Object> commentData = new HashMap<>();
                                                        commentData.put("userId", userId);
                                                        commentData.put("profileUrl", userProfileUrl);
                                                        Log.d("profile", "userProfile = " + userProfileUrl);
                                                        commentData.put("name", userName);
                                                        commentData.put("comment", commentEdit.getText().toString());
                                                        commentData.put("commentCreate", timestamp);

                                                        // certification 안의 문서에 새로운 collection(comments) 을 추가해야 한다.
                                                        firebaseFirestore = FirebaseFirestore.getInstance();
                                                        CollectionReference collectionReference = firebaseFirestore.collection("certification").document(contentId).collection("comments");
                                                        // contentId 가 같은 문서 안에 comments 생성
                                                        collectionReference.document()
                                                                .set(commentData, SetOptions.merge())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        onResume();
                                                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error writing document", e);
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                Log.d("documentId", "documentId = " + contentId);
            }
        });
    }

    public void onResume() {
        super.onResume();
        commentAdapter = new CommentAdapter(this, commentList, contentId);
        commentEdit = findViewById(R.id.comment_edit);
        commentEdit.setText("");
        recyclerview.setAdapter(commentAdapter);
    }
}