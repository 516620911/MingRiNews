package com.chenjunquan.mingrinews.base;

import android.content.Context;
import android.view.View;

/**
 * 左侧栏的详情页面
 * Created by 516620911 on 2017.11.25.
 */

public abstract class MenuDetailBasePager {
    public final Context mContext;
    /**
     * 左侧菜单栏各个详情页面的视图
     */
    public View rootView;

    public MenuDetailBasePager(Context context) {
        mContext = context;
        rootView = initView();
    }

    /**
     * @return 初始化不同页面的视图
     */
    public abstract View initView();

    /**
     * 初始化数据等耗时操作
     */
    public void initData(){

    }

}
