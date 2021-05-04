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

import com.example.myapplication.BottomNavigation;
import com.example.myapplication.R;
import com.example.myapplication.maps.MapsActivityCurrentPlace;
import com.example.myapplication.profileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavigationPloggingFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton maps;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_main_plogging, container, false);
        maps = rootView.findViewById(R.id.maps);
        maps.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        FloatingActionButton button = (FloatingActionButton) v;

        switch (button.getId()) {
            case R.id.maps:
                Intent intent = new Intent(getActivity(), MapsActivityCurrentPlace.class);

                startActivity(intent);
        }
    }
}
