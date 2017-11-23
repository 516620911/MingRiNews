package com.chenjunquan.mingrinews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.base.BaseFragment;
import com.chenjunquan.mingrinews.utils.LogUtil;

/**
 * Created by JunquanChen on 2017/11/23.
 */

public class ContentFragment extends BaseFragment {
    private RadioGroup rg_main;
    private ViewPager mViewPager;

    @Override
    public View initView() {
        LogUtil.e("ContentFragmentinitView");
        View view = View.inflate(mContext, R.layout.content_fragment, null);
        rg_main = view.findViewById(R.id.rg_main);
        mViewPager = view.findViewById(R.id.viewpager);
        return view;
    }

    @Override
    public void initData() {
        LogUtil.e("ContentFragmentinitData");
        rg_main.check(R.id.rb_home);
    }
}
