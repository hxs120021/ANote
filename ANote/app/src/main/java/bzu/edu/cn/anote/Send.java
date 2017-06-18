package bzu.edu.cn.anote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import bzu.edu.cn.anote.hatsuhikari.MainActivity2;
import bzu.edu.cn.anote.tool.ActivityCollector;


public class Send extends AppCompatActivity {

    private TextView happy;
    private TextView unhappy;
    private TextView doubt;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initview();
        setToolbar();
    }
    public void initview(){
        happy= (TextView) findViewById(R.id.happy);
        unhappy= (TextView) findViewById(R.id.unhappy);
        doubt= (TextView) findViewById(R.id.doubt);
        toolbar= (Toolbar) findViewById(R.id.send_toolbar);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Send.this, "感谢你对本团的支持，我们会继续努力", Toast.LENGTH_SHORT).show();
            }
        });
        unhappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Send.this, "感谢你对本团的支持，我们会继续努力", Toast.LENGTH_SHORT).show();
            }
        });
        doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Send.this, "感谢你对本团的支持，我们会继续努力", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setToolbar(){
        toolbar.setTitle("发送反馈");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cancel:
                        startActivity(new Intent(Send.this,MainActivity2.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
                return true;
            }
        });
    }
}
