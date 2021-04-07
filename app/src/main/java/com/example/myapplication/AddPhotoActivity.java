package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.schema.Board;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddPhotoActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage = null;
    FirebaseAuth auth = null;
    FirebaseFirestore firebaseFirestore = null;

    Integer CHOOSE_IMAGE_FROM_GALLERY = 0;
    Uri photoUri = null;
    Button uploadBtnAddPhoto;
    ImageView addPhoto;

    Board board;
    Board.Certification certificationBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        /*초기화
        * 1. 파이어베이스 스토리지
        * 2. 파이어베이스 계정
        * 3. 파이어베이스 스토어*/
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // 갤러리 열기
        Intent photoChooseIntent = new Intent(Intent.ACTION_PICK);
        photoChooseIntent.setType("image/*");
        startActivityForResult(photoChooseIntent, CHOOSE_IMAGE_FROM_GALLERY);

        // 사진 업로드 이벤트
        uploadBtnAddPhoto = findViewById(R.id.uploadBtnAddPhoto);
        uploadBtnAddPhoto.setOnClickListener(view -> explainAndUploadPhoto());
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

    // 이건 인증글 작성때 사용하는게 좋겠다.
    // 사진 이름 자동으로 정하기 + 파일 업로드
    private void explainAndUploadPhoto() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateString = timestamp.format(new Date());
        String photoFileName = "PHOTO_" + dateString + "_.png";

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
                    board = new Board("plogger@email.com", "테스트", "테스트 중 입니다.", "김동현", (long) 20210407);
                    certificationBoard = new Board.Certification(1, downloadUri);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }
}