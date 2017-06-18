package bzu.edu.cn.anote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;
import bzu.edu.cn.anote.tool.NetWork;
import bzu.edu.cn.anote.tool.UploadRecords;

/**
 * Created by Hatsuhikari on 2017/6/16.
 */

public class OperatingRecordDao {
    private UploadRecords uploadRecords = new UploadRecords(null);
    private NotesnameSQLiteOpenHelper helper;
    Context context;
    public OperatingRecordDao(Context context) {
        this.context = context;
        helper=new NotesnameSQLiteOpenHelper(context);
    }
    List<String> list = new ArrayList<String>();
    JSONObject jsonObject = new JSONObject();
    static int i = 0;

    public void adds(String log){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("log",log);
        System.out.println(values+"dasdasdsadqweqw23231212!!!");
        long id=db.insert("operatingrecord",null,values);
        db.close();
    }

    public int judge(){
        SQLiteDatabase db=helper.getWritableDatabase();
        int number=0;
        Cursor c = db.rawQuery("select * from operatingrecord", null);
        if(c != null){
            number=c.getCount();
        }
        c.close();
        db.close();
        System.out.println(number+"这是c");
        return number;
    }
    public void update() throws IOException {
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select log from operatingrecord",null);
        if(NetWork.isConnected(context)) {
            System.out.println(NetWork.isConnected(context)+"dsasd");
            while (cursor.moveToNext()) {
                String log = cursor.getString(cursor.getColumnIndex("log"));
                uploadRecords.up(log);
            }
            db.execSQL("DELETE FROM operatingrecord");
        }
        cursor.close();
        db.close();
    }
}
