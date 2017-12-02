package com.chenjunquan.mingrinews.menudetailpager.tabdetailpagers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.activity.NewsDetailActivity;
import com.chenjunquan.mingrinews.base.MenuDetailBasePager;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;
import com.chenjunquan.mingrinews.domain.TabDetailPagerBean;
import com.chenjunquan.mingrinews.utils.CacheUtil;
import com.chenjunquan.mingrinews.utils.Constants;
import com.chenjunquan.mingrinews.view.HorizontalSrollViewPager;
import com.chenjunquan.mingrinews.view.RefreshListView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * 页签详情页面
 * 服务器请求数据
 * viewpager适配(标题+红点)
 * Created by 516620911 on 2017.11.25.
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean.ChildrenData childrenData;
    /**
     * 顶置viewpager新闻
     */
    private List<TabDetailPagerBean.DataEntity.TopnewsData> topnews;
    private HorizontalSrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListView listview;
    private int prePosition = 0;
    private List<TabDetailPagerBean.DataEntity.NewsData> news;
    private MyTabDetailPagerListAdapter listAdapter;
    private ImageOptions imageOptions;
    private String url;
    private String moreUrl;
    private boolean isLoadMore = false;
    private String READ_ARRAY_ID;
    private boolean isDragging=false;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.tabdetail_pager, null);
        View topNewsView = View.inflate(mContext, R.layout.topnews, null);
        viewpager = (HorizontalSrollViewPager) topNewsView.findViewById(R.id.viewpager);
        tv_title = (TextView) topNewsView.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) topNewsView.findViewById(R.id.ll_point_group);
        listview = (RefreshListView) view.findViewById(R.id.listview);

        //把顶部viewpager等添加到ListView表头
        //listview.addHeaderView(topNewsView);
        //将下拉刷新和顶部轮播图结合成一个view添加到头部
        listview.addTopNewsView(topNewsView);
        //设置松开刷新监听方法
        listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void OnPullDownRefresh() {
                //刷新=联网请求数据
                getDataFromNet();
            }

            @Override
            public void OnLoadMore() {
                //Log.i("OnLoadMore","OnLoadMore");
                if (TextUtils.isEmpty(moreUrl)) {
                    Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                    listview.onRefreshFinish(false);
                } else {
                    Toast.makeText(mContext, "更多数据", Toast.LENGTH_SHORT).show();

                    getMoreDataFromNet(0);
                }
            }
        });
        //已读功能 点击变灰
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int newsId = news.get(position - 1).getId();
                String newsIds = CacheUtil.getString(mContext, READ_ARRAY_ID);
                if (!newsIds.contains(newsId + "")) {
                    CacheUtil.putString(mContext, READ_ARRAY_ID, newsIds + newsId + ",");
                    listAdapter.notifyDataSetChanged();
                }
                TabDetailPagerBean.DataEntity.NewsData newsData = news.get(position - 1);
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("url", Constants.BASE_URL + newsData.getUrl());
                mContext.startActivity(intent);

            }
        });

        return view;
    }

    private void getMoreDataFromNet(int i) {
        RequestParams params = new RequestParams(moreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isLoadMore = true;
                //解析并显示数据
                processData(result);
                listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                listview.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //Log.i("onCancelled----", cex.getMessage());
            }

            @Override
            public void onFinished() {

                //Log.i("onFinished----", "onFinished");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenData.getUrl();
        //取本地缓存数据
        String result = CacheUtil.getString(mContext, url);
        if (!TextUtils.isEmpty(result)) {
            processData(result);
        }
        //联网请求数据
        getDataFromNet();

    }

    public void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtil.putString(mContext, url, result);
                //解析并显示数据
                processData(result);
                //Log.i("onSuccess----", result);
                //隐藏顶部下拉刷新控件(更新时间)
                listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //Log.i("onError----", ex.getMessage());
                //隐藏顶部下拉刷新控件
                listview.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //Log.i("onCancelled----", cex.getMessage());
            }

            @Override
            public void onFinished() {

                //Log.i("onFinished----", "onFinished");
            }
        });
    }

    private void processData(String result) {
        TabDetailPagerBean bean = parsedJson(result);
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }
        if (!isLoadMore) {
            //默认
            topnews = bean.getData().getTopnews();
            viewpager.setAdapter(new TabDetailPagerTopNewsAdapter());
            tv_title.setText(topnews.get(prePosition).getTitle());
            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tv_title.setText(topnews.get(position).getTitle());
                    //设置红点改变
                    ll_point_group.getChildAt(prePosition).setEnabled(false);
                    ll_point_group.getChildAt(position).setEnabled(true);
                    prePosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    switch (state) {
                        //静止到拖拽
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            isDragging=true;
                            //拖拽中移除消息
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        //静止
                        case ViewPager.SCROLL_STATE_IDLE:

                        //稳定下来
                        case ViewPager.SCROLL_STATE_SETTLING:
                            if(!isDragging){
                                mHandler.removeCallbacksAndMessages(null);
                                mHandler.sendEmptyMessageDelayed(0,4000);
                            }
                            isDragging=false;
                            break;
                    }
                }
            });
            //因为该方法执行两次(本地,网络) 所以要先移除
            ll_point_group.removeAllViews();
            //添加红点
            addPoint();
            news = bean.getData().getNews();
            //设置listview的适配器
            listAdapter = new MyTabDetailPagerListAdapter();
            listview.setAdapter(listAdapter);
        } else {
            //加载更多处理逻辑
            isLoadMore = false;
            news.addAll(bean.getData().getNews());
            listAdapter.notifyDataSetChanged();
        }
        //顶部轮播图viewpager自动播放功能
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(0, 4000);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int item = (viewpager.getCurrentItem() + 1) % topnews.size();
            viewpager.setCurrentItem(item);
            mHandler.sendEmptyMessageDelayed(0, 4000);
            super.handleMessage(msg);
        }
    };

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    class MyTabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataEntity.NewsData newsData = news.get(position);
            String listimageurl = Constants.BASE_URL + newsData.getListimage();
            //请求图片
            x.image().bind(viewHolder.iv_icon, listimageurl, imageOptions);
            viewHolder.tv_title.setText(newsData.getTitle());
            viewHolder.tv_time.setText(newsData.getPubdate());
            String newsIds = CacheUtil.getString(mContext, READ_ARRAY_ID);
            if (newsIds.contains(newsData.getId() + "")) {
                //设置灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                //设置黑色
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    private void addPoint() {
        for (int i = 0; i < topnews.size(); i++) {
            ImageView point = new ImageView(mContext);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }


            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            //默认图片
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //拉伸图片
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            //获取图片地址
            TabDetailPagerBean.DataEntity.TopnewsData topnewsData = topnews.get(position);
            String topimageUrl = Constants.BASE_URL + topnewsData.getTopimage();
            x.image().bind(imageView, topimageUrl, imageOptions);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            //滑动过后ACTION_UP没有执行 转而执行ACTION_CANCEL(被拖拽替代)
                        /*case MotionEvent.ACTION_CANCEL:
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 4000);
                            break;*/
                    }
                    return true;
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private TabDetailPagerBean parsedJson(String result) {
        return new Gson().fromJson(result, TabDetailPagerBean.class);
    }
}
