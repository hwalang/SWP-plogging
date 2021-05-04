package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.fragment.NavigationIssueFragment;

import org.w3c.dom.Text;

public class SplishActivity extends AppCompatActivity {
    ImageView people, trash;
    TextView plogging;
    public static CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();
    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sleep(1500);
                Intent intent = new Intent(SplishActivity.this, NavigationIssueFragment.class);
                startActivity(intent);
                finish();
            }

        });thread.start();
        setContentView(R.layout.splish);
        people = findViewById(R.id.people);
        trash = findViewById(R.id.trash);
        plogging = findViewById(R.id.plogging);

        Animation mate1 = AnimationUtils.loadAnimation(this,R.anim.people_move);
        people.setAnimation(mate1);


    }

    private void Sleep(int i) {
    }
}
