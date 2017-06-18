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
import bzu.edu.cn.anote.entity.Notesname;

/**
 * Created by 李小宁 on 2017/homepage/28.
 */

public class NotesNameAdapter extends ArrayAdapter<Notesname> {
    int resourceId;
    public NotesNameAdapter(Context context, int resource, List<Notesname> objects) {
        super(context, resource,objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notesname notesname=getItem(position);
        View view;
        TextView tvwhonotes;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);
            tvwhonotes= (TextView) view.findViewById(R.id.tv_whoNotes);
            view.setTag(tvwhonotes);
        }else{
            view=convertView;
            tvwhonotes= (TextView) view.getTag();
        }
        tvwhonotes.setText(notesname.getNotesname());
        return view;
    }
}
