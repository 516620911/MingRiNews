package com.chenjunquan.mingrinews.menudetailpager.tabdetailpagers;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chenjunquan.mingrinews.base.MenuDetailBasePager;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;

/**
 *
 * 页签详情页面
 * Created by 516620911 on 2017.11.25.
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean.ChildrenData childrenData;
    private TextView textView;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenData childrenData) {
        super(context);
        this.childrenData=childrenData;
    }

    @Override
    public View initView() {
        textView = new TextView(mContext);
        textView.setText("页签");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText(childrenData.getTitle());
    }
}
