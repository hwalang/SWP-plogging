package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.fragment.NavigationCertifyFragment;
import com.example.myapplication.fragment.NavigationIssueFragment;
import com.example.myapplication.fragment.NavigationPloggingFragment;
import com.example.myapplication.fragment.NavigationRecruitFragment;
import com.example.myapplication.fragment.NavigationUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BottomNavigation extends AppCompatActivity {
    NavigationUserFragment navigationUserFragment;
    NavigationPloggingFragment navigationPloggingFragment;
    NavigationCertifyFragment navigationCertifyFragment;
    NavigationRecruitFragment navigationRecruitFragment;
    NavigationIssueFragment navigationIssueFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);

        navigationUserFragment = new NavigationUserFragment();
        navigationPloggingFragment = new NavigationPloggingFragment();
        navigationCertifyFragment = new NavigationCertifyFragment();
        navigationRecruitFragment = new NavigationRecruitFragment();
        navigationIssueFragment = new NavigationIssueFragment();

        // 첫 화면
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationUserFragment).commit();

        // 내비게이션 이벤트
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 기본 프래그먼트 정하기
        //bottomNavigationView.setSelectedItemId(R.id.main_user);
        /*
         *  bottomNavigation 버튼 클릭시 이벤트
         *  1. 사용자 정보
         *  2. 플로깅
         *  3. 인증글
         *  4. 모집글
         *  5. 환경이슈
         * */
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_user:
                            Toast.makeText(getApplicationContext(), "user 선택", Toast.LENGTH_SHORT).show();
                            // main_content: fragment id
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationUserFragment).commit();
                            return true;
                        case R.id.action_plogging:
                            Toast.makeText(getApplicationContext(), "plogging 선택", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationPloggingFragment).commit();
                            return true;
                        case R.id.action_certify:
                            Toast.makeText(getApplicationContext(), "certify 선택", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationCertifyFragment).commit();
                            return true;
                        case R.id.action_recruit:
                            Toast.makeText(getApplicationContext(), "recruit 선택", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationRecruitFragment).commit();
                            return true;
                        case R.id.action_issue:
                            Toast.makeText(getApplicationContext(), "issue 선택", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationIssueFragment).commit();
                            return true;
                    }
                    return false;
                }
        );
    }
}
