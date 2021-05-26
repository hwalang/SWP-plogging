package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

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

    NavigationPloggingFragment navigationPloggingFragment;
    NavigationCertifyFragment navigationCertifyFragment;
    NavigationRecruitFragment navigationRecruitFragment;
    NavigationIssueFragment navigationIssueFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);


        navigationPloggingFragment = new NavigationPloggingFragment();
        navigationCertifyFragment = new NavigationCertifyFragment();
        navigationRecruitFragment = new NavigationRecruitFragment();
        navigationIssueFragment = new NavigationIssueFragment();

        // 첫 화면
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationPloggingFragment).commit();

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
                        case R.id.action_plogging:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationPloggingFragment).commit();
                            return true;
                        case R.id.action_certify:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationCertifyFragment).commit();
                            return true;
                        case R.id.action_recruit:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationRecruitFragment).commit();
                            return true;
                        case R.id.action_issue:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, navigationIssueFragment).commit();
                            return true;
                    }
                    return false;
                }
        );
    }
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(BottomNavigation.this, profileActivity.class);
        startActivity(intent);
        finish();
    }
}
