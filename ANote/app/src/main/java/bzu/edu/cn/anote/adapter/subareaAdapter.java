package bzu.edu.cn.anote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bzu.edu.cn.anote.R;
import bzu.edu.cn.anote.entity.Subarea;

/**
 * Created by 李小宁 on 2017/homepage/29.
 */

public class subareaAdapter extends ArrayAdapter<Subarea> {
    private int resourceId;
    public subareaAdapter(Context context, int resource, List<Subarea> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subarea subarea=getItem(position);
        TextView whoSubarea;
        View view;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            whoSubarea= (TextView) view.findViewById(R.id.tv_whoSubarea);
            view.setTag(whoSubarea);
        }else{
            view=convertView;
            whoSubarea= (TextView) view.getTag();
        }
        whoSubarea.setText(subarea.getSubarea());
        return view;
    }
}
