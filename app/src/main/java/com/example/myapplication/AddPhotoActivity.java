package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage = null;
    Integer CHOOSE_IMAGE_FROM_GALLERY = 0;
    Uri photoUri = null;

    Button uploadBtnAddPhoto;
    ImageView addPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        // 파이어베이스 스토리지 초기화
        firebaseStorage = FirebaseStorage.getInstance();

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
                photoUri = data.getData();
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
        UploadTask uploadTask = (UploadTask) storageReference.putFile(photoUri)
                .addOnSuccessListener(taskSnapshot -> Toast.makeText(getApplicationContext(), "폰에서 사진 업로드", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "사진 업로드 실패", Toast.LENGTH_SHORT).show());
    }
}