package bzu.edu.cn.anote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import bzu.edu.cn.anote.adapter.NotesNameAdapter;
import bzu.edu.cn.anote.dao.Notesdao;
import bzu.edu.cn.anote.dao.Notesnamedao;
import bzu.edu.cn.anote.dao.OperatingRecordDao;
import bzu.edu.cn.anote.dao.Subareadao;
import bzu.edu.cn.anote.entity.Notesname;
import bzu.edu.cn.anote.tool.ActivityCollector;
import bzu.edu.cn.anote.tool.DonRecord;
import bzu.edu.cn.anote.tool.NetWork;
import bzu.edu.cn.anote.tool.PackageUpdate;
import bzu.edu.cn.anote.tool.UploadRecords;

public class HomePage extends AppCompatActivity implements AbsListView.OnScrollListener{
    static Activity instance;
    private long wel_id=0;  //notesname的id
    private TextView account;
    private ListView lv_notes;    //listview控件
    private View dialogView;     //提示框布局
    private EditText etinput;    //输入笔记本名称
    private static final String [] m={"OneDrive-个人"};
    private Spinner spinnerNotes;  //下拉菜单
    private NotesNameAdapter notesNameAdapter;      //notesName适配器
    private ArrayAdapter<String> adapter;
    private Notesnamedao notesnamedao;
    private DonRecord donRecord;    //同步类
    private UploadRecords uploadRecords;//同步类
    private List<Notesname> notesnameList;
    private OperatingRecordDao operatingRecordDao;
    public String name;
    private Context context;
    private Toolbar toolbar;  //标题栏
    private PackageUpdate packageUpdate;
    long exitTime =0;
    //刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x101:
                    if(swipeRefreshLayout.isRefreshing()){
                        notesNameAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_homepage);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context=this;
        operatingRecordDao = new OperatingRecordDao(this);
        packageUpdate = new PackageUpdate();
        initView();  //初始化控件函数
        setToolbar();
        Intent intent=getIntent();   //接收上一个Activity
        if(NetWork.isConnected(this)) {
            new Thread() {
                @Override
                public void run() {
                    if (operatingRecordDao.judge() != 0) {
                        try {
                            operatingRecordDao.update();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        name=intent.getStringExtra("username");
        account.setText("账户："+name);
        notesnamedao=new Notesnamedao(this);
        donRecord = new DonRecord(this);//初始化操作类
        uploadRecords = new UploadRecords(this);
        notesnameList=notesnamedao.query(name);  //根据用户名从notesname表里查询
        notesNameAdapter=new NotesNameAdapter(this,R.layout.notes_item,notesnameList);  //适配
        lv_notes.setAdapter(notesNameAdapter);  //listview控件适配
        refresh();  //刷新页面
//        setBreoadcast();
    }
    public void setToolbar(){
        toolbar.setTitle("云笔记");
        setSupportActionBar(toolbar);  //设置Toolbar
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.setting:
                        Intent intent1=new Intent(HomePage.this,Setting.class);
                        intent1.putExtra("account",name);
                        startActivity(intent1);
                        break;
                    case R.id.send:
                        Intent intent2=new Intent(HomePage.this,Send.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else
            {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public  void initView(){  //初始化控件函数
        toolbar= (Toolbar) findViewById(R.id.wel_toolbar);
        account= (TextView) findViewById(R.id.account);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.wel_srl);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent=new Intent(HomePage.this,Setting.class);
                inent.putExtra("account",name);
                startActivity(inent);
            }
        });
        lv_notes= (ListView) findViewById(R.id.lv_notes);
        lv_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {   //listview的Item长按事件
                final Notesname nn= (Notesname) parent.getItemAtPosition(position);  //获得点击位置上的数据
                dialogView= LayoutInflater.from(HomePage.this).inflate(R.layout.deltitle_alert,null);  //创建点击新建笔记后自定义出现的提示框
                final Button btn_del= (Button) dialogView.findViewById(R.id.del_title);
                btn_del.setText("删除笔记");
                final AlertDialog.Builder inputDialog=new AlertDialog.Builder(HomePage.this);
                inputDialog.setTitle(nn.getNotesname()).setView(dialogView);
                final AlertDialog dialog=inputDialog.show();  //将提示框显示
                btn_del.setOnClickListener(new View.OnClickListener() {  //删除的点击事件
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, nn.getId()+"", Toast.LENGTH_SHORT).show();
                        notesnamedao.delete(nn.getId(),name);  //根据id删除笔记名称
                        Subareadao s=new Subareadao(context);
                        s.deleteBynotesname(nn.getNotesname(),name);  //根据笔记名称删除分区
                        Notesdao n=new Notesdao(context);
                        n.deleteBynotesname(nn.getNotesname(),name);  //根据笔记名称删除笔记内容
                        notesnameList=notesnamedao.query(name);  //查询删除后笔记名称
                        notesNameAdapter=new NotesNameAdapter(context,R.layout.notes_item,notesnameList);  //适配
                        lv_notes.setAdapter(notesNameAdapter);  //listview设置适配器
                        dialog.cancel();  //关闭弹出框
                    }
                });
                return true;
            }
        });
        lv_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {//listview的Item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//listview的Item的点击事件
                Notesname n= (Notesname) parent.getItemAtPosition(position);  //获得点击位置上的数据
                //打开下一个Activity
                Intent intent=new Intent(HomePage.this,subarea.class);
                //传递参数
                intent.putExtra("name",name);
                intent.putExtra("notesname",n.getNotesname());
                startActivity(intent);  //开启下一个Activit
            }
        });
    }
    public void ss(View v){  //点击新建笔记后，设置弹出框
        dialogView= LayoutInflater.from(HomePage.this).inflate(R.layout.newnotes_alert,null);
        etinput= (EditText)dialogView.findViewById(R.id.etNotes_name);
        spinnerNotes= (Spinner)dialogView.findViewById(R.id.spinnerNotes);
        //将可选内容与ArrayAdapter连接起来
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //将adapter添加到spinner中
        spinnerNotes.setAdapter(adapter);
        final AlertDialog.Builder inputDialog=new AlertDialog.Builder(HomePage.this);
        inputDialog.setTitle("新建笔记本").setView(dialogView);
        final AlertDialog dialog=inputDialog.show();
        final Button create= (Button) dialogView.findViewById(R.id.create);
        final Button esc= (Button) dialogView.findViewById(R.id.btn_esc);
        etinput.addTextChangedListener(new TextWatcher() {  //输入框监听事件
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {  //输入框输入文字之后触发事件
                //如果输入框内容为空则创建按钮失效、否则可以创建
                if(TextUtils.isEmpty(etinput.getText().toString())){
                    create.setEnabled(false);
                    create.setTextColor(0xffcccccc);
                }
                else{
                    create.setEnabled(true);
                    create.setTextColor(0xff444444);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击创建按钮，创建笔记名称
                wel_id=notesnamedao.queryId(name);
                Notesname nt=new Notesname(++wel_id,name,etinput.getText().toString().trim());
                notesnamedao.insert(nt);
                notesnameList.add(nt);
                notesNameAdapter.notifyDataSetChanged();//更新
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
        if(notesNameAdapter.getCount()==visibleLastIndex && scrollState==SCROLL_STATE_IDLE){
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
                            donRecord.run(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notesnameList = notesnamedao.query(name);  //根据用户名从notesname表里查询
                    notesNameAdapter = new NotesNameAdapter(context, R.layout.notes_item, notesnameList);  //适配
                    lv_notes.setAdapter(notesNameAdapter);  //listview控件适配
                    new LoadDataThread().start();
            }
        });
    }
    /**
     * 设置网络监听
     */
//    private void setBreoadcast() {
//        BroadcastReceiver receiver=new NetBroadCastReciver();
//        IntentFilter filter=new IntentFilter();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(receiver, filter);
//    }
}
