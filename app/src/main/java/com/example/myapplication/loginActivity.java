package com.example.myapplication;

import android.content.Intent;
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
    private EditText emailLogin;
    private EditText emailPw;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                                } else {
                                    Toast.makeText(loginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();

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
}