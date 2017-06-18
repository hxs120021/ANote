package bzu.edu.cn.anote.tool;

import android.content.Context;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;

import bzu.edu.cn.anote.dao.Notesnamedao;
import bzu.edu.cn.anote.dao.TongBu;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Hatsuhikari on 2017/6/11.
 */
public class DonRecord {
    private Context contexts;
    private TongBu tongBu;
    public DonRecord(Context context){
        contexts = context;
        tongBu= new TongBu(contexts);
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private Notesnamedao notesnamedao = new Notesnamedao(contexts);
    private final OkHttpClient client = new OkHttpClient();
    public void run(String name) throws IOException {
        System.out.println(name);
        Request request = new Request.Builder()
                .url("http://10.3.21.199:8080/TransferServlet")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, name))
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String ss = response.body().string();
        System.out.println(ss+"回传!");

        JSONObject jsonObject = JSONObject.fromObject(ss);

            String n = jsonObject.getString("notesname");
            String n2 = jsonObject.getString("subarea");
            String n3 = jsonObject.getString("notes");
            JSONArray jsonArray = JSONArray.fromObject(n);
            JSONArray jsonArray2 = JSONArray.fromObject(n2);
            JSONArray jsonArray3 = JSONArray.fromObject(n3);
            String a,b,c,d,e,f,g,j,k="";
            for(int i = 0 ;i<jsonArray.size();i++){
                a = (JSONObject.fromObject(jsonArray.get(i))).getString("id");
                b = (JSONObject.fromObject(jsonArray.get(i))).getString("notesname");
                c = (JSONObject.fromObject(jsonArray.get(i))).getString("user");
                tongBu.insert1(a,b,c);
            }
            for(int i = 0;i<jsonArray2.size();i++){
                a = (JSONObject.fromObject(jsonArray.get(i))).getString("id");
                c = (JSONObject.fromObject(jsonArray.get(i))).getString("user");
                System.out.println(jsonArray2.size());
                b = (JSONObject.fromObject(jsonArray.get(i))).getString("subarea");

                d = (JSONObject.fromObject(jsonArray.get(i))).getString("notesname");
                tongBu.insert2(a,b,c,d);
            }

            for(int i = 0;i<jsonArray3.size();i++){
                a = (JSONObject.fromObject(jsonArray.get(i))).getString("id");
                b = (JSONObject.fromObject(jsonArray.get(i))).getString("title");
                c = (JSONObject.fromObject(jsonArray.get(i))).getString("user");
                d = (JSONObject.fromObject(jsonArray.get(i))).getString("notesname");
                e = (JSONObject.fromObject(jsonArray.get(i))).getString("subarea");
                f = (JSONObject.fromObject(jsonArray.get(i))).getString("content");
                j = (JSONObject.fromObject(jsonArray.get(i))).getString("data");
                k = (JSONObject.fromObject(jsonArray.get(i))).getString("time");
                tongBu.insert3(a,b,c,d,e,f,j,k);
            }
    }

}
