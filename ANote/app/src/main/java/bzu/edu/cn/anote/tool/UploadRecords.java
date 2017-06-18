package bzu.edu.cn.anote.tool;


import android.content.Context;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bzu.edu.cn.anote.dao.OperatingRecordDao;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Hatsuhikari on 2017/6/10.
 */

public class UploadRecords {
    private static int i = 0;
    private NetWork netWork = new NetWork();
    private OperatingRecordDao operatingRecordDao;
    public static List<String> list = new ArrayList<String>();
    static JSONObject jsonObject = new JSONObject();
    Context context;
    public UploadRecords(Context contextr){
        this.context=context;
    }
    public List<String> add(String s){
        System.out.println(i+"这是i");
        list.add(i,s);
        System.out.println(list+"  "+i);
        i++;
        return list;
    }
    public void cre(Context context) throws IOException {
        jsonObject.put("add",list);
        if(netWork.isConnected(context) == false) {
            operatingRecordDao = new OperatingRecordDao(context);
            operatingRecordDao.adds(jsonObject.toString());
        }else {
            up(jsonObject.toString());
        }
    }
    public void up(String json) throws IOException {
        post("http://10.3.21.199:8080/DataServlet",json);
    }
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    @SuppressLint("NewApi")
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println(json+"上传！");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String a = response.body().string();
            System.out.println(a+"这是a");
            if(a.equals("123")){
                System.out.println("你妹啊1");
                list.removeAll(list);
                i = 0;
                if(list.isEmpty()){
                    System.out.println("你妹啊2");
                }
            }else {
                System.out.println(a);
            }
            response.body().close();
            return a;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}