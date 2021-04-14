package adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.CertificationActivity;
import com.example.myapplication.R;
import com.example.myapplication.schema.CertificationBoard;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.CertifyViewHolder> {
    private ArrayList<CertificationBoard> certificationBoards;
    private Activity activity;

    public static class CertifyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public CertifyViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CertificationAdapter(Activity activity, ArrayList<CertificationBoard> certificationBoards) {
        this.certificationBoards = certificationBoards;
        this.activity = activity;
        // 유저 list 도 지정

    }

    @NonNull
    @Override
    public CertificationAdapter.CertifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_certification, parent, false);


        // 좋아요 이미지를 누르면 좋아요 수 증가
        cardView.findViewById(R.id.certifyitem_favorite_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "좋아요 수 증가", Toast.LENGTH_SHORT).show();
                Log.d("CertificationBoard", "getItemCount = "+ getItemCount());
//                Log.d("Recyclerview", "position = "+ certifyViewHolder.getAdapterPosition());
            }
        });
        // 댓글 누르면 댓글창으로 이동
        cardView.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "댓글창으로 이동", Toast.LENGTH_SHORT).show();
            }
        });
        return new CertifyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CertifyViewHolder holder, int position) {
        // position: 해당 글 인덱스
        CardView cardView = holder.cardView;

        // 인증글 보기로 이동
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "인증글 보기로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity, CertificationActivity.class);
                intent.putExtra("certificationBoard", (Serializable) certificationBoards.get(position));
                activity.startActivity(intent);
            }
        });

        // 제목
        TextView certifyitem_title = (TextView) cardView.findViewById(R.id.certifyitem_title);
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

        // 좋아요 수
        TextView certifyitem_favorite_count = cardView.findViewById(R.id.certifyitem_favorite_count);
        certifyitem_favorite_count.setText(String.valueOf(certificationBoards.get(position).getCertifyFeel()));
    }

    @Override
    public int getItemCount() {
        return certificationBoards.size();
    }
}
