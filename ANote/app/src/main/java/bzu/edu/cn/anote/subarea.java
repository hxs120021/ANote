package bzu.edu.cn.anote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import bzu.edu.cn.anote.adapter.subareaAdapter;
import bzu.edu.cn.anote.dao.Notesdao;
import bzu.edu.cn.anote.dao.Subareadao;
import bzu.edu.cn.anote.entity.Subarea;
import bzu.edu.cn.anote.tool.ActivityCollector;
import bzu.edu.cn.anote.tool.DonRecord;
import bzu.edu.cn.anote.tool.UploadRecords;

public class subarea extends AppCompatActivity implements AbsListView.OnScrollListener{
    private long sub_id=0;
    private Button addsubarea;  //添加分区
    private ListView lvSubarea;  //listview控件
    private View dialogView;    //弹出框布局
    private EditText etSubarea;   //新建分区名
    private subareaAdapter adapter;  //适配器
    private String name;   //用户名
    private String notesname;  //笔记名称
    private Subareadao subareadao;  //分区
    private List<Subarea> subareaList;
    private Context context;
    private Toolbar toolbar;
    //刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    private DonRecord donRecord;    //同步类
    private UploadRecords uploadRecords = new UploadRecords(null);//同步类
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x101:
                    if(swipeRefreshLayout.isRefreshing()){
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);  //设置不刷新
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subarea);
        context=this;
        initView();  //初始化控件函数
        Intent intent=getIntent();  //接收上一个Activity
        name=intent.getStringExtra("name");
        notesname=intent.getStringExtra("notesname");
        setToolbar(notesname);  //调用设置toolbar函数
        subareadao=new Subareadao(this);
        subareaList=subareadao.query(name,notesname); //根据用户名，笔记名称查询分区
        adapter=new subareaAdapter(this,R.layout.subarea_item,subareaList);  //适配
        lvSubarea.setAdapter(adapter);  //listview设置适配器
        refresh();
    }
    public  void setToolbar(String nn){   //自定义标题栏
        toolbar.setTitle(nn);
        setSupportActionBar(toolbar);  //设置toolbar
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
                    case R.id.setting:
                        Intent intent1 =new Intent(subarea.this,Setting.class);
                        intent1.putExtra("account",name);
                        startActivity(intent1);
                        break;
                    case R.id.send:
                        Intent intent2 =new Intent(subarea.this,Send.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  //添加菜单
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void initView(){
        addsubarea= (Button) findViewById(R.id.btn_addsubarea);
        lvSubarea= (ListView) findViewById(R.id.lv_subarea);
        toolbar= (Toolbar) findViewById(R.id.sub_toolbar);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.sub_srl);
        lvSubarea.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {  //listview的Item的长按事件
                final Subarea sb= (Subarea) parent.getItemAtPosition(position);
                dialogView= LayoutInflater.from(subarea.this).inflate(R.layout.deltitle_alert,null);
                final Button btn_del= (Button) dialogView.findViewById(R.id.del_title);
                btn_del.setText("删除分区");
                final AlertDialog.Builder inputDialog=new AlertDialog.Builder(subarea.this);
                inputDialog.setTitle(sb.getNotesname()).setView(dialogView);
                final AlertDialog dialog=inputDialog.show();
                btn_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subareadao.delete(sb.getId(),name);
                        Notesdao n=new Notesdao(context);
                        n.deleteBysubarea(sb.getSubarea(),name);
                        subareaList=subareadao.query(name,notesname);
                        adapter=new subareaAdapter(context,R.layout.subarea_item,subareaList);
                        lvSubarea.setAdapter(adapter);
                        dialog.cancel();
                    }
                });
                return true;
            }
        });
        lvSubarea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //listview的Item的点击事件
                Subarea s= (Subarea) parent.getItemAtPosition(position);
                Intent intent=new Intent(subarea.this,displaytitle.class);
                intent.putExtra("name",name);
                intent.putExtra("notesname",notesname);
                intent.putExtra("subarea",s.getSubarea());
                startActivity(intent);
            }
        });
        findViewById(R.id.tvFast_subarea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(subarea.this,displaytitle.class);
                intent.putExtra("name",name);
                intent.putExtra("notesname",notesname);
                intent.putExtra("subarea","");
                startActivity(intent);
            }
        });
    }
    public void addSubarea(View view){  //新建分区
        dialogView=LayoutInflater.from(subarea.this).inflate(R.layout.subarea_alert,null);
        final Button create= (Button) dialogView.findViewById(R.id.subarea_create);
        final Button esc= (Button) dialogView.findViewById(R.id.subarea_esc);
        etSubarea= (EditText) dialogView.findViewById(R.id.etSubarea);
        final AlertDialog.Builder inputDialog=new AlertDialog.Builder(subarea.this);
        inputDialog.setTitle("新建分区").setView(dialogView);
        final AlertDialog dialog=inputDialog.show();
        etSubarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(etSubarea.getText().toString())){
                    create.setEnabled(false);
                    create.setTextColor(0xffcccccc);
                }else{
                    create.setEnabled(true);
                    create.setTextColor(0xff444444);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub_id=subareadao.queryId(name);
                String sub=etSubarea.getText().toString();
                Subarea subarea=new Subarea(++sub_id,name,sub,notesname);
                subareadao.insert(subarea);
                subareaList.add(subarea);        //添加到集合
                adapter.notifyDataSetChanged();  //更新
                dialog.cancel();
            }
        });
        esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private int visibleLastIndex;  //用来可显示的最后一条数据的索引
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {   //onScrollStateChanged在listview状态改变时被调用，可以用来获取当前listview的状态：空闲SCROLL_STATE_IDLE
        if(adapter.getCount()==visibleLastIndex && scrollState==SCROLL_STATE_IDLE){
            new LoadDataThread().start();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  //onSCroll在listview滑动过程中被调用，可以获取到listview有多少条item以及现在显示到了第几条等等一些信息
        visibleLastIndex=firstVisibleItem+visibleItemCount-1;
    }
    class LoadDataThread extends Thread{
        public void run(){
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){e.printStackTrace();}
            handler.sendEmptyMessage(0x101);
        }
    }
    //刷新
    public void refresh(){
        //定义下拉刷新控件的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //刷新所触发的事件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if(!UploadRecords.list.isEmpty())
                    {
                        System.out.println("哈哈哈aaa");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    uploadRecords.cre(context);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                    if(UploadRecords.list.isEmpty()){
                        donRecord.run(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subareaList=subareadao.query(name,notesname);
                adapter=new subareaAdapter(context,R.layout.subarea_item,subareaList);
                lvSubarea.setAdapter(adapter);
                new LoadDataThread().start();
            }
        });
    }
}
