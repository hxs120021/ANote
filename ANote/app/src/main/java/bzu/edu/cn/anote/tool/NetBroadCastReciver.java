package bzu.edu.cn.anote.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by 张舜 on 2017/6/15.
 */

public class NetBroadCastReciver extends BroadcastReceiver {

    /**
     * 只有当网络改变的时候才会 经过广播。
     */

    @Override
    public void onReceive(Context context, Intent intent) {

        //如果是在开启wifi连接和有网络状态下
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (NetworkInfo.State.CONNECTED == info.getState()) {
                //连接状态 处理自己的业务逻辑
                Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "网络连接失败,请检查！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
