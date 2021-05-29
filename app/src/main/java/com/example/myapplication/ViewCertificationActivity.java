package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.fragment.NavigationCertifyFragment;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.CertificationAdapter;

public class ViewCertificationActivity extends AppCompatActivity {
    FirebaseUser user = null;
    String userId = null;
    FirebaseFirestore firebaseFirestore = null;
    CertificationBoard certificationBoard;
    String contentId;

    TextView title;
    TextView created;
    TextView writer;
    TextView content;

    private static final int MODIFY_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_certification);

        contentId = getIntent().getStringExtra("contentId");
        certificationBoard = (CertificationBoard) getIntent().getSerializableExtra("certification");

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // 제목
        title = findViewById(R.id.view_certifyitem_title);
        title.setText(certificationBoard.getBoardTitle());

        // 작성일
        created = findViewById(R.id.view_certifyitem_created);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createText = timeFormat.format(new Date(certificationBoard.getBoardCreate()));
        created.setText(createText);

        // 작성자
        writer = findViewById(R.id.view_certifyitem_user);
        writer.setText(certificationBoard.getName());

        // 이미지
        Glide.with(this).load(certificationBoard.getCertifyPhoto()).into(
                (ImageView) findViewById(R.id.view_certifyitem_imageview));

        // 내용
        content = findViewById(R.id.view_certifyitem_content);
        content.setText(certificationBoard.getBoardContent());

        // 좋아요
        ImageView likeImage = findViewById(R.id.view_certifyitem_favorite);

        // 댓글 버튼
        FloatingActionButton commentBtn = findViewById(R.id.view_certifyitem_comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("contentId", contentId);
                startActivity(intent);
            }
        });

        // 수정, 삭제 버튼
        Button modifyBtn = findViewById(R.id.modify_certification);
        Button deleteBtn = findViewById(R.id.delete_certification);

        userId = user.getUid();

        if (certificationBoard.getUserId().equals(userId)) {
            modifyBtn.setVisibility(View.VISIBLE);
            modifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentModify = new Intent(getApplicationContext(), AddCertificationActivity.class);
                    intentModify.putExtra("contentId", contentId);
                    startActivityForResult(intentModify, MODIFY_CODE);
                }
            });

            // collection 삭제하려면 nodeJs 를 사용해야 하는데 내가 사용 못함..
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseFirestore.collection("certification").document(contentId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ViewCertificationActivity.this, "인증글을 삭제했습니다", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewCertificationActivity.this, "인증글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show();
                                    Log.d("ViewCertification", "contentId = " + contentId);
                                }
                            });
                }
            });
        } else {
            modifyBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        }
    }
    public void onResume() {
        super.onResume();
    }
}