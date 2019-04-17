package com.tim.shopm.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tim.shopm.R;

import org.w3c.dom.Text;

public abstract class BaseFragment extends Fragment {
    private TitleViewHolder viewHolder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitle();
    }

    void initTitle() {
        viewHolder = new TitleViewHolder(getView());
        viewHolder.mTitle.setText(getPageTitle());
        getActivity().setTitle(getPageTitle());
    }

    public void enableRightButton(String btnIcon,View.OnClickListener click) {
        viewHolder.mBtnRight.setVisibility(View.VISIBLE);
        viewHolder.mBtnRight.setText(btnIcon);
        viewHolder.mBtnRight.setOnClickListener(click);
    }
    public void enableLeftButton(String btnIcon,View.OnClickListener click) {
        viewHolder.mBtnLeft.setVisibility(View.VISIBLE);
        viewHolder.mBtnLeft.setText(btnIcon);
        viewHolder.mBtnLeft.setOnClickListener(click);
    }

    protected abstract String getPageTitle();

    public static class TitleViewHolder {
        public View rootView;
        public TextView mTitle;
        public TextView mBtnLeft;
        public TextView mBtnRight;

        public TitleViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTitle = rootView.findViewById(R.id.title);
            this.mBtnLeft = rootView.findViewById(R.id.btn_left);
            this.mBtnRight = rootView.findViewById(R.id.btn_right);
        }

    }
}
