package com.maaateusz.service_background;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startBtn;
    private TextView textTextView;
    //private boolean isServiceOn;
    //private Handler handler = new Handler();
    private int i=0;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        startBtn = findViewById(R.id.startBtn);
        textTextView = findViewById(R.id.textTextView);

        if(getServiceStatus()) startBtn.setText("End Service");
        else startBtn.setText("Start Service");

        addListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void addListeners() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getServiceStatus()){
                    startBtn.setText("End Service");
                    setServiceStatus(true);
                    Log.d(TAG, "handler.postDelayed: RUN");
                    Log.d(TAG, "Service: " + getServiceStatus());
                    //handler.postDelayed(runnable, 1000);

                    //MainActivity.this.startService(new Intent(MainActivity.this,  MyService.class));
                    //startService(new Intent(MainActivity.this,  MyService.class));

                    //ContextCompat.startForegroundService(getApplicationContext(), new Intent(MainActivity.this,  MyService.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        MainActivity.this.startForegroundService(new Intent(MainActivity.this,  MyService.class));
                        //startForegroundService(new Intent(MainActivity.this,  MyService.class));
                    } else {
                        MainActivity.this.startService(new Intent(MainActivity.this,  MyService.class));
                        //startService(new Intent(MainActivity.this,  MyService.class));
                    }
                } else {
                    startBtn.setText("Start Service");
                    setServiceStatus(false);
                    Log.d(TAG, "handler.postDelayed: REMOVE");
                    Log.d(TAG, "Service: " + getServiceStatus());
                    //handler.removeCallbacks(runnable);
                    stopService(new Intent(MainActivity.this,  MyService.class));
                }
            }
        });
    }

    public boolean getServiceStatus(){
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isServiceOn", false);
    }

    public void setServiceStatus(boolean status){
        SharedPreferences sharedPreferences =  this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isServiceOn", status);
        editor.commit();
    }

//    final Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            i++;
//            textTextView.setText(""+ i);
//            Log.d(TAG, "handler.postDelayed: " + i);
//            handler.postDelayed(this, 1000);
//        }
//    };

}