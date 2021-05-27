package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Member;

public class loginActivity extends AppCompatActivity {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private EditText emailLogin;
    private EditText emailPw;
    String loginId, loginPwd;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.gotoPasswordResetButton).setOnClickListener(onClickListener);



        SharedPreferences pref = getSharedPreferences("mine", MODE_PRIVATE);
        loginId = pref.getString("email", null);
        loginPwd = pref.getString("pwd", null);

        if (loginId != null || loginPwd != null) {
            Intent intent = new Intent(loginActivity.this, profileActivity.class);
            startActivity(intent);
            finish();
        }


        Button join = (Button) findViewById(R.id.registerBtn);
        Button login = (Button) findViewById(R.id.Login);
        emailLogin = (EditText)findViewById(R.id.userIdRogin);
        emailPw = (EditText)findViewById(R.id.userPwRogin);


        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString().trim();
                String pwd = emailPw.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(loginActivity.this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }



                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(loginActivity.this, profileActivity.class);
                                    startActivity(intent);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("email", email);
                                    editor.putString("pwd", pwd);
                                    editor.commit();
                                    finish();
                                } else {
                                    Toast.makeText(loginActivity.this, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, registerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()){
            case R.id.gotoPasswordResetButton:
                Intent intent = new Intent(this, passwordReset.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}