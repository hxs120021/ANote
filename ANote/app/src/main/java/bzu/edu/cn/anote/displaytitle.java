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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import bzu.edu.cn.anote.adapter.notesAdapter;
import bzu.edu.cn.anote.dao.Notesdao;
import bzu.edu.cn.anote.entity.Notes;
import bzu.edu.cn.anote.pain.PainActivity;
import bzu.edu.cn.anote.pain.PaintView;
import bzu.edu.cn.anote.tool.ActivityCollector;
import bzu.edu.cn.anote.tool.DonRecord;
import bzu.edu.cn.anote.tool.UploadRecords;

public class displaytitle extends AppCompatActivity implements AbsListView.OnScrollListener{

    private long dis_id=0;
    private ListView lvnotes;   //listview控件
    private ImageButton addNotes;  //添加按钮
    private ImageButton ib_edit;
    private String name;
    private String notesname;
    private String subarea;
    private notesAdapter adapter;
    private Notesdao notesdao;
    private List<Notes> notesList;
    private View dialogView;
    private Context context;
    private Toolbar toolbar;
    private PaintView mPaintView;
    private FrameLayout mFrameLayout;
    private Button mBtnOK, mBtnClear, mBtnCancel;
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
        setContentView(R.layout.activity_displaytitle);
        context=this;
        initView();
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        notesname=intent.getStringExtra("notesname");
        subarea=intent.getStringExtra("subarea");
        if(subarea.equals("")){
            setToolbar("快速笔记");
        }else{
            setToolbar(subarea);
        }
        notesdao=new Notesdao(this);
        notesList=notesdao.query(name,notesname,subarea);
        adapter=new notesAdapter(this,R.layout.list_item,notesList);
        lvnotes.setAdapter(adapter);
        refresh();
    }
    public void setToolbar(String sb){
        toolbar.setTitle(sb);
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
                    case R.id.setting:
                        Intent intent1=new Intent(displaytitle.this,Setting.class);
                        intent1.putExtra("account",name);
                        startActivity(intent1);
                        break;
                    case R.id.send:
                        Intent intent2=new Intent(displaytitle.this,Send.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public void initView(){
        addNotes= (ImageButton) findViewById(R.id.add_notes);
        ib_edit= (ImageButton) findViewById(R.id.ib_edit);
        lvnotes= (ListView) findViewById(R.id.lv_titles);
        toolbar= (Toolbar) findViewById(R.id.dis_toolbar);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.dis_srl);
        lvnotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {  //长按删除
                final Notes notes= (Notes) parent.getItemAtPosition(position);  //获取点击位置上的数据
                dialogView= LayoutInflater.from(displaytitle.this).inflate(R.layout.deltitle_alert,null);  //弹出框布局
                final Button btn_del= (Button) dialogView.findViewById(R.id.del_title);//获取子布局中的删除按钮
                btn_del.setText("删除页");
                final AlertDialog.Builder inputDialog=new AlertDialog.Builder(displaytitle.this);  //创建提示框
                if(TextUtils.isEmpty(notes.getTitle())){
                    inputDialog.setTitle("无标题页").setView(dialogView);
                }else{
                    inputDialog.setTitle(notes.getTitle()).setView(dialogView);
                }
                final AlertDialog dialog=inputDialog.show();
                btn_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notesdao.delete(notes.getId(),name);
                        notesList=notesdao.query(name,notesname,subarea);
                        adapter=new notesAdapter(context,R.layout.list_item,notesList);
                        lvnotes.setAdapter(adapter);
                        dialog.cancel();
                    }
                });

                return true;
            }

        });
        lvnotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notes notes= (Notes) parent.getItemAtPosition(position);
                //Toast.makeText(displaytitle.this, notes.getId()+""+notes.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(displaytitle.this, WriteNotes.class);
                Bundle bundle=new Bundle();
                bundle.putLong("id",notes.getId());
                bundle.putString("name",name);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });
        ib_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(displaytitle.this, PainActivity.class);
                displaytitle.this.startActivity(intent);
            }
        });
    }
    public void addNotes(View view) {
        Intent intent = new Intent(displaytitle.this, WriteNotes.class);
        Bundle bundle=new Bundle();
        bundle.getLong("id",0);
        bundle.putString("name",name);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);  //这种方式启动Activity，需要调回onActivityResult()方法
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){  //回传数据不为空执行
            //判断结果码是否等于1，等于1添加笔记内容
            if (resultCode==1){
                if(requestCode==1){
                    dis_id=notesdao.queryId(name);
                    String wtitle= (String) data.getStringExtra("wtitle");
                    String wdata= (String) data.getStringExtra("wdata");
                    String wcontent= (String) data.getStringExtra("wcontent");
                    String wtime= (String) data.getStringExtra("wtime");
                    Notes notes=new Notes(++dis_id,name,notesname,subarea,wtitle,wdata,wcontent,wtime);
                    notesdao.insert(notes);
                    notesList.add(notes);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        //判断结果码是否等于1，等于1更新笔记内容
        if(resultCode==2){
            notesList=notesdao.query(name,notesname,subarea);
            adapter=new notesAdapter(this,R.layout.list_item,notesList);
            lvnotes.setAdapter(adapter);
        }
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
                notesList=notesdao.query(name,notesname,subarea);
                adapter=new notesAdapter(context,R.layout.list_item,notesList);
                lvnotes.setAdapter(adapter);
                new LoadDataThread().start();
            }
        });
    }
}
