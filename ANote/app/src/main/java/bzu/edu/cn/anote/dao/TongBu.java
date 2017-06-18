package bzu.edu.cn.anote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;
import bzu.edu.cn.anote.tool.UploadRecords;


/**
 * Created by Hatsuhikari on 2017/6/11.
 */

public class TongBu{
    private NotesnameSQLiteOpenHelper helper;
    private UploadRecords uploadRecords = new UploadRecords(null);
    public TongBu(Context context) {
//        super(context, "notes.db", null, bcg);
        helper=new NotesnameSQLiteOpenHelper(context);
        System.out.println(context+"@@@@!!!");
    }
    //NotesName表
    public void insert1(String id,String notesname,String user){   //插入笔记名称
        SQLiteDatabase db=helper.getWritableDatabase();
        System.out.println(id+notesname+user);
        ContentValues values=new ContentValues();    //创建一个ContentValues将参数名和列添加到ContentValues对象里
        values.put("id",id);
        values.put("notesname",notesname);
        values.put("user",user);
        System.out.println(values+"!!!");
        db.insert("notesname",null,values);
        db.close();
    }

    //对Subareadao表里的内容进行一系列操作，Subareadao表里的字段有用户名，笔记名称，分区
    public void insert2(String id,String subarea,String user,String notesname){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("subarea",subarea);
        values.put("user",user);
        values.put("notesname",notesname);
        db.insert("subarea",null,values);
        db.close();
    }
    public void insert3(String id,String title,String user,String notesname,String subarea,String content, String data,String time){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("title",title);
        values.put("user",user);
        values.put("notesname",notesname);
        values.put("subarea",subarea);
        values.put("content",content);
        values.put("data",data);
        values.put("time",time);
        db.insert("notes",null,values);
        db.close();
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
}
