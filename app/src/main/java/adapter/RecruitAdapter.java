package adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ViewCertificationActivity;
import com.example.myapplication.ViewRecruitActivity;
import com.example.myapplication.fragment.NavigationRecruitFragment;
import com.example.myapplication.schema.RecruitBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class RecruitAdapter extends RecyclerView.Adapter<RecruitAdapter.RecruitViewHolder> {
    private final ArrayList<RecruitBoard> recruitBoards;
    private final NavigationRecruitFragment activity;
    private final ArrayList<String> contentIdList;
    private final ArrayList<Integer> totalMeetingNumberList;
    private final ArrayList<Integer> nowMeetingNumberList;
    FirebaseFirestore firebaseFirestore;

    public static class RecruitViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public RecruitViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public RecruitAdapter(NavigationRecruitFragment activity, ArrayList<RecruitBoard> recruitBoards, ArrayList<String> contentIdList,
                          ArrayList<Integer> totalMeetingNumberList, ArrayList<Integer> nowMeetingNumberList) {
        this.activity = activity;
        this.recruitBoards = recruitBoards;
        this.contentIdList = contentIdList;
        this.totalMeetingNumberList = totalMeetingNumberList;       // userId => 채팅방 인원수에 사용 || 현재: 총 모집 인원수
        this.nowMeetingNumberList = nowMeetingNumberList;   // 현재 모집원 수

        firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("recruitment").document();
        documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                firebaseFirestore.collection("recruitment")
                        .orderBy("recruitCreate", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    recruitBoards.clear();
                                    contentIdList.clear();
                                    totalMeetingNumberList.clear();
                                    nowMeetingNumberList.clear();
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Log.d(TAG, document.getId() + "=> " + document.getData());
                                        int totalMeetingNumber = Integer.parseInt(Objects.requireNonNull(document.getData().get("totalMember")).toString());
                                        int nowMeetingNumber = Integer.parseInt(Objects.requireNonNull(document.getData().get("nowMember")).toString());

                                        recruitBoards.add(new RecruitBoard(
                                                Objects.requireNonNull(document.getData().get("userId")).toString(),
                                                Objects.requireNonNull(document.getData().get("userName")).toString(),
                                                Objects.requireNonNull(document.getData().get("title")).toString(),
                                                Objects.requireNonNull(document.getData().get("content")).toString(),
                                                (Long) document.getData().get("recruitCreate"),
                                                Objects.requireNonNull(document.getData().get("month")).toString(),
                                                Objects.requireNonNull(document.getData().get("day")).toString(),
                                                (Integer) totalMeetingNumber,
                                                (Integer) nowMeetingNumber
                                        ));
                                        contentIdList.add(document.getId());
                                        totalMeetingNumberList.add(totalMeetingNumber);
                                        nowMeetingNumberList.add(nowMeetingNumber);
                                    }
                                } else {
                                    Log.d(TAG, "error getting documents", task.getException());
                                }
                                notifyDataSetChanged();
                            }
                        });

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @NonNull
    @Override
    public RecruitAdapter.RecruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_recruit, parent, false);

        return new RecruitViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecruitViewHolder holder, int position) {
        // position: 해당 글 인덱스
        CardView cardView = holder.cardView;

        // 제목
        TextView recruitTitle = cardView.findViewById(R.id.recruit_card_title);
        recruitTitle.setText(recruitBoards.get(position).getTitleRecruit());

        // 작성일
        TextView recruitCreated = cardView.findViewById(R.id.recruit_card_create);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String created = timeFormat.format(new Date(recruitBoards.get(position).getCreateRecruit()));
        recruitCreated.setText(created);

        // 작성자
        TextView recruitWriter = cardView.findViewById(R.id.recruit_card_writer);
        recruitWriter.setText(recruitBoards.get(position).getNameRecruit());

        // 약속 날짜
        TextView recruitDate = cardView.findViewById(R.id.recruit_card_date);
        String recruitMonth = recruitBoards.get(position).getRecruitMonth();
        String recruitDay = recruitBoards.get(position).getRecruitDay();
        String recruitDateText = "약속 날짜: " + recruitMonth + "월 " + recruitDay + "일";
        recruitDate.setText(recruitDateText);

        // 작성자 주소: 아직 구현안됨
//        TextView recruitAddress = cardView.findViewById(R.id.recruit_card_address);
//        recruitAddress.setText(recruitBoards.get(position).get);

        // 마감여부
        TextView recruitIsDead = cardView.findViewById(R.id.recruit_card_deadline);

        // intent => 모집 중인지 아닌지 알려주는 코드 보내기
        if (!recruitBoards.get(position).isDeadlineCheck()) {
            recruitIsDead.setText("모집중");
        } else {
            recruitIsDead.setText("마감");
        }

        // 현재 인원 / 전체 인원
        TextView recruitMember = cardView.findViewById(R.id.recruit_card_member);
        TextView recruitTotalMember = cardView.findViewById(R.id.recruit_card_total_member);

        recruitMember.setText(String.valueOf(recruitBoards.get(position).getNowMeetingNumber()));
        recruitTotalMember.setText(String.valueOf(recruitBoards.get(position).getTotalMeetingNumber()));

        // 상세보기
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getContext(), "모집글 상세보기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity.getContext(), ViewRecruitActivity.class);
                intent.putExtra("recruit", recruitBoards.get(position));
                intent.putExtra("contentId", contentIdList.get(position));

                intent.putExtra("nowMember", nowMeetingNumberList.get(position));
                intent.putExtra("totalMember", totalMeetingNumberList.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recruitBoards.size();
    }
}
