package com.chenjunquan.mingrinews.menudetailpager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chenjunquan.mingrinews.base.MenuDetailBasePager;

/**
 * 互动详细页面
 * Created by 516620911 on 2017.11.25.
 */

public class InteractMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public InteractMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(mContext);
        textView.setText("互动详细");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    public void initData() {
        textView.setText("互动详细页面");
    }
}
