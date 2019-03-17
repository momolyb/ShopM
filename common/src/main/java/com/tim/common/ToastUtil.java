package com.tim.common;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ToastUtil {
    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public static Toast showDefaultShortToast(Context context, @StringRes int id) {
        return showDefaultShortToast(context, context.getResources().getString(id));
    }

    public static Toast showDefaultLongToast(Context context, @StringRes int id) {
        return showDefaultLongToast(context, context.getResources().getString(id));
    }

    public static Toast showDefaultShortToast(Context context, CharSequence string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showDefaultLongToast(Context context, CharSequence string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static Toast showShortToast(Context context, @LayoutRes int mLayoutId, @StringRes int title, @StringRes int message, @DrawableRes int image) {
        return showShortToast(context, mLayoutId, title==-1?null:context.getResources().getString(title), message==-1?null:context.getResources().getString(message), image);
    }

    public static Toast showShortToast(Context context, @LayoutRes int mLayoutId, CharSequence title, CharSequence message, @DrawableRes int image) {
        return showToast(context, mLayoutId, title, message, image, Toast.LENGTH_SHORT);
    }

    public static Toast showLongToast(Context context, @LayoutRes int mLayoutId, @StringRes int title, @StringRes int message, @DrawableRes int image) {
        return showLongToast(context, mLayoutId, title==-1?null:context.getResources().getString(title), message==-1?null:context.getResources().getString(message), image);
    }

    public static Toast showLongToast(Context context, @LayoutRes int mLayoutId, CharSequence title, CharSequence message, @DrawableRes int image) {
        return showToast(context, mLayoutId, title, message, image, Toast.LENGTH_LONG);
    }

    /**
     * @param context
     * @param mLayoutId title view R.id.titleView messageView R.id.messageView imageView R.id.imageView
     * @param title -1==null
     * @param message -1==null
     * @param image -1==null
     * @param duration
     * @return
     */
    public static Toast showToast(Context context, @LayoutRes int mLayoutId, CharSequence title, CharSequence message, @DrawableRes int image, @Duration int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        View view = LayoutInflater.from(context).inflate(mLayoutId, null);
        if (view.findViewById(R.id.titleView) != null&&title!=null) {
            ((TextView) view.findViewById(R.id.titleView)).setText(title);
        }
        if (view.findViewById(R.id.messageView) != null&&message!=null) {
            ((TextView) view.findViewById(R.id.messageView)).setText(message);
        }
        if (view.findViewById(R.id.imageView) != null&&image!=-1) {
            ((ImageView) view.findViewById(R.id.imageView)).setImageResource(image);
        }
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(view);
        toast.show();
        return toast;
    }
}
