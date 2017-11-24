package com.chenjunquan.mingrinews.pagers;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.chenjunquan.mingrinews.activity.MainActivity;
import com.chenjunquan.mingrinews.base.BasePager;
import com.chenjunquan.mingrinews.utils.Constants;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 主页
 * Created by 516620911 on 2017.11.24.
 */

public class NewsCenterPager extends BasePager{
    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        tv_title.setText("新闻中心");
        TextView textView = new TextView(mContext);
        textView.setText("新闻中心内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        fl_content_content.addView(textView);
        MainActivity mainActivity= (MainActivity) mContext;
        mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //向服务器请求数据
        getDataFromNet();
    }

    public void getDataFromNet() {
        RequestParams requestParams = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
