package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.schema.CertificationBoard;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import adapter.ChatAdapter;

public class ChatRoomActivity extends AppCompatActivity {
    private TextView        chattingRoomName;
    private RecyclerView    recyclerView;
    private EditText        chatEdit;
    private Button          chatSendBtn;

    private ChatAdapter                     chatAdapter;
    private ArrayList<RecruitBoard.Chat>    chats;
    private RecyclerView.LayoutManager      layoutManager;

    private FirebaseUser        user;
    private DatabaseReference   databaseReference;
    private FirebaseFirestore   firebaseFirestore;
    private RecruitBoard.Chat   chatInfo;
    private String              userName;
    private String              userProfileUrl;

    private String contentId        = null;
    private String userId           = null;
    private String recruitUserId    = null;
    private static final String TAG = "ChatRoomActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chattingRoomName = findViewById(R.id.frame_chat);
        chatEdit         = findViewById(R.id.chat_edit);
        chatSendBtn      = findViewById(R.id.chat_btn_send);
        recyclerView     = findViewById(R.id.chat_recyclerview);

        contentId           = getIntent().getStringExtra("contentId");
        recruitUserId       = getIntent().getStringExtra("recruitUserId");
        firebaseFirestore   = FirebaseFirestore.getInstance();
        user                = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        // 채팅방 이름
        firebaseFirestore.collection("recruitment").document(contentId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        chattingRoomName.setText(String.valueOf(Objects.requireNonNull(document.getData()).get("title")));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        layoutManager = new LinearLayoutManager(this);
        chats         = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("users").child(userId).child("userName")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    // 성공
                                    Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));

                                    userName = Objects.requireNonNull(task.getResult().getValue()).toString();
                                    databaseReference.child("users").child(userId).child("profileImageUrl")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.e("firebase", "Error getting data", task.getException());
                                                    } else {
                                                        // 성공
                                                        Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));

                                                        userProfileUrl = Objects.requireNonNull(task.getResult().getValue()).toString();
                                                        long timestamp = System.currentTimeMillis();

                                                        chatInfo = new RecruitBoard.Chat(
                                                                contentId,
                                                                userId,
                                                                userProfileUrl,
                                                                userName,
                                                                chatEdit.getText().toString(),
                                                                timestamp);

                                                        Map<String, Object> chatData = new HashMap<>();
                                                        chatData.put("chatRoom", contentId);
                                                        chatData.put("userChatId", userId);
                                                        chatData.put("profileUrl", userProfileUrl);
                                                        chatData.put("chatName", userName);
                                                        chatData.put("message", chatEdit.getText().toString());
                                                        chatData.put("chatCreate", timestamp);

                                                        CollectionReference collectionReference = firebaseFirestore.collection("recruitment").document(contentId).collection("chats");
                                                        // contentId 가 같은 문서 안에 comments 생성
                                                        collectionReference.document()
                                                                .set(chatData, SetOptions.merge())
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
                // 초마다 갱신될까?
                chatAdapter.notifyDataSetChanged();
                Log.d("documentId", "documentId = " + contentId);
            }
        });
    }
    public void onResume() {
        super.onResume();
        chatAdapter = new ChatAdapter(this, chats, contentId, recruitUserId);
        chatEdit = findViewById(R.id.chat_edit);
        chatEdit.setText("");
        recyclerView.setAdapter(chatAdapter);
    }

    // 초마다 갱신될까?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatAdapter.notifyDataSetChanged();
    }
}