package bzu.edu.cn.anote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;
import bzu.edu.cn.anote.entity.User;


/**
 * Created by 李小宁 on 2017/homepage/28.
 */

public class Userdao {
    private NotesnameSQLiteOpenHelper helper;
    public Userdao(Context context) {
        helper=new NotesnameSQLiteOpenHelper(context);
    }

    public long insert(User user ){

        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("username",user.getUsername());
        values.put("password",user.getPassword());
        long id=db.insert("user",null,values);   //向user表插入数据values
        db.close();
        return id;
    }
    public User query(String name){
        User user=new User();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor c=db.query("user",null,"username=?",new String[]{name},null,null,null,null);
        while(c.moveToNext()){
            String username=c.getString(1);
            String pass=c.getString(2);
            user.setUsername(username);
            user.setPassword(pass);
        }
        return user;
    }
}
