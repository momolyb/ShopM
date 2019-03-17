package com.tim.shopm.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.common.ToastUtil;
import com.tim.shopm.R;

import java.util.ArrayList;

public class PasswordView extends LinearLayout {
    private StringBuffer pass;
    private ArrayList<TextView> textViewArrayList;
    private OnEnterClick onEnterClick;
    interface OnEnterClick{
        void onEnter(String pass);
    }
    public PasswordView(Context context) {
        super(context);
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View inflate = inflate(context, R.layout.view_password, this);
        init(inflate);
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnEnterClick(OnEnterClick onEnterClick) {
        this.onEnterClick = onEnterClick;
    }

    void init(View view) {
        pass = new StringBuffer();
        textViewArrayList = new ArrayList<>();
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mNum0.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum1.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum2.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum3.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum4.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum5.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum6.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum7.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum8.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNum9.setOnClickListener(view1 -> add(((TextView) view1).getText().toString()));
        viewHolder.mNumB.setOnClickListener(view1 -> del());
        viewHolder.mNumE.setOnClickListener(view1 -> submit());
        textViewArrayList.add(viewHolder.mTvPass1);
        textViewArrayList.add(viewHolder.mTvPass2);
        textViewArrayList.add(viewHolder.mTvPass3);
        textViewArrayList.add(viewHolder.mTvPass4);
    }

    private void add(String num) {
        if (pass.length() < 4) {
            pass.append(num);
        }
        refreshView();
    }

    private void del() {
        if (pass.length() > 0) {
            pass.deleteCharAt(pass.length() - 1);
        }
        refreshView();
    }

    private void submit() {
        if (pass.length() == 4) {
            if (onEnterClick!=null){
                onEnterClick.onEnter(pass.toString());
            }
        }else {
            ToastUtil.showDefaultShortToast(getContext(),"请输入4位密码");
        }
    }

    private void refreshView() {
        for (int i = 0; i < textViewArrayList.size(); i++) {
            if (pass.length() > i)
                textViewArrayList.get(i).setText(R.string.pass);
            else
                textViewArrayList.get(i).setText("");
        }
    }

    class ViewHolder {
        View rootView;
        TextView mTvPass1;
        TextView mTvPass2;
        TextView mTvPass3;
        TextView mTvPass4;
        TextView mNum1;
        TextView mNum2;
        TextView mNum3;
        TextView mNum4;
        TextView mNum5;
        TextView mNum6;
        TextView mNum7;
        TextView mNum8;
        TextView mNum9;
        TextView mNumE;
        TextView mNum0;
        TextView mNumB;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvPass1 = rootView.findViewById(R.id.tv_pass_1);
            this.mTvPass2 = rootView.findViewById(R.id.tv_pass_2);
            this.mTvPass3 = rootView.findViewById(R.id.tv_pass_3);
            this.mTvPass4 = rootView.findViewById(R.id.tv_pass_4);
            this.mNum1 = rootView.findViewById(R.id.num_1);
            this.mNum2 = rootView.findViewById(R.id.num_2);
            this.mNum3 = rootView.findViewById(R.id.num_3);
            this.mNum4 = rootView.findViewById(R.id.num_4);
            this.mNum5 = rootView.findViewById(R.id.num_5);
            this.mNum6 = rootView.findViewById(R.id.num_6);
            this.mNum7 = rootView.findViewById(R.id.num_7);
            this.mNum8 = rootView.findViewById(R.id.num_8);
            this.mNum9 = rootView.findViewById(R.id.num_9);
            this.mNumE = rootView.findViewById(R.id.num_e);
            this.mNum0 = rootView.findViewById(R.id.num_0);
            this.mNumB = rootView.findViewById(R.id.num_b);
        }

    }
}
