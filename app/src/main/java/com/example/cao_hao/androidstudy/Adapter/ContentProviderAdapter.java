package com.example.cao_hao.androidstudy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;

import java.util.List;

import com.example.cao_hao.androidstudy.javabean.User;


/**
 * Created by cao-hao on 17-8-9.
 */

public class ContentProviderAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;

    public ContentProviderAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_contentprovider_item, null);
            holder = new ViewHolder();
            holder.ageTv = (TextView) convertView.findViewById(R.id.age_tv);
            holder.nickNameTv = (TextView) convertView.findViewById(R.id.nickname_tv);
            holder.userNameTv = (TextView) convertView.findViewById(R.id.username_tv);
            holder.genderTv = (TextView) convertView.findViewById(R.id.gender_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        User u = list.get(position);
        holder.ageTv.setText(u.getAge());
        holder.genderTv.setText(u.getGender());
        holder.nickNameTv.setText(u.getNickname());
        holder.userNameTv.setText(u.getUsername());
        return convertView;
    }

    class ViewHolder {
        TextView userNameTv, nickNameTv, genderTv, ageTv;
    }
}
