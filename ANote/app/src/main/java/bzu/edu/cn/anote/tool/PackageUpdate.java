package bzu.edu.cn.anote.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Hatsuhikari on 2017/6/17.
 */
/**
 * 检查更新工具类
 */
public class PackageUpdate {
    public String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
            System.out.println(versionCode+"这是版本号");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
