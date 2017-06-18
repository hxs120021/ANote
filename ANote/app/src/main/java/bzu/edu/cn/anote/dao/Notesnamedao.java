package bzu.edu.cn.anote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;
import bzu.edu.cn.anote.entity.Notesname;
import bzu.edu.cn.anote.tool.UploadRecords;

/**
 * Created by 李小宁 on 2017/homepage/28.
 */

public class Notesnamedao {
    private UploadRecords uploadRecords = new UploadRecords(null);
    private NotesnameSQLiteOpenHelper helper;
    public Notesnamedao(Context context) {   //构造方法，在创建Notesnamedao对象的时候建立数据库
        helper=new NotesnameSQLiteOpenHelper(context);
    }
    public long insert(Notesname notesname){   //插入笔记名称
        SQLiteDatabase db=helper.getWritableDatabase();   //创建一个写入的 SQLiteDatabase的对象
        ContentValues values=new ContentValues();    //创建一个ContentValues将参数名和列添加到ContentValues对象里
        values.put("id",notesname.getId());
        values.put("notesname",notesname.getNotesname());
        values.put("user",notesname.getUser());
        long id=db.insert("notesname",null,values);  //values的内容插入到notesname表里并获得id值
        String sql = "insert into notesname(id,notesname,user) values('"+notesname.getId()+"','"+notesname.getNotesname()+"','"+notesname.getUser()+"')";
        uploadRecords.add(sql);
        notesname.setId(id);   // 将id值赋给notesname的id属性
        db.close();
        return id;
    }
    public List<Notesname> query(String user){   //根据用户名查询所有的笔记名称，将查到的内容以集合的形式返回
        List<Notesname> list=new ArrayList<Notesname>();
        SQLiteDatabase db=helper.getReadableDatabase();//创建一个读取的 SQLiteDatabase的对象
        Cursor cursor=db.query("notesname",null,"user=?",new String[]{user},null,null,null,null);   //在notesname表里查询user字段为user的记录
        while (cursor.moveToNext()){    //cursor为游标对象，下一个存在，执行循环体内的语句
            long id1=cursor.getLong(cursor.getColumnIndex("id"));
            String notesname=cursor.getString(cursor.getColumnIndex("notesname"));
            Notesname notesname1=new Notesname(id1,user,notesname);    //将查到的内容封装到 Notesname对象中
            list.add(notesname1); //将 Notesname对象添加到集合里
        }
        db.close();
        cursor.close();
        return list;  //返回集合
    }
    public long queryId(String user){
        long id1=0;
        SQLiteDatabase db=helper.getReadableDatabase();//创建一个读取的 SQLiteDatabase的对象
        Cursor cursor=db.query("notesname",null,"user=?",new String[]{user},null,null,null,null);   //在notesname表里查询user字段为user的记录
        while (cursor.moveToNext()){    //cursor为游标对象，下一个存在，执行循环体内的语句
            id1=cursor.getLong(cursor.getColumnIndex("id"));
            String notesname=cursor.getString(cursor.getColumnIndex("notesname"));
        }
        cursor.close();
        db.close();
        return id1;  //返回集合
    }
    public int delete(long id,String name){  //根据id删除笔记名称
        SQLiteDatabase db=helper.getWritableDatabase();
        int count = db.delete("notesname","id=? and user=?",new String[]{id+"",name});  //根据id删除notesname表里的记录
        String sql = "delete from notesname where id="+id+" "+"and user='"+name+"'";
        uploadRecords.add(sql);
        db.close();
        return count;
    }
}
