package com.example.cao_hao.androidstudy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.service.FloatService;


public class PoPupActivity extends Activity {

    private PopupWindow mPopupWindow;

    private TextView mTextView;
    private View mTabContainer;

    private CheckBox mCheckBackground;
    private CheckBox mCheckFocusable;
    private CheckBox mCheckTouchable;

    private TextView mPopupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_pup);

        initViews();
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.text);
        mCheckBackground = (CheckBox) findViewById(R.id.check_background);
        mCheckFocusable = (CheckBox) findViewById(R.id.check_focusable);
        mCheckTouchable = (CheckBox) findViewById(R.id.check_outsideTouchable);
        mTabContainer = findViewById(R.id.tab_container);
    }


    /**
     * 基本用法
     */
    private void showPopupWindowBasic() {
        View rootView = getLayoutInflater().inflate(R.layout.popup_window, null);
        mPopupText = (TextView) rootView.findViewById(R.id.popup_text);
        mPopupText.setText("Basic");

        mPopupWindow = new PopupWindow(rootView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAsDropDown(mTextView);
    }

    /**
     * 优化
     */
    private void showPopupWindowOptimized() {
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
        mPopupWindow.showAsDropDown(mTextView);
    }


    /**
     * 事件处理
     */
    private void showPopupWindowEvent() {
        dismissPopupWindow();

        View rootView = getLayoutInflater().inflate(R.layout.popup_window, null);
        mPopupText = (TextView) rootView.findViewById(R.id.popup_text);
        Button popupButton = (Button) rootView.findViewById(R.id.popup_button);
        ListView popupList = (ListView) rootView.findViewById(R.id.popup_list);

        mPopupText.setText("Event");
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PoPupActivity.this, "click button", Toast.LENGTH_SHORT).show();
            }
        });
        popupList.setAdapter(mAdapter);
        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PoPupActivity.this, "click item " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mPopupWindow = new PopupWindow(rootView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popup_window);

        if (mCheckBackground.isChecked()) {
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mPopupWindow.setOutsideTouchable(mCheckTouchable.isChecked());
        mPopupWindow.setFocusable(mCheckFocusable.isChecked());

        mPopupWindow.update();
        mPopupWindow.showAsDropDown(mTextView);
    }

    /**
     * 隐藏
     */
    private void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
      /*  *
         * 在使用PopupWindow时，要注意dismiss方法的调用。当Activity被关闭时，
         * 如果PopupWindow仍在显示，此时就会抛出Window Leaked异常，原因是PopupWindow附属
                * 于Activity的WindowManager，而Activity被关闭了，窗体也不再存在。所以应该覆写onStop方法如下，
         * 确保在Activity退出前先关闭PopupWindow。*/

       /* mPopupWindow.dismiss();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item0:
                dismissPopupWindow();
                return true;
            case R.id.item1:
                showPopupWindowBasic();
                return true;
            case R.id.item2:
                showPopupWindowOptimized();
                return true;
            case R.id.item3:
                showPopupWindowEvent();
                return true;
            case R.id.item4:
                new ArrowPopupBubble(this).showAtBottom(
                        mTextView, ArrowPopupBubble.Position.LEFT,
                        20, mTabContainer.getWidth() / 6, mTabContainer.getHeight());
                return true;
            case R.id.item5:
                startService(new Intent(this, FloatService.class));
                return true;
            case R.id.item6:
                stopService(new Intent(this, FloatService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ListAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView t = new TextView(PoPupActivity.this);
            t.setText(String.valueOf(position));
            return t;
        }
    };
}
