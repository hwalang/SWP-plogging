package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 로그인 후 BoardCertify.kt 으로 이동해야 한다..!
public class MainActivity extends AppCompatActivity {
    Button main_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 로그인이후 버튼 클릭시 BottomNavigation 으로 전환
        * */
        main_button = findViewById(R.id.main_button);
        main_button.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BottomNavigation.class);
            startActivity(intent);
        });
    }
}