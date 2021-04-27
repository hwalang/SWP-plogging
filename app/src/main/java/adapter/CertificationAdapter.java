package adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.CommentActivity;
import com.example.myapplication.R;
import com.example.myapplication.ViewCertificationActivity;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static java.util.Objects.requireNonNull;

public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.CertifyViewHolder> {
    private final ArrayList<CertificationBoard> certificationBoards;
    private final Activity activity;
    private final ArrayList<String> contentIdList;
    FirebaseFirestore firebaseFirestore;

    public static class CertifyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public CertifyViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CertificationAdapter(Activity activity, ArrayList<CertificationBoard> certificationBoards, ArrayList<String> contentIdList) {
        this.certificationBoards = certificationBoards;
        this.contentIdList = contentIdList;
        this.activity = activity;
        // 유저 list 도 지정

        firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("certification").document();
        documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                firebaseFirestore.collection("certification")
                        .orderBy("boardCreate", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    certificationBoards.clear();
                                    contentIdList.clear();
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Log.d(TAG, document.getId() + "=> " + document.getData());
                                        certificationBoards.add(new CertificationBoard(
                                                document.getData().get("boardTitle").toString(),
                                                document.getData().get("boardContent").toString(),
                                                document.getData().get("name").toString(),
                                                (Long) document.getData().get("boardCreate"),
                                                document.getData().get("certifyPhoto").toString()
                                        ));

                                        Log.d("documentReference", "getId = " + document.getId());
                                        contentIdList.add(document.getId());
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
    public CertificationAdapter.CertifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_certification, parent, false);

        return new CertifyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CertifyViewHolder holder, int position) {
        // position: 해당 글 인덱스
        CardView cardView = holder.cardView;

        // 제목
        TextView certifyitem_title = cardView.findViewById(R.id.certifyitem_title);
        certifyitem_title.setText(certificationBoards.get(position).getBoardTitle());

        // 작성일
        TextView certifyitem_created = cardView.findViewById(R.id.certifyitem_created);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String created = timeFormat.format(new Date(certificationBoards.get(position).getBoardCreate()));
        certifyitem_created.setText(created);

        // 작성자
        TextView certifyitem_user = cardView.findViewById(R.id.certifyitem_user);
        certifyitem_user.setText(certificationBoards.get(position).getName());

        // Image
        Glide.with(cardView.getContext()).load(certificationBoards.get(position).getCertifyPhoto()).into(
                (ImageView) cardView.findViewById(R.id.certifyitem_imageview_content)
        );

        // 내용
        TextView certifyitem_content_textview = cardView.findViewById(R.id.certifyitem_content_textview);
        certifyitem_content_textview.setText(certificationBoards.get(position).getBoardContent());

        // 상세보기
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "인증글 상세보기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, ViewCertificationActivity.class);
                intent.putExtra("certification", certificationBoards.get(position));
                intent.putExtra("contentId", contentIdList.get(position));
                activity.startActivity(intent);
            }
        });

        // 댓글
        FloatingActionButton comment = cardView.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "댓글 버튼 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, CommentActivity.class);
                Log.d("documentId", "documentId = " + contentIdList.get(position));
                intent.putExtra("contentId", contentIdList.get(position));
                activity.startActivity(intent);
            }
        });

        // 좋아요 수
        TextView certifyitem_favorite_count = cardView.findViewById(R.id.certifyitem_favorite_count);
        certifyitem_favorite_count.setText(String.valueOf(certificationBoards.get(position).getCertifyFeel()));

        // 좋아요 이미지를 누르면 좋아요 수 증가
        cardView.findViewById(R.id.certifyitem_favorite_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "좋아요 수 증가", Toast.LENGTH_SHORT).show();
                Log.d("CertificationBoard", "getItemCount = " + getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return certificationBoards.size();
    }
}
