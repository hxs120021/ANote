package bzu.edu.cn.anote.hatsuhikari;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import bzu.edu.cn.anote.R;


/**
 * Created by Hatsuhikari on 2017/homepage/27.
 */
public class WebViewLoadWeb extends Activity {
    private String url;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将屏幕设置为全屏
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.the_guide_1);
        webView = (WebView)findViewById(R.id.wv_webview);
        url = "file:///android_asset/guide/index.html";
        loadLocalHtml(url);
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    public void loadLocalHtml(String url){
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //重写此方法，用于捕捉页面上的跳转链接
                if ("http://start/".equals(url)){
                    //在html代码中的按钮跳转地址需要同此地址一致
                    Toast.makeText(getApplicationContext(), "开始体验", Toast.LENGTH_SHORT).show();
                    JumpActivity();
                    finish();
                }
                return true;
            }
        });
        webView.loadUrl(url);
    }
    private void JumpActivity(){
        Intent intent = new Intent(WebViewLoadWeb.this, MainActivity2.class);
        startActivity(intent);
        this.finish();
    }

}
