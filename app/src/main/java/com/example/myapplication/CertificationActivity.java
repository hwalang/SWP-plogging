package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.schema.CertificationBoard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CertificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);

        Intent intent = getIntent();
        CertificationBoard certificationBoard = (CertificationBoard) intent.getSerializableExtra("certificationBoard");

        // 제목
        TextView certifyitem_title = (TextView) findViewById(R.id.certifyitem_title);
        certifyitem_title.setText(certificationBoard.getBoardTitle());

        // 작성일
        TextView certifyitem_created = findViewById(R.id.certifyitem_created);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String created = timeFormat.format(new Date(certificationBoard.getBoardCreate()));
        certifyitem_created.setText(created);

        // 작성자
        TextView certifyitem_user = findViewById(R.id.certifyitem_user);
        certifyitem_user.setText(certificationBoard.getName());

        // Image
        Glide.with(this).load(certificationBoard.getCertifyPhoto()).into(
                (ImageView) findViewById(R.id.certifyitem_imageview_content)
        );

        // 내용
        TextView certifyitem_content_textview = findViewById(R.id.certifyitem_content_textview);
        certifyitem_content_textview.setText(certificationBoard.getBoardContent());

        // 좋아요 이미지를 누르면 좋아요 수 증가
        findViewById(R.id.certifyitem_favorite_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "좋아요 수 증가", Toast.LENGTH_SHORT).show();
            }
        });
        // 좋아요 수
        TextView certifyitem_favorite_count = findViewById(R.id.certifyitem_favorite_count);
        certifyitem_favorite_count.setText(String.valueOf(certificationBoard.getCertifyFeel()));

        // 댓글 누르면 댓글창으로 이동
        findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "댓글창으로 이동", Toast.LENGTH_SHORT).show();
            }
        });
    }
}