package adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.myapplication.R;
import com.example.myapplication.ViewRecruitActivity;
import com.example.myapplication.fragment.NavigationRecruitFragment;
import com.example.myapplication.schema.RecruitBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final ArrayList<RecruitBoard.Chat> chats;
    private final Activity activity;
    private final String contentId;
    private String recruitUserId;
    private String userId;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ChatViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public ChatAdapter(Activity activity, ArrayList<RecruitBoard.Chat> chats,
                       String contentId, String recruitUserId) {
        this.activity = activity;
        this.chats = chats;
        this.contentId = contentId;
        this.recruitUserId = recruitUserId;

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("recruitment").document(contentId);
        documentReference.collection("chats").document()
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                        }

                        firebaseFirestore.collection("recruitment").document(contentId)
                                .collection("chats")
                                .orderBy("chatCreate", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            chats.clear();
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                Log.d(TAG, document.getId() + "=> " + document.getData());

                                                chats.add(new RecruitBoard.Chat(
                                                        Objects.requireNonNull(document.getData().get("chatRoom")).toString(),
                                                        Objects.requireNonNull(document.getData().get("userChatId")).toString(),
                                                        Objects.requireNonNull(document.getData().get("profileUrl")).toString(),
                                                        Objects.requireNonNull(document.getData().get("chatName")).toString(),
                                                        Objects.requireNonNull(document.getData().get("message")).toString(),
                                                        (Long) document.getData().get("chatCreate")
                                                ));
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
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);


        return new ChatViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder holder, int position) {
        // position: 해당 글 인덱스
        CardView cardView = holder.cardView;

        if (user != null) {
            userId = user.getUid();
            if (userId.equals(recruitUserId)) {
                cardView.findViewById(R.id.recruit_cardView).setBackgroundColor(Color.parseColor("#31ED7C"));
            }
        }

        // 프로필
        Glide.with(cardView.getContext()).load(chats.get(position).getProfileUrl()).into(
                (ImageView) cardView.findViewById(R.id.item_chat_profile)
        );

        // 작성자
        TextView userName = cardView.findViewById(R.id.item_chat_username);
        userName.setText(chats.get(position).getChatName());

        // 작성일
        TextView create = cardView.findViewById(R.id.item_chat_create);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String created = timeFormat.format(new Date(chats.get(position).getChatCreate()));
        create.setText(created);

        // 메시지
        TextView chat = cardView.findViewById(R.id.item_chat);
        chat.setText(chats.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
