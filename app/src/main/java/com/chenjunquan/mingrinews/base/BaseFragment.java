package com.chenjunquan.mingrinews.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JunquanChen on 2017/11/23.
 */

public abstract class BaseFragment extends Fragment {
    public Activity mContext;

    //fragment创建后回调函数
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainActivity
        mContext = getActivity();
    }

    //视图被创建后回调
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * @return 返回Fragment的视图
     */
    public abstract View initView();

    //Activity创建完成回调
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 提供一个方法用于初始化页面的数据
     */
    public void initData() {

    }
}
