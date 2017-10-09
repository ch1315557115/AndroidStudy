package com.example.cao_hao.androidstudy.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.example.cao_hao.androidstudy.Fragment.ChatFragment;
import com.example.cao_hao.androidstudy.Fragment.ContactsFragment;
import com.example.cao_hao.androidstudy.Fragment.FoundFragment;
import com.example.cao_hao.androidstudy.R;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * http://blog.csdn.net/guolin_blog/article/details/18234477  actionBar加仿照微信
 *
 * http://www.javaapk.com/sns/qq/6046.html
 *
 * http://blog.csdn.net/wangsidadehao/article/details/50722149   android studio中导入PagerSlidingTabStrip开源库
 */

public class ActionBarActivity extends FragmentActivity{

    /**
     * 聊天界面的Fragment
     */
    private ChatFragment chatFragment;

    /**
     * 发现界面的Fragment
     */
    private FoundFragment foundFragment;

    /**
     * 通讯录界面的Fragment
     */
    private ContactsFragment contactsFragment;

    /**
     * PagerSlidingTabStrip的实例
     */
    private AdvancedPagerSlidingTabStrip tabs;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        //在API 16的模拟器上ActionBar最右边的overflow按钮不见了！那么此时我们如何查看隐藏在overflow中的Action按钮呢？其实非常简单，
        // 按一下底部Menu键，隐藏的内容就会从底部出来了，另外一种方式是通过反射让其显示
        setOverflowShowingAlways();

        //可以通过调用setDisplayHomeAsUpEnabled()方法来启用ActionBar图标导航功能 返回箭头
        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        dm = getResources().getDisplayMetrics();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        tabs = (AdvancedPagerSlidingTabStrip) findViewById(R.id.tabs);

        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        MenuItem menuItem =  menu.findItem(R.id.action_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(ActionBarActivity.this,"Expand",Toast.LENGTH_SHORT).show();
                return true;//返回true 才能展开SearhView
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(ActionBarActivity.this,"Collapse",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  android.R.id.home:
                finish();
                return true;
         /*   case R.id.action_compose:
                Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度 滑动的下滑线
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色 滑动的下滑线
        tabs.setIndicatorColor(Color.parseColor("#45c01a"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
//        tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
        //下面的一整条下滑线，用于区分Indicator高度
        //tabs.setUnderlineHeight(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = { "聊天", "发现", "通讯录" };

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (chatFragment == null) {
                        chatFragment = new ChatFragment();
                    }
                    return chatFragment;
                case 1:
                    if (foundFragment == null) {
                        foundFragment = new FoundFragment();
                    }
                    return foundFragment;
                case 2:
                    if (contactsFragment == null) {
                        contactsFragment = new ContactsFragment();
                    }
                    return contactsFragment;
                default:
                    return null;
            }
        }

    }
}
