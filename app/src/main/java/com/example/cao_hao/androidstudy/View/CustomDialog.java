package com.example.cao_hao.androidstudy.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;

/**
 * Created by cao-hao on 17-8-28.
 */

public  class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }



  /*  public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context);
    }


    public static class Builder {

        private Context context;

        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralButtonText;
        private View contentView;
        private View view;
        private Drawable icon;
        private Bitmap iconBitmap;
        private int  iconResId = -1;
        private DialogInterface.OnClickListener
                positiveButtonClickListener,
                negativeButtonClickListener,
                neutralButtonClickListener;

        public Builder(Context context) {
            this.context = context;

            title = null;
            message = null;
            positiveButtonText = null;
            negativeButtonText = null;
            neutralButtonText = null;
            contentView = null;
            view = null;
            icon = null;
            iconBitmap = null;
            iconResId = -1;

            positiveButtonClickListener = null;
            negativeButtonClickListener = null;
            neutralButtonClickListener = null;

        }


        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }


        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }


        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }


        public Builder setView(View view) {
            this.view = view;
            return this;
        }
        public Builder setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }
        public Builder setIcon(Bitmap iconBitmap) {
            this.iconBitmap = iconBitmap;
            return this;
        }
        public Builder setIcon(int iconResId) {
            this.iconResId = iconResId;
            return this;
        }



        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String negativeButtonText,
                                        DialogInterface.OnClickListener listener) {
            this.neutralButtonText = negativeButtonText;
            this.neutralButtonClickListener = listener;
            return this;
        }


        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context);
            View layout = inflater.inflate(R.layout.dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);


            //set icon
            *//*if(icon!=null){
                ((ImageView)(layout.findViewById(R.id.d_icon))).setBackgroundDrawable(icon);
            }
            if(iconBitmap!=null){
                ((ImageView)(layout.findViewById(R.id.d_icon))).setImageBitmap(iconBitmap);
            }
            if(iconResId!=-1){
                ((ImageView)(layout.findViewById(R.id.d_icon))).setBackgroundResource(iconResId);
            }*//*
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    dialog.cancel();
                                }
                            });
                }else{
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    dialog.cancel();

                                }
                            });

                }



            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                    dialog.cancel();

                                }
                            });
                }else{
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    dialog.cancel();

                                }
                            });

                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }

            // set the neutralButton button
       *//*     if ( neutralButtonText != null) {
                ((Button) layout.findViewById(R.id.neutralButton))
                        .setText(neutralButtonText);
                if (neutralButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.neutralButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    neutralButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                    dialog.cancel();

                                }
                            });
                }else{
                    ((Button) layout.findViewById(R.id.neutralButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    dialog.cancel();

                                }
                            });

                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.neutralButton).setVisibility(
                        View.GONE);
            }
*//*







            // set the content message
            if (message != null) {
                ((LinearLayout)layout.findViewById(R.id.content)).setVisibility(View.VISIBLE);
                ( (TextView)( layout.findViewById(R.id.dialog_message))).setText(message);
                ((TextView)layout.findViewById(R.id.dialog_message)).setVisibility(View.VISIBLE);
            } else{
                ((TextView)layout.findViewById(R.id.dialog_message)).setVisibility(View.GONE);
            }
            //set view
            if(view!=null){
                ((LinearLayout)layout.findViewById(R.id.content)).setVisibility(View.VISIBLE);
                ((LinearLayout)layout.findViewById(R.id.content)).addView(view);
            }else if(message ==null){
                ((LinearLayout)layout.findViewById(R.id.content)).setVisibility(View.GONE);
            }

            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView,
                                new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }



    }
*/
}