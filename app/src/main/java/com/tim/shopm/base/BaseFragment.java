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
        viewHolder.mBtnLeft.setOnClickListener(v -> onLeftClick());
        viewHolder.mBtnRight.setOnClickListener(view -> onRightClick());
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

    public static class TitleViewHolder {
        public View rootView;
        public TextView mTitle;
        public ImageButton mBtnLeft;
        public ImageButton mBtnRight;

        public TitleViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTitle = rootView.findViewById(R.id.title);
            this.mBtnLeft = rootView.findViewById(R.id.btn_left);
            this.mBtnRight = rootView.findViewById(R.id.btn_right);
        }

    }
}
