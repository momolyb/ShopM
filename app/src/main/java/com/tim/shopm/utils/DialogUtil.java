package com.tim.shopm.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tim.shopm.R;

import static com.tim.shopm.entity.OutOrder.TYPE_ALI;
import static com.tim.shopm.entity.OutOrder.TYPE_CASH;
import static com.tim.shopm.entity.OutOrder.TYPE_WX;

public class DialogUtil {
    public static PopupWindow showAskWindows(Context context, View parent, String title, String msgStr, String buttonEnter, String buttonCancle, boolean isOnebutton, View.OnClickListener onEnterClick, View.OnClickListener onCancleClick) {
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
        if (!TextUtils.isEmpty(buttonEnter)) {
            ((TextView) view.findViewById(R.id.btn_e)).setText(buttonEnter);
        }
        if (!TextUtils.isEmpty(buttonCancle)) {
            ((TextView) view.findViewById(R.id.btn_c)).setText(buttonCancle);
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

    public interface OnSubmit {
        void submit(String str);
    }

    public static PopupWindow showEditWindows(Context context, OnSubmit onEnterClick, View.OnClickListener onCancleClick, String hit, String title, CharSequence buttonEnterStr, CharSequence buttonCancelStr2, boolean isOnebutton, View parent) {
        PopupWindow askWindows = new PopupWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.edit_windows, null);
        view.findViewById(R.id.btn_e).setOnClickListener(v -> {
            if (onEnterClick == null) {
                return;
            }
            onEnterClick.submit(((EditText) view.findViewById(R.id.et_content)).getText().toString());
            askWindows.dismiss();
        });
        view.findViewById(R.id.btn_c).setOnClickListener(v -> {
            askWindows.dismiss();
            if (onCancleClick == null) {
                return;
            }
            onCancleClick.onClick(v);
        });
        if (!TextUtils.isEmpty(title)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        }
        if (!TextUtils.isEmpty(buttonEnterStr)) {
            ((TextView) view.findViewById(R.id.btn_e)).setText(buttonEnterStr);
        }
        if (!TextUtils.isEmpty(buttonCancelStr2)) {
            ((TextView) view.findViewById(R.id.btn_c)).setText(buttonCancelStr2);
        }
        view.findViewById(R.id.btn_c).setVisibility(isOnebutton ? View.GONE : View.VISIBLE);
        askWindows.setContentView(view);
        askWindows.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        askWindows.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        askWindows.setTouchable(true);
        askWindows.setFocusable(true);
        askWindows.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        askWindows.showAtLocation(parent, Gravity.CENTER, 0, 0);
        return askWindows;
    }

    public interface OnSelectPayMode {
        void select(int mode);
    }

    public static PopupWindow showPayWindows(Context context, OnSelectPayMode onSelect, View.OnClickListener onCancleClick, CharSequence buttonCancelStr2, View parent) {
        PopupWindow askWindows = new PopupWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.pay_windows, null);
        view.findViewById(R.id.tv_alipay).setOnClickListener(v -> {
            if (onSelect == null) {
                return;
            }
            onSelect.select(TYPE_ALI);
            askWindows.dismiss();
        });
        view.findViewById(R.id.tv_wxpay).setOnClickListener(v -> {
            if (onSelect == null) {
                return;
            }
            onSelect.select(TYPE_WX);
            askWindows.dismiss();
        });
        view.findViewById(R.id.tv_money).setOnClickListener(v -> {
            if (onSelect == null) {
                return;
            }
            onSelect.select(TYPE_CASH);
            askWindows.dismiss();
        });
        view.findViewById(R.id.btn_c).setOnClickListener(v -> {
            askWindows.dismiss();
            if (onCancleClick == null) {
                return;
            }
            onCancleClick.onClick(v);
        });
        if (!TextUtils.isEmpty(buttonCancelStr2)) {
            ((TextView) view.findViewById(R.id.btn_c)).setText(buttonCancelStr2);
        }
        askWindows.setContentView(view);
        askWindows.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        askWindows.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        askWindows.setTouchable(true);
        askWindows.setFocusable(true);
        askWindows.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        askWindows.showAtLocation(parent, Gravity.CENTER, 0, 0);
        return askWindows;
    }
}
