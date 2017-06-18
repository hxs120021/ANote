package bzu.edu.cn.anote;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import bzu.edu.cn.anote.hatsuhikari.MainActivity2;

public class Start extends AppCompatActivity {
    private int index = 5;
    private final int yanchi = 5000;
    Timer timer = new Timer();
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        textView = (TextView)findViewById(R.id.sheng_time);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    textView.setText(String.valueOf(index));
                    if (index < 0)
                        timer.cancel();
                    index--;
                });
            }
        }, 1000, 1000);

        new Handler().postDelayed(()->{
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo == null || !networkInfo.isAvailable())
                Toast.makeText(Start.this, "没网", Toast.LENGTH_SHORT).show();
            else Toast.makeText(Start.this, "有网", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Start.this, MainActivity2.class);
            Start.this.startActivity(intent);
            Start.this.finish();
        }, yanchi);
    }
}
