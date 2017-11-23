package com.chenjunquan.mingrinews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager viewpager;
    private ArrayList<ImageView> imageViews;
    private int[] ids;
    private LinearLayout ll_point_group;
    private Button bt_start;
    private ImageView iv_red_point;
    /**
     * 两点的间距
     */
    private int leftmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewpager = findViewById(R.id.viewpager);
        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        ll_point_group = findViewById(R.id.ll_point_group);
        iv_red_point = findViewById(R.id.iv_red_point);
        ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            //导航点
            ImageView point = new ImageView(getApplicationContext());
            point.setBackgroundResource(R.drawable.point_normal);
            //单位像素
            int px = DensityUtil.dip2px(getApplicationContext(), 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
            if (i != 0) {
                params.leftMargin = px;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
        viewpager.setAdapter(myPagerAdapter);

        //根据View的生命周期,当View执行到onLayout或者onDraw时,就已经意味着测量完毕
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGloBalLayoutListener());
        //得到屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i("positionOffset", positionOffset + "");
            Log.i("positionOffsetPixels", positionOffsetPixels + "");

            //红点间移动的距离=屏幕移动百分比*两点间距
            //但是红点的左边距=红点间移动的距离+之前的距离
            int leftmargin = (int) (positionOffset * leftmax) + position * leftmax;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGloBalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //多次执行 需要移除
            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
            Log.i("onGlobalLayout", leftmax + "");
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        /**
         * @param view   当前创建的视图
         * @param object 上面instantiateItem的返回值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
