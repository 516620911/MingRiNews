package com.chenjunquan.mingrinews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chenjunquan.mingrinews.R;

/**
 * 基类公共类
 * HomePagerNewsCenterPagerSmartServicePager..
 * Created by JunquanChen on 2017/11/24.
 */

public class BasePager {
    public final Context mContext;
    /**
     * 视图代表各个不同的页面
     */
    public View mRootView;

    public TextView tv_title;
    public ImageButton iv_menu;
    public FrameLayout fl_content_content;

    public BasePager(Context context) {
        mContext = context;
        //构造方法一执行就初始化视图
        mRootView = initView();
    }

    private View initView() {
        //基类的页面
        View view = View.inflate(mContext, R.layout.base_pager, null);
        tv_title = view.findViewById(R.id.tv_title);
        iv_menu = view.findViewById(R.id.ib_menu);
        fl_content_content = view.findViewById(R.id.fl_content_content);

        return view;
    }

    /**
     * 初始化数据 跟数据相关的操作比如联网请求数据
     * 因为此操作(网络请求)是耗时操作,所以我们单独封装此方法以便控制操作的时机
     */
    public void initData() {

    }

}
