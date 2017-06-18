package bzu.edu.cn.anote.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Hatsuhikari on 2017/6/16.
 */

public class NetWork {
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }else {
            Toast.makeText(context, "网络链接失败,请检查！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}