package bzu.edu.cn.anote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;
import bzu.edu.cn.anote.entity.Notes;
import bzu.edu.cn.anote.tool.UploadRecords;

/**
 * Created by 李小宁 on 2017/homepage/28.
 */

public class Notesdao {
    private NotesnameSQLiteOpenHelper helper;
    public Notesdao(Context context) {
        helper=new NotesnameSQLiteOpenHelper(context);
    }
    //对Notes表里的内容进行一系列操作，Notes表里的字段有分区，标题，内容，时间等
    private UploadRecords uploadRecords = new UploadRecords(null);
    public void insert(Notes notes){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",notes.getId());
        values.put("title",notes.getTitle());
        values.put("user",notes.getUser());
        values.put("notesname",notes.getNotesname());
        values.put("subarea",notes.getSubarea());
        values.put("content",notes.getContent());
        values.put("data",notes.getData());
        values.put("time",notes.getTime());
        long id=db.insert("notes",null,values);
        notes.setId(id);
        String sql = "insert into notes(id,title,user,notesname,subarea,content,data,time) values('"+notes.getId()+"','"+notes.getTitle()+"','"+notes.getUser()+"','"+notes.getNotesname()+"','"+notes.getSubarea()+"','"+notes.getContent()+"','"+notes.getData()+"','"+notes.getTime()+"')";
        uploadRecords.add(sql);
        db.close();
    }
    public List<Notes> query(String name,String nn,String subarea){
        List<Notes> notesList=new ArrayList<Notes>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor;
        if (TextUtils.isEmpty(subarea)||subarea.equals("")){
            cursor=db.rawQuery("select * from notes where user=? and notesname=?",new String[]{name,nn});
        }else{
            cursor=db.rawQuery("select * from notes where user=? and notesname=? and subarea=?",new String[]{name,nn,subarea});
        }
        while(cursor.moveToNext()){
            long id=cursor.getLong(cursor.getColumnIndex("id"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            Notes notes=new Notes(title,id);
            notesList.add(notes);
        }
        cursor.close();
        db.close();
        return notesList;
    }
    public long queryId(String name){
        long id=0;
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from notes where user=?",new String[]{name});
        while(cursor.moveToNext()){
            id=cursor.getLong(cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();
        return id;
    }
    public Notes queryBytitle(long id){
        SQLiteDatabase db=helper.getReadableDatabase();
        Notes notes=null;
        Cursor cursor=db.rawQuery("select * from notes where id=?",new String[]{String.valueOf(id)});
        while(cursor.moveToNext()){
            long id1=cursor.getLong(cursor.getColumnIndex("id"));
            String title1=cursor.getString(cursor.getColumnIndex("title"));
            String name1=cursor.getString(cursor.getColumnIndex("user"));
            String notesname1=cursor.getString(cursor.getColumnIndex("notesname"));
            String subarea1=cursor.getString(cursor.getColumnIndex("subarea"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            String data=cursor.getString(cursor.getColumnIndex("data"));
            String time=cursor.getString(cursor.getColumnIndex("time"));
            notes=new Notes(id1,name1,notesname1,subarea1,title1,data,content,time);
        }
        cursor.close();
        db.close();
        return notes;
    }
    public void update(String title,String data,String time,String content,long id,String name){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("update notes set title=?,data=?,time=?,content=? where id=?",new Object[]{title,data,time,content,id});
        String sql = "update notes set title='"+title +"',data='"+data+"',time='"+time+"',content='"+content+"' where id="+id+" "+"and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
    }
    public int delete(long id,String name){
        SQLiteDatabase db=helper.getWritableDatabase();
        int count = db.delete("notes","id=? and user=?",new String[]{id+"",name});
        String sql = "delete from notes where id = "+id +" and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
        return count;
    }
    public int deleteBynotesname(String notesname,String name){
        SQLiteDatabase db=helper.getWritableDatabase();
        int count = db.delete("notes","notesname=? and user=?",new String[]{notesname,name});
        String sql = "delete from notes where notesname='"+notesname+"'"+" "+"and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
        return count;
    }
    public int deleteBysubarea(String subarea,String name){
        SQLiteDatabase db=helper.getWritableDatabase();
        int count = db.delete("notes","subarea=? and user=?",new String[]{subarea,name});
        String sql = "delete from notes where subarea = '" + subarea+"'"+" "+"and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
        return count;
    }
}
