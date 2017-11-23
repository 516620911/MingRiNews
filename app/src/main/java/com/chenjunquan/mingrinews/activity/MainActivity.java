package com.chenjunquan.mingrinews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.fragment.ContentFragment;
import com.chenjunquan.mingrinews.fragment.LeftmenuFragment;
import com.chenjunquan.mingrinews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * SlidingMenu的使用
 */
public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置内容主页面
        setContentView(R.layout.activity_main);
        //设置左侧抽屉菜单
        setBehindContentView(R.layout.activity_leftmenu);
        //设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_leftmenu);
        //设置显示的模式:左侧菜单+主页;左侧菜单+主页+右侧;主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置滑动模式:滑动边缘,全屏滑动,禁止滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(getApplicationContext(), 270));

        //初始化Fragment
        initFragment();
    }

    private void initFragment() {
        //动态添加fragment到指定布局
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //add是在原来的基础叠加一个Fragment
        //replace是将原来的全部remove再add
        transaction.replace(R.id.fl_content, new ContentFragment(), MAIN_CONTENT_TAG);
        transaction.replace(R.id.fl_leftmenu, new LeftmenuFragment(), LEFTMENU_TAG);
        transaction.commit();
    }
}