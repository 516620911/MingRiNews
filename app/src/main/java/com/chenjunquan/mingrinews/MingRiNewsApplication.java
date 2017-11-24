package com.chenjunquan.mingrinews;

import android.app.Application;

import org.xutils.x;

/**
 * 所有组件被创建前执行
 * Created by JunquanChen on 2017/11/24.
 */

public class MingRiNewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
