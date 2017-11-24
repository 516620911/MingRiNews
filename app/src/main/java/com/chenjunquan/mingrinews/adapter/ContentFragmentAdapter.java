package com.chenjunquan.mingrinews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.chenjunquan.mingrinews.base.BasePager;

import java.util.ArrayList;

/**
 * 替换MainActivity布局的Fragment里面ViewPager的适配器
 *
 * Created by 516620911 on 2017.11.24.
 */

public class ContentFragmentAdapter extends PagerAdapter {
    private ArrayList<BasePager> mBasePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers) {
        mBasePagers = basePagers;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager = mBasePagers.get(position);
        View rootView = basePager.mRootView;
        //因为ViewPager会预加载下一个页面,但是我们不一定用的到那个页面
        //为了减少流量的使用 调整数据初始化的时机为页面被选中
        //basePager.initData();
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mBasePagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}