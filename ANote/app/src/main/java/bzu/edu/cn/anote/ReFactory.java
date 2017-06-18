package bzu.edu.cn.anote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;

/**
 * Created by hxs on 17-6-18.
 */

public class ReFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    public static List<HashMap<String, Object>> resdata = new ArrayList<HashMap<String, Object>>();
    private NotesnameSQLiteOpenHelper dbHaper;

    public ReFactory(Context context, Intent intent){
        this.context = context;
        dbHaper = new NotesnameSQLiteOpenHelper(context);
    }

    @Override
    public void onCreate() {
        SQLiteDatabase database = dbHaper.getReadableDatabase();
        Cursor c = database.query("notes", null, null, null, null, null, null);
        if(c.moveToFirst()){
            do{
                HashMap<String, Object> item = new HashMap<>();
                //int id = c.getInt(c.getColumnIndex("id"));
                String title = c.getString(c.getColumnIndex("title"));
                //item.put("id", id);
                item.put("title", title);
                resdata.add(item);
            }while(c.moveToNext());
        }
        c.close();
        database.close();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        resdata.clear();
    }

    @Override
    public int getCount() {
        return resdata.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(position < 0 || position > resdata.size())
            return null;
        else{
            HashMap<String, Object> content = resdata.get(position);
            RemoteViews rv = new RemoteViews(this.context.getPackageName(), R.layout.list_item);
            rv.setTextViewText(R.id.tv_title, "\t"+content.get("title"));

            Intent fillIntent = new Intent();
            fillIntent.putExtra(ListWidget.clickAction, position);
            rv.setOnClickFillInIntent(R.id.widget_aitme, fillIntent);
            return rv;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
