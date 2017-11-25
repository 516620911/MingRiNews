package com.chenjunquan.mingrinews.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.base.MenuDetailBasePager;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;
import com.chenjunquan.mingrinews.menudetailpager.tabdetailpagers.TabDetailPager;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻详情页面
 * Created by 516620911 on 2017.11.25.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    private List<NewsCenterPagerBean.DataBean.ChildrenData> children;
    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.newsmenu_detail_pager, null);
        x.view().inject(this, view);
        return view;
    }

    public void initData() {
        //准备新闻详情页面的数据
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(mContext, children.get(i)));
        }
        viewpager.setAdapter(new MyNewsMenuDetailPagerAdapter());
        //绑定ViewPager和TabPagerIndicator
        tabPageIndicator.setViewPager(viewpager);
        //绑定后使用TabPagerIndicator监听
    }

    class MyNewsMenuDetailPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            container.addView(tabDetailPager.rootView);
            tabDetailPager.initData();
            return tabDetailPager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
