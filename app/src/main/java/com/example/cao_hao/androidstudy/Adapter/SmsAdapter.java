package com.example.cao_hao.androidstudy.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;


import java.util.ArrayList;
import java.util.List;

import com.example.cao_hao.androidstudy.bean.SmsInfo;

/**
 * Created by cao-hao on 17-8-7.
 */

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<SmsInfo> mDatas = new ArrayList<SmsInfo>();

    public SmsAdapter(Context context, List<SmsInfo> datas){
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public void refresh(List<SmsInfo> data){
        if (null != mDatas){
            mDatas =  null;
            mDatas = data;
            /*mDatas.clear();
            mDatas = data;  这样处理mData拿不到数据 目前不清楚原因
            * */
          /*  mDatas.clear(); //也无法成功获取到数据
            mDatas.addAll(data);*/

        }
       notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_sms_item,null);
        ViewHolder holder = new ViewHolder(view);
        holder.phoneText = (TextView) view.findViewById(R.id.tv_Sms_name);
        holder.bodyText = (TextView) view.findViewById(R.id.tv_Sms_body);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.phoneText.setText("号码："+mDatas.get(position).getPhoneNumber());
        holder.bodyText.setText("内容："+mDatas.get(position).getSmsbody());
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
        private TextView phoneText;
        private TextView bodyText;
    }
}
