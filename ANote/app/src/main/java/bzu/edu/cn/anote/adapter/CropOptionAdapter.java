package bzu.edu.cn.anote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bzu.edu.cn.anote.R;
import bzu.edu.cn.anote.entity.CropOption;

/**
 * Created by Hatsuhikari on 2017/6/15.
 */

public class CropOptionAdapter extends ArrayAdapter<CropOption> {
    private ArrayList<CropOption> mOptions;
    private LayoutInflater mInflater;
    public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
        super(context, R.layout.crop_selector, options);
        mOptions = options;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.crop_selector, null);

        CropOption item = mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(R.id.iv_icon))
                    .setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(R.id.tv_name))
                    .setText(item.title);
            return convertView;
        }

        return null;
    }
}
