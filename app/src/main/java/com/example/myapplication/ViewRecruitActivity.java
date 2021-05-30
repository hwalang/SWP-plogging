package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.fragment.NavigationRecruitFragment;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import adapter.RecruitAdapter;

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
    private final static String TAG             = "ViewRecruitActivity";
    boolean isChatUserId2                       = false;
    boolean isChatUserId3                       = false;
    boolean isChatUserId4                       = false;

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

        user                = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore   = FirebaseFirestore.getInstance();
        databaseReference   = FirebaseDatabase.getInstance().getReference();

        // 제목
        viewTitle.setText(recruitBoard.getTitleRecruit());

        // 작성일
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createText           = timeFormat.format(new Date(recruitBoard.getCreateRecruit()));
        viewCreate.setText(createText);

        // 약속 날짜
        String month            = recruitBoard.getRecruitMonth();
        String day              = recruitBoard.getRecruitDay();
        String appointmentDay   = "약속 날짜: " + month + "월 " + day + "일";
        viewDate.setText(appointmentDay);

        // 작성자
        viewWriter.setText(recruitBoard.getNameRecruit());

        // 내용
        viewContent.setText(recruitBoard.getContentRecruit());

        // 현재 인원수 / 전체 인원수
        viewNowMember.setText(String.valueOf(recruitBoard.getNowMeetingNumber()));
        viewTotalMember.setText(String.valueOf(recruitBoard.getTotalMeetingNumber()));

        // 마감 여부
        if (nowMember == totalMember) {
            // 마감
            viewDeadLine.setText("마감");
        } else {
            // 마감x
            viewDeadLine.setText("모집중");
        }

        // 푸시알림 시스템 권한 요청
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (user != null) {
            userId = user.getUid();
            // 작성자 주소
            databaseReference.child("users").child(userId).child("address")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                // 성공
                                Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                                String address = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());

                                viewAddress.setText(address);
                            }
                        }
                    });

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

                                if (recruitBoard.getChatUserId1().equals(userId)) {
                                    // 작성자와 동일한 유저일 경우
                                    viewEnterChatBtn.setVisibility(View.VISIBLE);
                                    viewModifyBtn.setVisibility(View.VISIBLE);
                                    viewDeleteBtn.setVisibility(View.VISIBLE);

                                    viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);  // chat 으로 이동
                                    viewModifyBtn.setOnClickListener(ViewRecruitActivity.this);
                                    viewDeleteBtn.setOnClickListener(ViewRecruitActivity.this);
                                } else {
                                    // 작성자와 동일 유저가 아닐 경우
                                    // 실력부족: 신청버튼을 누르면 자동으로 가입 -> 선착순..
                                    if (nowMember < totalMember) {
                                        // 가입 인원을 다 채우지 않은 모집글인 경우
                                        if (nowMember == 1) {
                                            // 가입 인원이 1명
                                            isChatUserId2 = true;
                                            viewApplyBtn.setVisibility(View.VISIBLE);
                                            viewApplyBtn.setOnClickListener(ViewRecruitActivity.this);
                                            createNotificationChannel();
                                        } else if (nowMember == 2) {
                                            // 가입 인원이 2명
                                            if (!recruitBoard.getChatUserId2().equals(userId)) {
                                                // 가입 인원에 현재 사용자가 없을때
                                                isChatUserId3 = true;
                                                viewApplyBtn.setVisibility(View.VISIBLE);
                                                viewApplyBtn.setOnClickListener(ViewRecruitActivity.this);
                                                createNotificationChannel();
                                            } else {
                                                viewApplyBtn.setVisibility(View.INVISIBLE);
                                                viewEnterChatBtn.setVisibility(View.VISIBLE);
                                                viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                            }
                                        } else if (nowMember == 3) {
                                            // 가입 인원이 3명
                                            if (!recruitBoard.getChatUserId2().equals(userId)) {
                                                if (!recruitBoard.getChatUserId3().equals(userId)) {
                                                    isChatUserId4 = true;
                                                    viewApplyBtn.setVisibility(View.VISIBLE);
                                                    viewApplyBtn.setOnClickListener(ViewRecruitActivity.this);
                                                    createNotificationChannel();
                                                } else {
                                                    viewApplyBtn.setVisibility(View.INVISIBLE);
                                                    viewEnterChatBtn.setVisibility(View.VISIBLE);
                                                    viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                                }
                                            } else {
                                                viewApplyBtn.setVisibility(View.INVISIBLE);
                                                viewEnterChatBtn.setVisibility(View.VISIBLE);
                                                viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                            }
                                        } else {
                                            // 최대 가입자 수와 일치했을때
                                            // 파이어스토어에 저장된 id와 일치하면
                                            if (recruitBoard.getChatUserId2().equals(userId)) {
                                                viewEnterChatBtn.setVisibility(View.VISIBLE);
                                                viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                            } else if (recruitBoard.getChatUserId3().equals(userId)) {
                                                viewEnterChatBtn.setVisibility(View.VISIBLE);
                                                viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                            } else if (recruitBoard.getChatUserId4().equals(userId)) {
                                                viewEnterChatBtn.setVisibility(View.VISIBLE);
                                                viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                            }
                                        }
                                    } else if (recruitBoard.getChatUserId2().equals(userId)) {
                                        viewEnterChatBtn.setVisibility(View.VISIBLE);
                                        viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                    } else if (recruitBoard.getChatUserId3().equals(userId)) {
                                        viewEnterChatBtn.setVisibility(View.VISIBLE);
                                        viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                    } else if (recruitBoard.getChatUserId4().equals(userId)) {
                                        viewEnterChatBtn.setVisibility(View.VISIBLE);
                                        viewEnterChatBtn.setOnClickListener(ViewRecruitActivity.this);
                                    } else {
                                        Toast.makeText(ViewRecruitActivity.this, "더 이상 가입할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public void onResume() {
        super.onResume();
    }

    public String getArrayList(ArrayList<String> arrayList) {
        if (arrayList.size() != 0) {
            for (String e : arrayList) {
                return e;
            }
        }
        return "없다";
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        Intent intent;
        switch (btn.getId()) {
            case R.id.view_recruit_apply:
                sendNotification();
                nowMember++;

                updateFireStore("nowMember", nowMember);
                recruitBoard.setNowMeetingNumber(nowMember);
                if (isChatUserId2 == true) {
                    updateFireStore("chatUserId2", userId);
                    recruitBoard.setChatUserId2(userId);
                } else if (isChatUserId3 == true) {
                    updateFireStore("chatUserId3", userId);
                    recruitBoard.setChatUserId3(userId);
                } else if (isChatUserId4 == true) {
                    updateFireStore("chatUserId4", userId);
                    recruitBoard.setChatUserId4(userId);
                }
                Toast.makeText(ViewRecruitActivity.this, "새로고침을 위해 다시 들어와주세요", Toast.LENGTH_LONG).show();

                viewEnterChatBtn.setVisibility(View.VISIBLE);
                viewApplyBtn.setVisibility(View.INVISIBLE);

                // 현재 인원수 / 전체 인원수
                viewNowMember.setText(String.valueOf(recruitBoard.getNowMeetingNumber()));
                viewTotalMember.setText(String.valueOf(recruitBoard.getTotalMeetingNumber()));

                // 마감 여부
                if (nowMember == totalMember) {
                    // 마감
                    viewDeadLine.setText("마감");
                } else {
                    // 마감x
                    viewDeadLine.setText("모집중");
                }
                onResume();
                break;
            case R.id.view_recruit_enter_chat:
                // 알림 때문에 입장 버튼이 먹통인가??
                intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                intent.putExtra("contentId", contentId);
                startActivity(intent);
                break;
            case R.id.view_recruit_modify:
                if (recruitBoard.getNowMeetingNumber() != 1) {
                    Toast.makeText(this, "가입한 모집원이 있을때는 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
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
                                Toast.makeText(ViewRecruitActivity.this, "모집글을 삭제했습니다", Toast.LENGTH_SHORT).show();
                                Log.d("ViewRecruitActivity", "contentId = " + contentId);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewRecruitActivity.this, "모집글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show();
                                Log.d("ViewRecruitActivity", "contentId = " + contentId);
                            }
                        });
                break;
        }
    }

    // 푸시알림 채널설정
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 가입 알림 채널
            notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "가입 알림",
                    notificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription(userName + "님은 해당 모임에 가입했습니다.");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    // 푸시알림 builder 생성
    private NotificationCompat.Builder getNotificationBuilder() {
        Intent intent = new Intent(this, profileActivity.class);

        // 백 스택을 포함하는 활동: 현재 로그인 정보를 유지하네
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        // 앱 사용하는 동안 알림 클릭 -> loginActivity 로 이동(뒤로가기 누르면 해당 액티비티로 이동)
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
//        );
        PendingIntent pendingIntent1 = stackBuilder.getPendingIntent(
                NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("가입 알림")
                .setContentText(userName + "님은 해당 모임에 가입 했습니다.")
                .setSmallIcon(R.drawable.people)
                .setContentIntent(pendingIntent1)
                .setAutoCancel(true);

        return builder;
    }

    // 푸시알림 보내기
    public void sendNotification() {
        NotificationCompat.Builder builder = getNotificationBuilder();
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // nowMember += 1
    public void updateFireStore(String field, int updateValue) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("recruitment").document(contentId);
        documentReference
                .update(field, updateValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    // nowMember == totalMember: true
    public void updateFireStore(String field, boolean updateValue) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("recruitment").document(contentId);
        documentReference
                .update(field, updateValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    // chatUserId2,3,4에 userId
    public void updateFireStore(String field, String updateValue) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("recruitment").document(contentId);
        documentReference
                .update(field, updateValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

}