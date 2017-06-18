package bzu.edu.cn.anote.pain;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import bzu.edu.cn.anote.R;

/**
 * Created by Hatsuhikari on 2017/6/17.
 */

public class PainActivity extends AppCompatActivity {
    //拷贝图
    private Bitmap bitcopy;
    private ImageView mIVSign;
    private TextView mTVSign;
    private Bitmap mSignBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain);
        Button save = (Button) findViewById(R.id.save_img);
        mIVSign = (ImageView) findViewById(R.id.iv_sign);
        mTVSign = (TextView) findViewById(R.id.tv_sign);
        mIVSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WritePadDialog mWritePadDialog = new WritePadDialog(
                        PainActivity.this, new WriteDialogListener() {

                    public void onPaintDone(Object object) {
                        mSignBitmap = (Bitmap) object;
                        createSignFile();
                        mIVSign.setImageBitmap(mSignBitmap);
                        mTVSign.setVisibility(View.GONE);
                    }
                });
                mWritePadDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIVSign.buildDrawingCache(true);
                mIVSign.buildDrawingCache();
                Bitmap bitmap = mIVSign.getDrawingCache();
                saveBitmapFile(bitmap);
                mIVSign.setDrawingCacheEnabled(false);
            }
        });
    }

    public void saveBitmapFile(Bitmap bitmap) {

        File temp = new File("/sdcard/1delete/");//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        ////重复保存时，覆盖原同名图片
        File file = new File("/sdcard/1delete/1.jpg");//将要保存图片的路径和图片名称
        //    File file =  new File("/sdcard/1delete/1.png");/////延时较长
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //创建签名文件
    private void createSignFile() {
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        String path = null;
        File file = null;
        try {
            path = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg";
            file = new File(path);
            fos = new FileOutputStream(file);
            baos = new ByteArrayOutputStream();
            //如果设置成Bitmap.compress(CompressFormat.JPEG, 100, fos) 图片的背景都是黑色的
            mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            byte[] b = baos.toByteArray();
            if (b != null) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
