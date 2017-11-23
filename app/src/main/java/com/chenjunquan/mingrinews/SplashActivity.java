package com.chenjunquan.mingrinews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.chenjunquan.mingrinews.activity.GuideActivity;
import com.chenjunquan.mingrinews.utils.CacheUtil;

public class SplashActivity extends Activity {
    /**
     * 是否进入过主页面
     */
    public static final String START_MAIN = "start_main";
    private RelativeLayout rl_splash_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_splash_root = findViewById(R.id.rl_splash_root);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //动画集
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(1000);
        animationSet.setFillAfter(true);
        rl_splash_root.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //是否需要打开引导页面
                boolean isStartMain = CacheUtil.getBoolean(getApplicationContext(), START_MAIN);
                if (isStartMain) {
                    //直接进入主页面
                } else {
                    //引动页面
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                }
                //关闭当前页面
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
