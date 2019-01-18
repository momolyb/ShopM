package com.tim.shopm.base;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tim.shopm.R;

public abstract class BaseActivity extends AppCompatActivity {
    private TitleViewHolder viewHolder;
    void initTitle() {
        viewHolder = new TitleViewHolder();
        viewHolder.mTitle.setText(getPageTitle());
        setTitle(getPageTitle());
        viewHolder.mBtnLeft.setOnClickListener(v -> onLeftClick());
        viewHolder.mBtnRight.setOnClickListener(v -> onRightClick());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initTitle();
    }

    public void enableRightButton(@DrawableRes int btnIcon) {
        viewHolder.mBtnRight.setVisibility(View.VISIBLE);
        viewHolder.mBtnRight.setImageResource(btnIcon);
    }

    public void enableLeftButton(@DrawableRes int btnIcon) {
        viewHolder.mBtnLeft.setVisibility(View.VISIBLE);
        viewHolder.mBtnLeft.setImageResource(btnIcon);
    }

    public void onRightClick() {

    }

    public void onLeftClick() {

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
