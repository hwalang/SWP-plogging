package com.example.myapplication.maps;



import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.skt.Tmap.TMapView;

public class Tmap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        TMapView tmapview = new TMapView(this);

        tmapview.setSKTMapApiKey("l7xx7769f2f8072748a889a4fa74153355ff");
        linearLayoutTmap.addView(tmapview);
    }

}
