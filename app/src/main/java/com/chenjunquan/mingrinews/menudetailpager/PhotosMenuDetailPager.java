package com.chenjunquan.mingrinews.menudetailpager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chenjunquan.mingrinews.base.MenuDetailBasePager;

/**
 * 图组详细页面
 * Created by 516620911 on 2017.11.25.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(mContext);
        textView.setText("图组详细");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    public void initData() {
        textView.setText("图组详细页面");
    }
}
