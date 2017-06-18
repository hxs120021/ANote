package bzu.edu.cn.anote;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bzu.edu.cn.anote.adapter.CropOptionAdapter;
import bzu.edu.cn.anote.entity.CropOption;
import bzu.edu.cn.anote.hatsuhikari.MainActivity2;
import bzu.edu.cn.anote.tool.ActivityCollector;
import bzu.edu.cn.anote.tool.CheckUpdates;


public class Setting extends AppCompatActivity {
    public ImageView imageView;
    public static Uri imageUri;
    private final int OPENPICTURE = 1;
    private final int CUTPICTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        imageView = (ImageView)findViewById(R.id.image_view);
        if(imageUri != null)
            imageView.setImageURI(imageUri);
    }

    public void btnClick(View view){
        Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI ,"image/*");
        startActivityForResult(intent, OPENPICTURE);
    }

    boolean isCard(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else return false;
    }

    private void cut(Uri uri){
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        //intent.putExtra("outputFormat", "jpeg");
        startActivityForResult(intent, CUTPICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        if(requestCode == OPENPICTURE){
            if(data != null){
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                Uri uri = data.getData();
                cut(uri);
            }
        }else if(requestCode == CUTPICTURE){
            if(data != null){
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = data.getParcelableExtra("data");
                //imageView.setImageBitmap(bitmap);
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }
}