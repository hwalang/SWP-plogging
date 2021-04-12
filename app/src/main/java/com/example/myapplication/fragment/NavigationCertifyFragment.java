package com.example.myapplication.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.AddCertificationActivity;
import com.example.myapplication.BottomNavigation;
import com.example.myapplication.R;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Objects;

import adapter.CertificationAdapter;

public class NavigationCertifyFragment extends Fragment implements View.OnClickListener {
    DocumentReference documentReference;
    private static final String TAG = "NavigationCertifyFragment";

    RecyclerView recyclerview;
    CertificationAdapter certificationAdapter;
    FloatingActionButton rank_button;
    FloatingActionButton write_certification_button;

    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_main_certify, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        final ArrayList<CertificationBoard> certificationBoards = new ArrayList<>();


        // 랭킹페이지로 가는 버튼
        rank_button = rootView.findViewById(R.id.rank_button);
        rank_button.setOnClickListener(this);

        // 파이어스토어에서 글 가져오기
        firebaseFirestore.collection("certification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(getContext(), "반복 불러오기", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                certificationBoards.add(new CertificationBoard(
                                        document.getData().get("boardTitle").toString(),
                                        document.getData().get("boardContent").toString(),
                                        document.getData().get("name").toString(),
                                        (Long) document.getData().get("boardCreate"),
                                        document.getData().get("certifyPhoto").toString()
                                ));
                                recyclerview = rootView.findViewById(R.id.certifyitemfragment_recyclerview);
                                recyclerview.setHasFixedSize(true);
                                layoutManager = new LinearLayoutManager(getActivity());
                                recyclerview.setLayoutManager(layoutManager);

                                certificationAdapter = new CertificationAdapter(getActivity(), certificationBoards);
                                recyclerview.setAdapter(certificationAdapter);
                            }
                        } else {
                            Log.d(TAG, "error getting documents", task.getException());
                        }
                    }
                });

//        certifyViewRecyclerViewAdapter.notifyDataSetChanged(); // 새로고침

        // 글 작성 페이지로 가는 버튼
        write_certification_button = rootView.findViewById(R.id.write_certification_button);
        write_certification_button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        FloatingActionButton button = (FloatingActionButton) v;

        switch (button.getId()) {
            case R.id.rank_button:
                //버튼 클릭시 아래 구현이 실행된다.
                Toast.makeText(getContext(), "랭킹 버튼 클릭", Toast.LENGTH_SHORT).show();
//                getActivity().startActivity(new Intent(getActivity(), Ranking.class));
                break;
            case R.id.write_certification_button:
                Toast.makeText(getContext(), "인증글 작성 버튼 클릭", Toast.LENGTH_SHORT).show();
                // 갤러리 권한
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "권한 허용됨", Toast.LENGTH_SHORT).show();
                    getActivity().startActivity(new Intent(getActivity(), AddCertificationActivity.class));
                } else {
                    // 허용 요청: 허용을 하지 않으면 네비게이션바로 못가는 기능도 추가해야함!
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    Toast.makeText(getContext(), "권한을 허용해야 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
