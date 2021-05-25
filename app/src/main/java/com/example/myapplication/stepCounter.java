package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class stepCounter extends AppCompatActivity implements SensorEventListener {

    FirebaseUser user;
    String userId = null;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference stepData;

    private TextView textView;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
//    private Sensor mStepDetectorSensor;
    private int mSteps = 0;
    private int mCounterSteps = 0;
    private int storedSteps;
    int value = -1; //Step Counter

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcounter);

        user =FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        stepData = databaseReference.child("users").child(userId).child("steps");

        stepData.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    storedSteps = Integer.parseInt(task.getResult().getValue().toString());
                    Log.d("firebase", String.valueOf(task.getResult().getValue() + " " + storedSteps));


                }
            }
        });



        textView = findViewById(R.id.steps);
        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStepCounterSensor = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }
//        mStepDetectorSensor = mSensorManager
//                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        Button button = findViewById(R.id.Reset);
        button.setOnClickListener(view -> {
            storedSteps = storedSteps + mSteps;
            Log.d("firebase", "steps = " + storedSteps);
            stepData.setValue(storedSteps);
            mSteps = 0;
            mCounterSteps = 0;
            textView.setText(Integer.toString(mSteps));
        });
    }

    public void onAccuracyChanged(Sensor sensor, int a){}

    @SuppressLint("SetTextI18n")
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;


        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (mCounterSteps < 1) {
                mCounterSteps = (int) event.values[0];
            }
            mSteps = (int)event.values[0] - mCounterSteps;
            textView.setText(Integer.toString(mSteps));
            Log.d("TAG", "stepcounter is added");
        } else {
            Log.d("TAG", "No Sensor");
        }

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mStepCounterSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
//        mSensorManager.registerListener(this, mStepDetectorSensor,
//                SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
//        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(stepCounter.this, profileActivity.class);
        startActivity(intent);
        finish();
    }

}
