package bzu.edu.cn.anote.hatsuhikari;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import bzu.edu.cn.anote.R;
import bzu.edu.cn.anote.Start;
import bzu.edu.cn.anote.tool.ActivityCollector;


/**
 * Created by Hatsuhikari on 2017/homepage/27.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enterHomeActivity();
        ActivityCollector.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        SharedPreferences setting = getSharedPreferences("YOUR_PREF_FILE_NAME", 0);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = setting.getBoolean("FIRST",true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            setting.edit().putBoolean("FIRST", false).commit();
            Intent intent = new Intent(this, WebViewLoadWeb.class);
            startActivity(intent);
            finish();
            return;
        }
        // 如果不是第一次启动app，则正常显示启动屏

        setContentView(R.layout.activity_main2);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, Start.class);
        startActivity(intent);
        finish();
    }
}
