package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MountineActivity;
import com.example.myapplication.ParkActivity;
import com.example.myapplication.R;
import com.example.myapplication.WalkActivity;

public class NavigationPloggingFragment extends Fragment {
    Button walk,park,moun;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_main_plogging, container, false);

        walk = view.findViewById(R.id.walk);
        walk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), ParkActivity.class);
                startActivity(intent);
            }
        });
        park = view.findViewById(R.id.park);
        park.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), ParkActivity.class);
                startActivity(intent);
            }
        });
        moun = view.findViewById(R.id.mountine);
        moun.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), MountineActivity.class);
                startActivity(intent);
            }
        });

    return view;
    }

}


