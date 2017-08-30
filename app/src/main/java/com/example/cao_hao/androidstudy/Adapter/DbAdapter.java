package com.example.cao_hao.androidstudy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;

import java.util.List;

import com.example.cao_hao.androidstudy.bean.Person;

/**
 * Created by cao-hao on 17-8-8.
 */

public class DbAdapter extends BaseAdapter {
    private List<Person> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public DbAdapter(Context context, List<Person> list){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    //刷新适配器
    public void refresh(List<Person> list){
        if(null != list){
            list = null;
            this.list = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person p = list.get(position);
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.activity_db_result_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.textView1);
            holder.sex = (TextView) convertView.findViewById(R.id.textView2);
            holder.number = (TextView) convertView.findViewById(R.id.textView3);
            holder.desc = (TextView) convertView.findViewById(R.id.textView4);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(p.getName());
        holder.sex.setText(p.getSex());
        holder.number.setText(p.getNumber());
        holder.desc.setText(p.getDesc());

        return convertView;
    }


    public class ViewHolder {
        public TextView name;
        public TextView sex;
        public TextView number;
        public TextView desc;
        public TextView id;
    }
}
