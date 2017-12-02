package com.chenjunquan.mingrinews.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.activity.MainActivity;
import com.chenjunquan.mingrinews.base.MenuDetailBasePager;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;
import com.chenjunquan.mingrinews.menudetailpager.tabdetailpagers.TopicTabDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题详细页面
 * Created by 516620911 on 2017.11.25.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {


    /**
     * 专题详情页面
     * Created by 516620911 on 2017.11.25.
     */

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;
    private List<NewsCenterPagerBean.DataBean.ChildrenData> children;
    /**
     * 页签页面的集合
     */
    private ArrayList<TopicTabDetailPager> tabDetailPagers;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.topicmenu_detail_pager, null);
        x.view().inject(this, view);
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
        });
        return view;
    }

    public void initData() {
        //准备专题详情页面的数据
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TopicTabDetailPager(mContext, children.get(i)));
        }
        viewpager.setAdapter(new MyNewsMenuDetailPagerAdapter());
        //绑定ViewPager和TabPagerIndicator
        tabPageIndicator.setViewPager(viewpager);
        //绑定后使用TabPagerIndicator监听
        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //slidingMenu可以全屏滑动
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyNewsMenuDetailPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            Log.i("getPageTitle", children.get(position).getTitle());
            return children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicTabDetailPager tabDetailPager = tabDetailPagers.get(position);
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

