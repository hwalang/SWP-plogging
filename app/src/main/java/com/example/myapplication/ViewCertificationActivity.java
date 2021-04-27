package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewCertificationActivity extends AppCompatActivity {
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_certification);

        CertificationBoard certificationBoard = (CertificationBoard) getIntent().getSerializableExtra("certification");

        // 제목
        TextView title = findViewById(R.id.view_certifyitem_title);
        title.setText(certificationBoard.getBoardTitle());

        // 작성일
        TextView created = findViewById(R.id.view_certifyitem_created);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createText = timeFormat.format(new Date(certificationBoard.getBoardCreate()));
        created.setText(createText);

        // 작성자
        TextView writer = findViewById(R.id.view_certifyitem_user);
        writer.setText(certificationBoard.getName());

        // 이미지
        Glide.with(this).load(certificationBoard.getCertifyPhoto()).into(
                (ImageView) findViewById(R.id.view_certifyitem_imageview));

        // 내용
        TextView content = findViewById(R.id.view_certifyitem_content);
        content.setText(certificationBoard.getBoardContent());

        // 좋아요
        ImageView likeImage = findViewById(R.id.view_certifyitem_favorite);

        // 좋아요 수
        TextView likeCount = findViewById(R.id.view_certifyitem_favorite_count);

        // 댓글 버튼
        FloatingActionButton commentBtn = findViewById(R.id.view_certifyitem_comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentId = getIntent().getStringExtra("contentId");
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("contentId", contentId);
                startActivity(intent);
            }
        });

        // 수정, 삭제 버튼
        Button modifyBtn = findViewById(R.id.modify_certification);
        Button deleteBtn = findViewById(R.id.delete_certification);
        // 유저 데이터 받아야한다.
//        if (certificationBoard.getName().equals(userData))
        modifyBtn.setVisibility(View.VISIBLE);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewCertificationActivity.this, "인증글 수정", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewCertificationActivity.this, "인증글 삭제", Toast.LENGTH_SHORT).show();
            }
        });


    }
}