package com.tim.shopm.base;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tim.shopm.R;

import java.lang.reflect.Method;

public abstract class BaseActivity extends AppCompatActivity {
    private TitleViewHolder viewHolder;
    void initTitle() {
        viewHolder = new TitleViewHolder();
        viewHolder.mTitle.setText(getPageTitle());
        setTitle(getPageTitle());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initTitle();
    }

    public void enableRightButton(@DrawableRes int btnIcon,View.OnClickListener click) {
        viewHolder.mBtnRight.setVisibility(View.VISIBLE);
        viewHolder.mBtnRight.setImageResource(btnIcon);
        viewHolder.mBtnRight.setOnClickListener(click);
    }

    public void enableLeftButton(@DrawableRes int btnIcon,View.OnClickListener click) {
        viewHolder.mBtnLeft.setVisibility(View.VISIBLE);
        viewHolder.mBtnLeft.setImageResource(btnIcon);
        viewHolder.mBtnLeft.setOnClickListener(click);
    }

    protected abstract String getPageTitle();

    public class TitleViewHolder {
        public TextView mTitle;
        public ImageButton mBtnLeft;
        public ImageButton mBtnRight;

        public TitleViewHolder() {
            this.mTitle = findViewById(R.id.title);
            this.mBtnLeft = findViewById(R.id.btn_left);
            this.mBtnRight = findViewById(R.id.btn_right);
        }

    }
}
