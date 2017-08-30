package com.example.cao_hao.androidstudy.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.cao_hao.androidstudy.R;

import java.util.List;

import com.example.cao_hao.androidstudy.bean.SmsInfo;


/**
 * Created by cao-hao on 17-7-25.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<SmsInfo> mDatas;

    public MyRecyclerViewAdapter(Context context, List<SmsInfo> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_sms_item, null);
        ViewHolder holder = new ViewHolder(view);
        holder.phoneNumber = (TextView) view.findViewById(R.id.tv_Sms_name);
        holder.body = (TextView) view.findViewById(R.id.tv_Sms_body);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.phoneNumber.setText(mDatas.get(position).getPhoneNumber());
        holder.body.setText(mDatas.get(position).getSmsbody());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView phoneNumber;
        TextView body;
    }
}


