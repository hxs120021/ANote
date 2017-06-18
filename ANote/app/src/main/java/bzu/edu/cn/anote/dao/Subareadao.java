package bzu.edu.cn.anote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;
import bzu.edu.cn.anote.entity.Subarea;
import bzu.edu.cn.anote.tool.UploadRecords;

/**
 * Created by 李小宁 on 2017/homepage/29.
 */

public class Subareadao {
    private UploadRecords uploadRecords = new UploadRecords(null);
    private NotesnameSQLiteOpenHelper helper;

    public Subareadao(Context context) {
        helper=new NotesnameSQLiteOpenHelper(context);
    }
    //对Subareadao表里的内容进行一系列操作，Subareadao表里的字段有用户名，笔记名称，分区
    public long insert(Subarea subarea){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",subarea.getId());
        values.put("subarea",subarea.getSubarea());
        values.put("user",subarea.getUser());
        values.put("notesname",subarea.getNotesname());
        long id=db.insert("subarea",null,values);
        String sql = "insert into subarea(id,subarea,user,notesname) values('"+subarea.getId()+"','"+subarea.getSubarea()+"','"+subarea.getUser()+"','"+subarea.getNotesname()+"')";
        uploadRecords.add(sql);
        subarea.setId(id);
        db.close();
        return id;
    }
    public List<Subarea> query(String name,String nn){
        SQLiteDatabase db=helper.getReadableDatabase();
        List<Subarea> subareaList=new ArrayList<Subarea>();
        Cursor cursor=db.rawQuery("select * from subarea where user=?  and notesname=?",new String[]{name,nn});
        while(cursor.moveToNext()){
            long id1=cursor.getLong(cursor.getColumnIndex("id"));
            String subarea=cursor.getString(cursor.getColumnIndex("subarea"));
            Subarea subarea1=new Subarea(id1,name,subarea,nn);
            subareaList.add(subarea1);
        }
        cursor.close();
        db.close();
        return subareaList;
    }
    public long queryId(String name){
        long id1=0;
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from subarea where user=?",new String[]{name});
        while(cursor.moveToNext()){
            id1=cursor.getLong(cursor.getColumnIndex("id"));
            String subarea=cursor.getString(cursor.getColumnIndex("subarea"));
        }
        cursor.close();
        db.close();
        return id1;
    }
    public int delete(long id,String name){
        SQLiteDatabase db=helper.getWritableDatabase();
        int count = db.delete("subarea","id=? and user=?",new String[]{id+"",name});
        String sql = "delete from subarea where id="+id+" "+"and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
        return count;
    }

    public int deleteBynotesname(String notesname,String name){
        SQLiteDatabase db=helper.getWritableDatabase();
        int count = db.delete("subarea","notesname=? and user=?",new String[]{notesname,name});
        String sql = "delete from subarea where notesname='"+notesname+"'"+" "+"and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
        return count;
    }
}
