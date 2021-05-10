package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AddRecruitActivity;
import com.example.myapplication.R;
import com.example.myapplication.schema.RecruitBoard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import adapter.RecruitAdapter;

public class NavigationRecruitFragment extends Fragment implements View.OnClickListener {
    RecyclerView                    recyclerView;
    FloatingActionButton            floatingActionButton;

    RecyclerView.LayoutManager      layoutManager;
    FirebaseFirestore               firebaseFirestore = null;
    RecruitAdapter                  recruitAdapter;

    ArrayList<String>               contentIdList;
    ArrayList<RecruitBoard>         recruitBoards;
    ArrayList<Integer>              nowMemberList;
    ArrayList<Integer>              totalMemberList;

    FirebaseUser                    user;
    String                          userId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView     = inflater.inflate(R.layout.fragment_bottom_main_recruit, container, false);
        recruitBoards     = new ArrayList<>();
        contentIdList     = new ArrayList<>();
        totalMemberList   = new ArrayList<>();
        nowMemberList     = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user              = FirebaseAuth.getInstance().getCurrentUser();

        // 계정 작업
        if (user != null) {
            userId = user.getUid();
            Toast.makeText(getActivity(), "계정 확인", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "현재 계정이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        recyclerView = rootView.findViewById(R.id.recruit_recyclerview);
        recyclerView .setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        floatingActionButton = rootView.findViewById(R.id.write_recruit_button);
        floatingActionButton.setOnClickListener(this);
        return rootView;
    }
    public void onResume() {
        super.onResume();
        recruitAdapter = new RecruitAdapter(this, recruitBoards, contentIdList, totalMemberList, nowMemberList);
        recyclerView.setAdapter(recruitAdapter);
    }


    @Override
    public void onClick(View v) {
        FloatingActionButton button = (FloatingActionButton) v;
        if (button.getId() == R.id.write_recruit_button) {
            Toast.makeText(getContext(), "모집글 작성", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AddRecruitActivity.class);
            startActivity(intent);
        }
    }
}
