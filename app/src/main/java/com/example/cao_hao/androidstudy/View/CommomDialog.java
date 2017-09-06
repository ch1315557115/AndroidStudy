package com.example.cao_hao.androidstudy.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cao_hao.androidstudy.R;


public class CommomDialog extends Dialog {


    public CommomDialog(Context context) {
        super(context);
    }


    public CommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
    }


    protected CommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);*/
        // 设置宽度为屏宽, 靠近屏幕底部。

    }

    public static class Builder {
        private Context mContext;
        private LinearLayout lvRootView;
        private TextView contentTxt;
        private TextView titleTxt;
        private TextView submitTxt;
        private TextView cancelTxt;
        private int layoutResID = 0;

        private String title;
        private String content;
        private String positiveName;
        private String negativeName;

        private CommomDialog dialog;

        private OnClickListener
                positiveButtonClickListener,
                negativeButtonClickListener;

        private int postion;

        private int width = 0;
        private int height = 0;
        private boolean isCircle = false;

        public Builder(Context context) {
            mContext = context;
            titleTxt = null;
            contentTxt = null;
            submitTxt = null;
            cancelTxt = null;

            title = null;
            content = null;
            negativeName = null;
            positiveName = null;


            positiveButtonClickListener = null;
            negativeButtonClickListener = null;

            /*width = mContext.getResources().getDimensionPixelSize(
                    R.dimen.dialog_width);
            height = mContext.getResources().getDimensionPixelSize(
                    R.dimen.dialog_height);*/


        }

        private CommomDialog create() {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            dialog = new CommomDialog(mContext);
            View layout = inflater.inflate(R.layout.dialog_commom, null);
            dialog.setContentView(layout);
            /*dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));*/

            initView(layout);

            if (width > 0 && height > 0) {
                setDialogDimension(width, height);
            }
            return dialog;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButton(String name) {
            this.positiveName = name;
            return this;
        }

        public Builder setNegativeButton(String name) {
            this.negativeName = name;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setContentView(int layoutResID) {
            this.layoutResID = layoutResID;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeName = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setPostiveButton(String negativeButtonText,
                                        OnClickListener listener) {
            this.positiveName = negativeButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setDialogDimension(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setPostion(int postion) {
            this.postion = postion;
            return this;
        }

        public Builder setRoundCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        private void initView(View view) {
            lvRootView = (LinearLayout) view.findViewById(R.id.ll_root);
            titleTxt = (TextView) view.findViewById(R.id.tv_title);
            contentTxt = (TextView) view.findViewById(R.id.content);
            cancelTxt = (TextView) view.findViewById(R.id.cancel);
            submitTxt = (TextView) view.findViewById(R.id.ok);

            if (!TextUtils.isEmpty(title)) {
                titleTxt.setText(title);
            }
            if (!TextUtils.isEmpty(positiveName)) {
                submitTxt.setText(positiveName);
            }
            if (!TextUtils.isEmpty(negativeName)) {
                cancelTxt.setText(negativeName);
            }

            if (!TextUtils.isEmpty(content)) {
                contentTxt.setText(content);
            }
            initListener();
        }

        public void initListener() {
            if (null != positiveButtonClickListener) {
                submitTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveButtonClickListener.onClick(
                                dialog,
                                DialogInterface.BUTTON_POSITIVE);
                        dialog.cancel();

                    }
                });
            }

            if (null != negativeButtonClickListener) {
                cancelTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeButtonClickListener.onClick(
                                dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                        dialog.cancel();
                    }
                });
            }
        }

        public void show() {
            Dialog dialog = create();
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            if (layoutResID != 0) {
                dialog.setContentView(layoutResID);
            }
            if (isCircle) {
                window.setBackgroundDrawableResource(R.drawable.bg_round_white);
            }
            dialog.show();

            //dialog宽高设置要放在 show之后
            if (width > 0 && height > 0) {
               /* lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;*/
                lp.width = width;
                lp.height = height;
            }
            if (postion > 0) {
                lp.gravity = postion; // 显示位置
            }
            window.setAttributes(lp);
        }
    }
}
