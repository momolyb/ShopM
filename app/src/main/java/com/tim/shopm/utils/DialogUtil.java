package com.tim.shopm.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tim.shopm.R;

public class DialogUtil {
    public static PopupWindow showAskWindows(Context context, View parent, String title, String msgStr, String buttonStr, String buttonStr2, boolean isOnebutton, View.OnClickListener onEnterClick, View.OnClickListener onCancleClick) {
        PopupWindow askWindows = new PopupWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.ask_windows, null);
        view.findViewById(R.id.btn_e).setOnClickListener(v -> {
            if (onEnterClick == null) {
                return;
            }
            onEnterClick.onClick(v);
            askWindows.dismiss();
        });
        view.findViewById(R.id.btn_c).setOnClickListener(v -> {
            askWindows.dismiss();
            if (onCancleClick == null) {
                return;
            }
            onCancleClick.onClick(v);
        });
        TextView msg = view.findViewById(R.id.tv_msg);
        msg.setText(msgStr);
        if (!TextUtils.isEmpty(title)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        }
        if (!TextUtils.isEmpty(buttonStr)) {
            ((TextView) view.findViewById(R.id.btn_e)).setText(buttonStr);
        }
        if (!TextUtils.isEmpty(buttonStr2)) {
            ((TextView) view.findViewById(R.id.btn_c)).setText(buttonStr2);
        }
        view.findViewById(R.id.btn_c).setVisibility(isOnebutton ? View.GONE : View.VISIBLE);
        askWindows.setContentView(view);
        askWindows.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        askWindows.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        askWindows.showAtLocation(parent, Gravity.CENTER, 0, 0);
        return askWindows;
    }

    public static void showAskWindows(Context context, View decorView, String s, View.OnClickListener onClickListener, View.OnClickListener onCancleClick) {
        showAskWindows(context, decorView, null, s, null, null, false, onClickListener, onCancleClick);
    }
}
