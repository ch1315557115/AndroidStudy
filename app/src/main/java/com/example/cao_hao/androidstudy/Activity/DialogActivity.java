package com.example.cao_hao.androidstudy.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.View.CommomDialog;
import com.example.cao_hao.androidstudy.View.ShareDialog;
import com.example.cao_hao.androidstudy.utils.Utils;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtCommom;
    private Button mBtShare;
    private Button mBtPopwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mBtCommom = (Button) findViewById(R.id.bt_CommomDialog);
        mBtCommom.setOnClickListener(this);
        mBtShare = (Button) findViewById(R.id.bt_ShareDialog);
        mBtShare.setOnClickListener(this);
        mBtPopwindow = (Button) findViewById(R.id.bt_Popwindow);
        mBtPopwindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_CommomDialog:
                showCommomDialog();
                break;
            case R.id.bt_ShareDialog:
                showShaerDialog();
                break;
            case R.id.bt_Popwindow:
                showMenu();
                break;
        }
    }

    private void showCommomDialog(){
        CommomDialog.Builder builder = new CommomDialog.Builder(this)
                .setTitle("Discard changes?")
                .setContent("Do you want delete this phontos?")
                .setRoundCircle(true)
                .setPostion(Gravity.CENTER)
                /*.setContentView(R.layout.commom_test)*/
                .setPostiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("caohao", "okok");
                    }
                })
                .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("caohao", "CANCLE");

                    }
                });
        builder.show();
    }

    private void showShaerDialog(){
        new ShareDialog(DialogActivity.this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1:
                        Utils.toast(DialogActivity.this, "微信好友");
                        break;
                    case 2:
                        Utils.toast(DialogActivity.this, "朋友圈");
                        break;
                    case 3:
                        Utils.toast(DialogActivity.this, "QQ");
                        break;
                    case 4:
                        Utils.toast(DialogActivity.this, "微博");
                        break;
                }
            }
        }).show();
    }

    public void showMenu(){
        MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this, new MenuPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(PopupWindow popupWindow, int position) {

                popupWindow.dismiss();
            }
        });
        menuPopupWindow.showAsDropDown(mBtPopwindow, -200, 40);
    }
}
