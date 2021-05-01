package adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final ArrayList<CertificationBoard.Comment> comments;
    private final Activity activity;
    private final String contentId;
    FirebaseFirestore firebaseFirestore;

    public CommentAdapter(Activity activity, ArrayList<CertificationBoard.Comment> comments, String contentId) {
        this.activity = activity;
        this.comments = comments;
        this.contentId = contentId;

        firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("certification").document(contentId);
        documentReference.collection("comments").document()
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                        }
                        firebaseFirestore.collection("certification").document(contentId).collection("comments")
                                .orderBy("commentCreate")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            comments.clear();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                comments.add(new CertificationBoard.Comment(
                                                        document.getData().get("userId").toString(),
                                                        document.getData().get("profileUrl").toString(),
                                                        document.getData().get("name").toString(),
                                                        document.getData().get("comment").toString(),
                                                        (Long) document.getData().get("commentCreate")
                                                ));
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                        notifyDataSetChanged();
                                    }
                                });
                        if (value != null && value.exists()) {
                            Log.d(TAG, "Current data: " + value.getData());
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(cardView);
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public CommentViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        // 작성자 프로필
        Glide.with(cardView.getContext()).load(comments.get(position).getProfileUrl()).into(
                (ImageView) cardView.findViewById(R.id.item_comment_profile)
        );

        // 작성자 이름
        TextView username = cardView.findViewById(R.id.item_comment_username);
        username.setText(comments.get(position).getName());

        // 작성일
        TextView create = cardView.findViewById(R.id.item_comment_create);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String created = timeFormat.format(new Date(comments.get(position).getCommentCreate()));
        create.setText(created);

        // 댓글 내용
        TextView comment = cardView.findViewById(R.id.item_comment);
        comment.setText(comments.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public ArrayList<CertificationBoard.Comment> getData() {
        return comments;
    }
}
