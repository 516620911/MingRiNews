package com.chenjunquan.mingrinews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.adapter.ContentFragmentAdapter;
import com.chenjunquan.mingrinews.base.BaseFragment;
import com.chenjunquan.mingrinews.base.BasePager;
import com.chenjunquan.mingrinews.pagers.GovaffairPager;
import com.chenjunquan.mingrinews.pagers.HomePager;
import com.chenjunquan.mingrinews.pagers.NewsCenterPager;
import com.chenjunquan.mingrinews.pagers.SettingPager;
import com.chenjunquan.mingrinews.pagers.SmartServicePager;
import com.chenjunquan.mingrinews.utils.LogUtil;
import com.chenjunquan.mingrinews.view.NoScrollViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * RadioGroup选项卡功能
 * xUtils3注解的使用
 * Created by JunquanChen on 2017/11/23.
 */

public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    @ViewInject(R.id.viewpager)
    private NoScrollViewPager mViewPager;
    //五个页面的集合
    private ArrayList<BasePager> mBasePagers;


    @Override
    public View initView() {
        LogUtil.e("ContentFragmentinitView");
        View view = View.inflate(mContext, R.layout.content_fragment, null);
        //rg_main = view.findViewById(R.id.rg_main);
        //mViewPager = view.findViewById(R.id.viewpager);
        //把视图注入到框架中,关联当前类和指定view
        x.view().inject(ContentFragment.this, view);
        return view;

    }

    @Override
    public void initData() {
        LogUtil.e("ContentFragmentinitData");
        //添加页面到集合中
        mBasePagers = new ArrayList<>();
        mBasePagers.add(new HomePager(mContext));
        mBasePagers.add(new NewsCenterPager(mContext));
        mBasePagers.add(new SmartServicePager(mContext));
        mBasePagers.add(new GovaffairPager(mContext));
        mBasePagers.add(new SettingPager(mContext));


        ContentFragmentAdapter contentFragmentAdapter = new ContentFragmentAdapter(mBasePagers);
        mViewPager.setAdapter(contentFragmentAdapter);
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置RadioGroup选中状态改变监听 实现选项卡功能
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        rg_main.check(R.id.rb_home);
        mBasePagers.get(0).initData();
    }

    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) mBasePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //初始化被选中页面的数据
            mBasePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.rb_newscenter:
                    mViewPager.setCurrentItem(1,false);
                    break;
                case R.id.rb_smartservice:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.rb_govaffair:
                    mViewPager.setCurrentItem(3);
                    break;
                case R.id.rb_setting:
                    mViewPager.setCurrentItem(4);
                    break;
            }
        }
    }



}
