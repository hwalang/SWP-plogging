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
import com.example.myapplication.CommentActivity;
import com.example.myapplication.R;
import com.example.myapplication.ViewCertificationActivity;
import com.example.myapplication.schema.CertificationBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static android.app.Activity.RESULT_OK;

public class NavigationCertifyFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NavigationCertifyFragment";

    RecyclerView recyclerview;
    FloatingActionButton write_certification_button;

    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore = null;
    CertificationAdapter certificationAdapter;
    ArrayList<CertificationBoard> certificationBoards;
    ArrayList<String> contentIdList;

    FirebaseUser user;
    String userId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_main_certify, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        certificationBoards = new ArrayList<>();
        contentIdList = new ArrayList<>();

        // 계정작업
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            Toast.makeText(getActivity(), "계정 확인", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "현재 계정이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        recyclerview = rootView.findViewById(R.id.certifyitemfragment_recyclerview);

        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        // 글 작성 페이지로 가는 버튼
        write_certification_button = rootView.findViewById(R.id.write_certification_button);
        write_certification_button.setOnClickListener(this);

        return rootView;
    }
    public void onResume() {
        super.onResume();
        certificationAdapter = new CertificationAdapter(getActivity(), certificationBoards, contentIdList);
        recyclerview.setAdapter(certificationAdapter);
    }

    /* 버튼 이벤트
    * 인증글 작성
    * 인증글 수정
    * 인증글 삭제
    * */
    @Override
    public void onClick(View v) {
        FloatingActionButton button = (FloatingActionButton) v;

        switch (button.getId()) {
            case R.id.write_certification_button:
                Toast.makeText(getContext(), "인증글 작성 버튼 클릭", Toast.LENGTH_SHORT).show();
                // 갤러리 권한
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "권한 허용됨", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddCertificationActivity.class);
                    startActivity(intent);
                } else {
                    // 허용 요청
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    Toast.makeText(getContext(), "권한을 허용해야 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
