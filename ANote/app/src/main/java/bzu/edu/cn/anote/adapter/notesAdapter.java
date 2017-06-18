package bzu.edu.cn.anote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bzu.edu.cn.anote.R;
import bzu.edu.cn.anote.entity.Notes;

/**
 * Created by 李小宁 on 2017/homepage/25.
 */

public class notesAdapter extends ArrayAdapter<Notes> {
    private TextView textView;
    private int resourceId;
    public notesAdapter(Context context, int resource, List<Notes> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notes notes=getItem(position);
        View view=null;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);
            textView= (TextView) view.findViewById(R.id.tv_title);
            view.setTag(textView);
        }else{
            view=convertView;
            textView= (TextView) view.getTag();
        }
        if(TextUtils.isEmpty(notes.getTitle())){
            textView.setText("无标题页");
        }else{
            textView.setText(notes.getTitle());
        }
        notifyDataSetChanged();
        return view;
    }
}
