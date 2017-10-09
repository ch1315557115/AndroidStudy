package com.example.cao_hao.androidstudy.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;

import java.util.ArrayList;

import com.example.cao_hao.androidstudy.View.CustomDialog;
import com.example.cao_hao.androidstudy.utils.LogUtils;

public class MainActivity extends Activity{

    private ListView mLvList;
    private ArrayList<String> mArrayList = new ArrayList<String>();

    private PopupWindow mPopupWindow;
    private TextView mIntrodText;
    private TextView mPopupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mIntrodText = (TextView) findViewById(R.id.tv_introduction);

        ininData();

        mLvList = (ListView) findViewById(R.id.lv_main_list);
        mLvList.setAdapter(new MyAdapter());

        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(position);
            }
        });

        mLvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("onLongClick ");
                showPopupWindowOptimized(view);
                return true;
            }
        });
    }

    private void ininData() {
        mArrayList.add("开启HanderActivity");
        mArrayList.add("短信");
        mArrayList.add("数据库");
        mArrayList.add("内容提供者读取系统联系人");
        mArrayList.add("自定义内容提供者");
        mArrayList.add("文件读写操作");
        mArrayList.add("popupwindow");
        mArrayList.add("自定义在屏幕内拖动控件");
        mArrayList.add("禁用底部返回键、菜单键、任务键");
        mArrayList.add("AsyncTask");
        mArrayList.add("Widget");
        mArrayList.add("CursorAdapte");
        mArrayList.add("SurfaceView");
        mArrayList.add("aidl");
        mArrayList.add("Bundle");
        mArrayList.add("DialogActivity");
        mArrayList.add("SearchView");
        mArrayList.add("Bitmap绘制相关");
        mArrayList.add("动画");
        mArrayList.add("TestActivity");
        mArrayList.add("图片跟随手指缩缩放");
        mArrayList.add("矩阵变换");
        mArrayList.add("ActionBarActivity");


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onClick(int postion) {
        switch (postion) {
            case 0:
//                startActivity(new Intent(MainActivity.this, HandlerTestAcitvity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, SmsActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, DbActivity.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                break;
            case 4:
                startActivity(new Intent(MainActivity.this, ContentProviderActivity.class));
                break;
            case 5:
                startActivity(new Intent(MainActivity.this, FileActivity.class));
                break;
            case 6:
                startActivity(new Intent(MainActivity.this, PoPupActivity.class));
                break;
            case 7:
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
                break;
            case 8:
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                break;
            case 9:
                startActivity(new Intent(MainActivity.this, AsyncTaskActivity.class));
                break;
            case 10:
                startActivity(new Intent(MainActivity.this, AsyncTaskActivity.class));
                break;
            case 11:
                startActivity(new Intent(MainActivity.this, CursorAdapterActivity.class));
                break;
            case 12:
                startActivity(new Intent(MainActivity.this, SurfaceActivity.class));
                break;
            case 13:
                startActivity(new Intent(MainActivity.this, AIDLActivity.class));
                break;
            case 14:
                startActivity(new Intent(MainActivity.this, BundleActivity.class));
                break;
            case 15:
                startActivity(new Intent(MainActivity.this, DialogActivity.class));
                break;
            case 16:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case 17:
                startActivity(new Intent(MainActivity.this, DrawBitmapActivity.class));
                break;
            case 18:
                startActivity(new Intent(MainActivity.this, AnimationActivity.class));
                break;
            case 19:
                startActivity(new Intent(MainActivity.this,TestActivity.class));
                break;
            case 20:
                startActivity(new Intent(MainActivity.this,ScaleImageViewActivity.class));
                break;
            case 21:
                startActivity(new Intent(MainActivity.this,MatrixActivity.class));
                break;
            case 22:
                startActivity(new Intent(MainActivity.this,ActionBarActivity.class));
                break;
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);


        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 1.     在Android开发中会发现，有时listView和GridView的item顶层布局不起作用，即不能设置高度和宽度
         * 原因是当用自定义的adapter时，如果使用convertView= mInflater.inflate(R.layout.material_grid_item, null)
         * 方法就不会起作用，这个 方法的第二个参数是父View，传入为空，所以没有加载顶层布局，此时如果使用
         * convertView= mInflater.inflate(R.layout.material_grid_item, parent,false);传入parent设置的高度
         * 和宽度就会起作用了。
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.activity_main_list_item, parent, false);
                holder.tvText = (TextView) convertView.findViewById(R.id.tv_text);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvText.setText(mArrayList.get(position));
            return convertView;
        }


        public class ViewHolder {
            public TextView tvText;
        }
    }

    /**
     * 优化
     */
    private void showPopupWindowOptimized(View view) {
        // 如果正在显示则不处理
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        // 如果没有初始化则初始化
        if (mPopupWindow == null) {
            View rootView = getLayoutInflater().inflate(R.layout.popup_window, null);
            mPopupText = (TextView) rootView.findViewById(R.id.popup_text);

            mPopupWindow = new PopupWindow(rootView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 设置文本
        mPopupText.setText("Optimized");
        // 刷新内容
        mPopupWindow.update();

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));

        //PopupWindow以外的区域是否可点击，即如果点击PopupWindow以外的区域，PopupWindow是否会消失。
        mPopupWindow.setOutsideTouchable(true);
        // 显示
        mPopupWindow.showAsDropDown(view);
    }

    //122

}
