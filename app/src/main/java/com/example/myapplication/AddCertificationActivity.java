package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.fragment.NavigationCertifyFragment;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class AddCertificationActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage = null;
    FirebaseUser user = null;
    String userId = null;
    String contentId = null;
    FirebaseFirestore firebaseFirestore = null;
    DatabaseReference firebaseDatabase;
    String boardName;
    CertificationBoard certificationBoard;

    Integer CHOOSE_IMAGE_FROM_GALLERY = 0;
    Uri photoUri = null;
    Button write_certification_ok, write_certification_cancel;
    ImageView addPhoto;
    EditText write_certification_title, write_certification_explain;

    private static final String TAG = "AddCertificationActivity";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certification);

        /*초기화
         * 1. 파이어베이스 스토리지
         * 2. 파이어베이스 계정
         * 3. 파이어베이스 스토어*/
        contentId = getIntent().getStringExtra("contentId");


        firebaseStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            Log.d(TAG, "userId = " + userId);

            firebaseFirestore = FirebaseFirestore.getInstance();

            // 갤러리 열기
            Intent photoChooseIntent = new Intent(Intent.ACTION_PICK);
            photoChooseIntent.setType("image/*");
            startActivityForResult(photoChooseIntent, CHOOSE_IMAGE_FROM_GALLERY);

            // 인증글 작성 및 취소 이벤트
            write_certification_ok = findViewById(R.id.write_certification_ok);
            write_certification_cancel = findViewById(R.id.write_certification_cancel);


            write_certification_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String dateString = timestamp.format(new Date());
                    String photoFileName = "IMAGE_" + dateString + "_.png";

                    // 폰에서 파이어베이스 스토리지로 파일 업로드
                    StorageReference storageReference = firebaseStorage.getReference().child("images").child(photoFileName);
                    UploadTask uploadTask = (UploadTask) storageReference.putFile(photoUri);

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Map<String, Object> data = new HashMap<>(); // data 추가 방법

                                // 작성자: userId 를 통해서 name 을 가져온다
                                firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                                firebaseDatabase.child("users").child(userId).child("userName")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.e("firebase", "Error getting data", task.getException());
                                                }
                                                else {
                                                    // 성공
                                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                                    boardName = task.getResult().getValue().toString();

                                                    assert downloadUri != null; // downloadUri.toString() 할 때 있으면 좋은 코드
                                                    certificationBoard = new CertificationBoard(
                                                            userId,
                                                            boardName,
                                                            write_certification_title.getText().toString(),
                                                            write_certification_explain.getText().toString(),
                                                            System.currentTimeMillis(),
                                                            downloadUri.toString()
                                                    );

                                                    // 파이어스토어에 데이터 넣기
                                                    data.put("userId", userId);
                                                    data.put("name", boardName);
                                                    data.put("boardTitle", write_certification_title.getText().toString());
                                                    data.put("boardContent", write_certification_explain.getText().toString());
                                                    data.put("boardCreate", System.currentTimeMillis());
                                                    data.put("certifyPhoto", downloadUri.toString());

                                                    // 수정 또는 작성
                                                    if (contentId != null) {
                                                        DocumentReference documentReference = firebaseFirestore.collection("certification").document(contentId);
                                                        documentReference.set(certificationBoard)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(AddCertificationActivity.this, "수정 완료", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error writing document", e);
                                                                    }
                                                                });
                                                    } else {
                                                        firebaseFirestore.collection("certification")
                                                                .add(data)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @SuppressLint("LongLogTag")
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        documentReference.set(data);
                                                                        Toast.makeText(AddCertificationActivity.this, "파이어스토어에 저장 성공", Toast.LENGTH_SHORT).show();
                                                                        Log.d(TAG, "문서ID: " + documentReference.getId());
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @SuppressLint("LongLogTag")
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(AddCertificationActivity.this, "파이어스토어에 저장 실패", Toast.LENGTH_SHORT).show();
                                                                        Log.w(TAG, "문서 추가 에러", e);
                                                                    }
                                                                });
                                                    }
                                                } // 계정 받아오기
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                            }
                                        });
                            }
                        }
                    });
                }
            });

            write_certification_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // 제목 및 내용 작성 ID
            write_certification_title = findViewById(R.id.write_certification_title);
            write_certification_explain = findViewById(R.id.write_certification_explain);

        } else {
            Toast.makeText(this, "계정이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                // 사진 선택시 경로 받기
                if (data != null) {
                    photoUri = data.getData();
                }
                addPhoto = findViewById(R.id.addPhoto);
                addPhoto.setImageURI(photoUri);
            } else {
                // 선택하지 않고 갤러리를 떠났을 때 일어나는 이벤트
                finish();
            }
        }
    }
}