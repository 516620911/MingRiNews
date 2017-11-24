package com.chenjunquan.mingrinews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager屏蔽滑动
 * Created by 516620911 on 2017.11.24.
 */

public class NoScrollViewPager extends ViewPager {

    /**
     * 通常是代码中实例化时使用的构造方法
     * @param context
     */
    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 布局文件实例化时使用这种两个参数的构造方法
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
