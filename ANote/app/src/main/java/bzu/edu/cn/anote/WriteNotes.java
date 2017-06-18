package bzu.edu.cn.anote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import bzu.edu.cn.anote.dao.Notesdao;
import bzu.edu.cn.anote.entity.Notes;
import bzu.edu.cn.anote.tool.ActivityCollector;

public class WriteNotes extends AppCompatActivity {
    private EditText et_title; //标题
    private TextView tv_data;  //时期
    private TextView tv_time;  //时间
    private EditText et_content;  //内容
    private String title;
    private String content;
    private String time;
    private String data;
    private Notes notes=null;
    private Notesdao notesdao;
    private long id;
    private String name;
    private Intent intent1;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notes);

        //初始化控件
        initView();
        setToolbar();
        SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyy年MM月dd日");
        SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        intent1=getIntent();  //接收intent
        setResult(2,intent1);
        Bundle bundle=intent1.getExtras();
        id=bundle.getLong("id");  //接收传递过来的id
        name=bundle.getString("name");
        notesdao=new Notesdao(this);
        if (id>0){  //id大于0，说明表里有内容，将表内的数据查询出来
            notes=notesdao.queryBytitle(id);
            et_title.setText(notes.getTitle());
            et_content.setText(notes.getContent());
            tv_data.setText(notes.getData());
            tv_time.setText(notes.getTime());
        }else{   //否则，初始化创建记录的时间
            String str1 = formatter1.format(curDate);
            String str2=formatter2.format(curDate);
            tv_data.setText(str1);
            tv_time.setText(str2);
        }
    }
    public void setToolbar(){
        toolbar.setTitle("云笔记");
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
                    case R.id.write_del:   //删除页
                        notesdao.delete(id,name);
                        onBackPressed();
                        break;
                    case R.id.write_setting:
                        Intent intent=new Intent(WriteNotes.this,Setting.class);
                        intent.putExtra("account",name);
                        startActivity(intent);
                        break;
                    case R.id.write_send:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_menu,menu);
        return true;
    }

    public void initView(){
        et_title= (EditText) findViewById(R.id.et_title);
        tv_data= (TextView) findViewById(R.id.tv_data);
        tv_time= (TextView) findViewById(R.id.tv_time);
        et_content= (EditText) findViewById(R.id.et_content);
        toolbar= (Toolbar) findViewById(R.id.write_toolbar);
    }

    @Override
    public void onBackPressed() {   //返回事件
//        super.onBackPressed();  //将父类的finish()方法重写
        title=et_title.getText().toString();
        content=et_content.getText().toString();
        data=tv_data.getText().toString();
        time=tv_time.getText().toString();
        if(id>0){  //如果id>0修改表里的内容
            notesdao.update(title,data,time,content,id,name);
            this.finish();
        }
        Intent intent=new Intent();
        intent.putExtra("wtitle",title);
        intent.putExtra("wdata",data);
        intent.putExtra("wcontent",content);
        intent.putExtra("wtime",time);
        setResult(1,intent);
        this.finish();
    }
}
