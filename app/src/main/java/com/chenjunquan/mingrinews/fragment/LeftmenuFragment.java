package com.chenjunquan.mingrinews.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chenjunquan.mingrinews.base.BaseFragment;
import com.chenjunquan.mingrinews.utils.LogUtil;

/**
 * Created by JunquanChen on 2017/11/23.
 */

public class LeftmenuFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View initView() {
        LogUtil.e("LeftmenuFragmentinitView");
        textView = new TextView(mContext);
        textView.setTextSize(23);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        LogUtil.e("LeftmenuFragmentinitData");
        textView.setText("左侧菜单页面");
    }
}
