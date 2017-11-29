package com.chenjunquan.mingrinews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 水平滑动的ViewPager
 * Created by JunquanChen on 2017/11/28.
 */

public class HorizontalSrollViewPager extends ViewPager {
    public HorizontalSrollViewPager(Context context) {
        super(context);
    }

    public HorizontalSrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private float startX;
    private float startY;

    /**
     * 顶部viewpager
     * 首尾两张正常处理(父拦截)
     * 中间页面能正常切换图片
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //请求父类不拦截 自己处理
                getParent().requestDisallowInterceptTouchEvent(true);
                startX=ev.getX();
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX=ev.getX();
                float endY=ev.getY();
                float distanceX=endX-startX;
                float distanceY=endY-startY;
                if(Math.abs(distanceX)>Math.abs(distanceY)){

                    if(getCurrentItem()==0&&distanceX>0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if((getCurrentItem()==(getAdapter().getCount()-1))&&distanceX<0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else{
                    //正常
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
