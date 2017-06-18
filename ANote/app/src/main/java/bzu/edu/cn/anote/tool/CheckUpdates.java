package bzu.edu.cn.anote.tool;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import bzu.edu.cn.anote.R;

/**
 * Created by Hatsuhikari on 2017/6/18.
 */

public class CheckUpdates extends AsyncTask<Void, Void, String> {

    private static final String TAG = "CheckUpdates";
    private ProgressDialog dialog;
    private Context mContext;
    private boolean mShowProgressDialog;
    private static final String VERSION_INFO_URL = "http://10.3.21.199:8080/DonServlet?filename=update.json";
    public CheckUpdates(Context context, boolean showProgressDialog) {
        this.mContext = context;
        this.mShowProgressDialog = showProgressDialog;
    }

    //初始化显示Dialog
    protected void onPreExecute() {
                if (mShowProgressDialog) {
                    dialog = new ProgressDialog(mContext);
                    dialog.setMessage(mContext.getString(R.string.check_new_version));
                    dialog.show();
                }
    }
    @Override
    protected String doInBackground(Void... params) {
        return getVersionInfo(VERSION_INFO_URL);
    }
    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        System.out.println(result+"!!!");
        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }
    public String getVersionInfo(String urlStr){
        HttpURLConnection uRLConnection = null;
        InputStream is = null;
        BufferedReader buffer = null;
        String result = null;
        try {
            URL url = new URL(urlStr);
            if (url.getProtocol().toLowerCase().equals("https")) {
                uRLConnection = (HttpURLConnection) url.openConnection();
            }else {
                uRLConnection = (HttpURLConnection) url.openConnection();
            }
            uRLConnection.setRequestMethod("POST");
            is = uRLConnection.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            result = strBuilder.toString();
            System.out.println(result+"!!!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "http post error");
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException ignored) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {

                }
            }
            if (uRLConnection != null) {
                uRLConnection.disconnect();
            }
        }
        return result;
    }

    private void parseJson(String result) {
        try {
            JSONObject obj = JSONObject.fromObject(result);
            String apkUrl = obj.getString("url");                 //APK下载路径
            String updateMessage = obj.getString("updateMessage");//版本更新说明
            int apkCode = obj.getInt("versionCode");              //新版APK对于的版本号
            //取得已经安装在手机的APP的版本号 versionCode
            int versionCode = getCurrentVersionCode();

            //对比版本号判断是否需要更新
            if (apkCode > versionCode) {

                showDialog(updateMessage, apkUrl);

            } else if (mShowProgressDialog) {
                Toast.makeText(mContext,mContext.getString(R.string.there_no_new_version), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "parse json error");
        }
    }
    public int getCurrentVersionCode() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return 0;
    }
    public void showDialog(String content, final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_choose_update_title);
        builder.setMessage(Html.fromHtml(content))
                .setPositiveButton(R.string.dialog_btn_confirm_download, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //下载apk文件
                        goToDownloadApk(downloadUrl);
                    }
                })
                .setNegativeButton(R.string.dialog_btn_cancel_download, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        //点击对话框外面,对话框不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 用intent启用DownloadService服务去下载AKP文件
     * @param downloadUrl
     */
    private void goToDownloadApk(String downloadUrl) {
        Intent intent = new Intent(mContext, DownloadApkService.class);
        intent.putExtra("apkUrl", downloadUrl);
        mContext.startService(intent);
    }
}
