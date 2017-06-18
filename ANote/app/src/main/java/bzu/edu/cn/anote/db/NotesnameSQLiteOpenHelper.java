package bzu.edu.cn.anote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 李小宁 on 2017/homepage/28.
 */

public class NotesnameSQLiteOpenHelper extends SQLiteOpenHelper {
    private Context contexts;
    private String sql1="create table notesname(id integer primary key,notesname varchar(100),user varchar(50))";
    private String sql2="create table subarea(id integer primary key,subarea varchar(50),user varchar(50),notesname varchar(100))";
    private String sql3="create table notes( id integer primary key,title varchar(100),user varchar(50),notesname varchar(100),subarea varchar(100),content varchar(1000),data varchar(50),time varchar(50))";
    private String sql4="create table operatingrecord(id integer primary key AUTOINCREMENT,log varchar(9999))";
    public NotesnameSQLiteOpenHelper(Context context) {
        super(context, "notes.db", null, 1);
        contexts=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
