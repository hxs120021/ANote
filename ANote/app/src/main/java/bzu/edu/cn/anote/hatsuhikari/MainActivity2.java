package bzu.edu.cn.anote.hatsuhikari;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import bzu.edu.cn.anote.HomePage;
import bzu.edu.cn.anote.R;


public class MainActivity2 extends Activity{
    WebView view;
    TextView titleTv;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp =getSharedPreferences("userinfo", MODE_PRIVATE);
        editor = sp.edit();
        String name = sp.getString("user","");
        if(name!=""){
            System.out.println(name+"FUCKYOU");

            sub(name);
        }
        else {
            if (Build.VERSION.SDK_INT >= 11) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
            }
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.login);
            view = (WebView) findViewById(R.id.web);
            view.getSettings().setDefaultTextEncodingName("utf-8");
            view.getSettings().setJavaScriptEnabled(true);
            view.getSettings().setDomStorageEnabled(true);
            view.getSettings().setAllowFileAccessFromFileURLs(true);
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            titleTv = (TextView) findViewById(R.id.text);
            view.getSettings().setAppCacheEnabled(true);
            view.addJavascriptInterface(this, "submitacc");
            view.getSettings().setLoadWithOverviewMode(true);
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            view.loadUrl("file:///android_asset/Android/login.html");
            view.setWebChromeClient(new WebChromeClient());
            view.getSettings().setUseWideViewPort(true);
            view.getSettings().setLoadWithOverviewMode(true);
            view.getSettings().setDefaultFontSize((int) 15);
            view.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                // 出现错误是   的回调
                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    // TODO Auto-generated method stub
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

            });
            view.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    // 设置标题
                    super.onReceivedTitle(view, title);
                    if (titleTv != null) {
                        titleTv.setText(title);
                    }
                }

                /**
                 * 覆盖默认的window.alert展示界面、避免title里显示为“：来自file:////”
                 */
                @Override
                public boolean onJsAlert(WebView view, String url, String message,
                                         JsResult result) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setTitle("Ning-Tips")
                            .setMessage(message)
                            .setPositiveButton("确定", null);

                    // 不需要绑定按键事件
                    // 屏蔽keycode等于84之类的按键
                    builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            return true;
                        }
                    });
                    // 禁止响应按back键的事件
                    builder.setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                    return true;
                    // return super.onJsAlert(view, url, message, result);
                }

                /**
                 * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
                 */
            /*public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {}*/
                @Override
                public void onShowCustomView(View view, CustomViewCallback callback) {
                    // TODO Auto-generated method stub
                    super.onShowCustomView(view, callback);
                }
            });
        }
    }
    //重写window.alert
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("对话框")
                .setMessage(message)
                .setPositiveButton("确定", null);
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
        return true;
        // return super.onJsAlert(view, url, message, result);
    }

    //JavaScript  -----方法
    @android.webkit.JavascriptInterface
    public void toActivity(String username) {
//        //此处应该定义常量对应，同时提供给web页面编写者
//        if(TextUtils.equals(activityName, "a")){
//            startActivity(new Intent(this,MainActivity.class));
//        }else{
//            startActivity(new Intent(this,MainActivity.class));
//        }
        editor.putString("user",username);
        editor.commit();
        Toast.makeText(this, username+"登陆成功！", Toast.LENGTH_SHORT).show();
        sp = getSharedPreferences("userInfo", 0);
        sub(username);
    }

    public void sub(String username) {
        System.out.println(username);
        Intent intent=new Intent(this,HomePage.class);
        intent.putExtra("username",username.trim());
        startActivity(intent);
        finish();
    }
}
