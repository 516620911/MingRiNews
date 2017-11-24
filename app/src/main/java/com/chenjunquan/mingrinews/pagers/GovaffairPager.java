package com.chenjunquan.mingrinews.pagers;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.chenjunquan.mingrinews.activity.MainActivity;
import com.chenjunquan.mingrinews.base.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 政要
 * Created by 516620911 on 2017.11.24.
 */

public class GovaffairPager extends BasePager{
    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        tv_title.setText("政要");
        TextView textView = new TextView(mContext);
        textView.setText("政要内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        fl_content_content.addView(textView);
        MainActivity mainActivity= (MainActivity) mContext;
        mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }
}
