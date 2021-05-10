package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.schema.RecruitBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ViewRecruitActivity extends AppCompatActivity implements View.OnClickListener {
    TextView viewTitle,
             viewCreate,
             viewWriter,
             viewAddress,
             viewDate,
             viewContent,
             viewNowMember,
             viewTotalMember,
             viewDeadLine;

    Button   viewApplyBtn,
             viewEnterChatBtn,
             viewModifyBtn,
             viewDeleteBtn;

    FirebaseUser user                   = null;
    String userId                       = null;
    FirebaseFirestore firebaseFirestore = null;

    RecruitBoard            recruitBoard;
    String                  contentId;
    String                  userName;
    int                     nowMember;
    int                     totalMember;
    String                  chatRoom;
    NotificationManager     notificationManager;
    NotificationChannel     notificationChannel;
    Notification.Builder    builder;
    DatabaseReference       databaseReference;

    private final Integer RECRUIT_MODIFY_CODE   = 102;
    private final String PRIMARY_CHANNEL_ID     = "primary_notification_channel";
    private final int NOTIFICATION_ID           = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recruit);

        contentId       = getIntent().getStringExtra("contentId");
        chatRoom        = getIntent().getStringExtra("contentId");
        recruitBoard    = (RecruitBoard) getIntent().getSerializableExtra("recruit");
        nowMember       = getIntent().getIntExtra("nowMember", 1);
        totalMember     = getIntent().getIntExtra("totalMember", 4);

        viewTitle           = findViewById(R.id.view_recruit_title);
        viewCreate          = findViewById(R.id.view_recruit_create);
        viewWriter          = findViewById(R.id.view_recruit_writer);
        viewAddress         = findViewById(R.id.view_recruit_address);
        viewDate            = findViewById(R.id.view_recruit_date);
        viewContent         = findViewById(R.id.view_recruit_content);
        viewNowMember       = findViewById(R.id.view_recruit_now_member);
        viewTotalMember     = findViewById(R.id.view_recruit_total_member);
        viewDeadLine        = findViewById(R.id.view_recruit_deadline);
        viewApplyBtn        = findViewById(R.id.view_recruit_apply);
        viewEnterChatBtn    = findViewById(R.id.view_recruit_enter_chat);
        viewModifyBtn       = findViewById(R.id.view_recruit_modify);
        viewDeleteBtn       = findViewById(R.id.view_recruit_delete);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // 제목
        viewTitle.setText(recruitBoard.getTitleRecruit());

        // 작성일
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createText           = timeFormat.format(new Date(recruitBoard.getCreateRecruit()));
        viewCreate.setText(createText);

        // 작성자
        viewWriter.setText(recruitBoard.getNameRecruit());

        // 내용
        viewContent.setText(recruitBoard.getContentRecruit());

        // 현재 인원수 / 전체 인원수
        viewNowMember.setText(String.valueOf(recruitBoard.getNowMeetingNumber()));
        viewTotalMember.setText(String.valueOf(recruitBoard.getTotalMeetingNumber()));

        // 마감 여부
        if (!recruitBoard.isDeadlineCheck()) {
            // 마감
            viewDeadLine.setText("마감");
        } else {
            // 마감x
            viewDeadLine.setText("모집 중");
        }

        // 푸시알림 시스템 권한 요청
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (user != null) {
            userId = user.getUid();
            databaseReference.child("users").child(userId).child("userName")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                // 성공
                                Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                                userName = Objects.requireNonNull(task.getResult().getValue()).toString();


                                // 푸시알림 객체 생성
                                builder = new Notification.Builder(getApplicationContext());
                                Intent pushIntent = new Intent(ViewRecruitActivity.this.getApplicationContext(), ViewRecruitActivity.class);
                                // THIS.최상단 | 최상단 액티비티 제외한 모든 액티비티 삭제
                                pushIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                if (recruitBoard.getUserIdRecruit().equals(userId)) {
                                    // 작성자와 동일한 유저일 경우
                                    viewEnterChatBtn.setVisibility(View.VISIBLE);
                                    viewModifyBtn.setVisibility(View.VISIBLE);
                                    viewDeleteBtn.setVisibility(View.VISIBLE);

                                    viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);  // chat 으로 이동
                                    viewModifyBtn.setOnClickListener(ViewRecruitActivity.this);
                                    viewDeleteBtn.setOnClickListener(ViewRecruitActivity.this);
                                } else {
                                    // 가입 안했다면 신청 버튼 보이기
                                    viewApplyBtn.setVisibility(View.VISIBLE);
                                    viewApplyBtn.setOnClickListener(ViewRecruitActivity.this);
                                    createNotificationChannel();

                                    // 가입했다면 입장 버튼 보이기

                                }
                            }
                        }
                    });
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        Intent intent;
        switch (btn.getId()) {
            case R.id.view_recruit_apply:
                Toast.makeText(this, "신청 버튼", Toast.LENGTH_SHORT).show();
                sendNotification();
                break;
            case R.id.view_recruit_enter_chat:
                Toast.makeText(this, "입장 버튼", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                intent.putExtra("contentId", contentId);
                intent.putExtra("recruitUserId", recruitBoard.getUserIdRecruit());
                startActivity(intent);
                break;
            case R.id.view_recruit_modify:
                if (recruitBoard.getNowMeetingNumber() != 1) {
                    Toast.makeText(this, "가입한 모집원이 있을때는 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "수정 버튼", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), AddRecruitActivity.class);
                    intent.putExtra("contentId", contentId);
                    startActivity(intent);
                }
                break;
            case R.id.view_recruit_delete:
                // collection 삭제는 실력 부족
                firebaseFirestore.collection("recruitment").document(contentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ViewRecruitActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                Log.d("ViewRecruitActivity", "contentId = " + contentId);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewRecruitActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                Log.d("ViewRecruitActivity", "contentId = " + contentId);
                            }
                        });
                break;
        }
    }

    // 푸시알림 채널설정
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "TEST",
                    notificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription(userName + "님이 가입 신청을 했습니다.");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    // 푸시알림 builder 생성
    private NotificationCompat.Builder getNotificationBuilder() {
        // 사용자정보화면으로 보내고 싶은데 로그인 유지 기능이 없다.
        Intent intent = new Intent(this, loginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("test")
                .setContentText("누군가가 가입 신청을 했습니다.")
                .setSmallIcon(R.drawable.people)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder;
    }

    // 푸시알림 보내기
    public void sendNotification() {
        NotificationCompat.Builder builder = getNotificationBuilder();
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}