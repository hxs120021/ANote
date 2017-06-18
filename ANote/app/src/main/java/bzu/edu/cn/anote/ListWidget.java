package bzu.edu.cn.anote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.HashMap;

import bzu.edu.cn.anote.db.NotesnameSQLiteOpenHelper;

/**
 * Implementation of App Widget functionality.
 */
public class ListWidget extends AppWidgetProvider {

    public final static String refrashAction = "refrash";
    public final static String clickAction = "clickAction";
    private NotesnameSQLiteOpenHelper dbHelper;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName cn = new ComponentName(context, ListWidget.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_widget);

        Intent intent = new Intent(context, ReService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

        remoteViews.setRemoteAdapter(R.id.widget_listview, intent);
        remoteViews.setEmptyView(R.id.widget_listview, R.layout.list_item);

        Intent clickIntent = new Intent(context, ListWidget.class);
        clickIntent.setAction(clickAction);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_listview,pending);

        Intent reIntent = new Intent(context, ListWidget.class);
        reIntent.setAction(refrashAction);
        PendingIntent rePendin = PendingIntent.getBroadcast(context, 0, reIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_refrash, rePendin);

        appWidgetManager.updateAppWidget(cn, remoteViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();

        String action = intent.getAction();
        if(action.equals(refrashAction)){
            Toast.makeText(context, "refrash", Toast.LENGTH_SHORT).show();
            ReFactory.resdata.clear();
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, ListWidget.class);
            dbHelper = new NotesnameSQLiteOpenHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.query("notes", null, null, null, null, null, null);
            if(c.moveToFirst()) {
                do {
                    HashMap<String, Object> item = new HashMap<>();
                    String title = c.getString(c.getColumnIndex("title"));
                    if(title.equals(""))
                        title = "新标签页";
                    item.put("title", title);
                    ReFactory.resdata.add(item);
                }while(c.moveToNext());
            }
            c.close();
            db.close();
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn), R.id.widget_listview);

        }else if(action.equals(clickAction)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            long viewIndex = intent.getIntExtra(clickAction, 0);
            Intent startIntent = new Intent(context, WriteNotes.class);
            startIntent.putExtra("id", viewIndex + 1);
            context.startActivity(startIntent);

            Toast.makeText(context, "" + appWidgetId + "  " + viewIndex, Toast.LENGTH_SHORT).show();

        }
    }

}

