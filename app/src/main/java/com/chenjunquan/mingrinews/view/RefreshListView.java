package com.chenjunquan.mingrinews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chenjunquan.mingrinews.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义下拉刷新listview
 * Created by JunquanChen on 2017/11/28.
 */

public class RefreshListView extends ListView {
    private View ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;
    private LinearLayout headerView;
    private int pullDownRefreshHeight;

    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;

    /**
     * 手松刷新
     */
    public static final int RELEASE_REFRESH = 1;


    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;


    /**
     * 当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;
    private LinearLayout footerView;
    private int footerViewHeight;
    /**
     * 是否已经加载更多
     */
    private boolean isLoadMore;
    private View topNewsView;
    private int listViewOnscreenY=-1;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footerView = (LinearLayout) View.inflate(context, R.layout.refresh_footer, null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        //向listview底部添加尾部
        addFooterView(footerView);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //静止或惯性滚动状态
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                    //并且最后一条可见
                    Log.i(getLastVisiblePosition() + "", getCount() + "");
                    if(getLastVisiblePosition()>=getCount()-1){

                        //显示加载更多的footerView
                        footerView.setPadding(8, 8, 8, 8);
                        //改变状态
                        isLoadMore = true;
                        //回调
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.OnLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private Animation upAnimation;
    //private Animation downAnimation;

    private void initAnimation() {
        //箭头由下到上
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        /*downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);*/
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_pull_down_refresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        ll_pull_down_refresh.measure(0, 0);
        pullDownRefreshHeight = ll_pull_down_refresh.getMeasuredHeight();
        ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
        //则最先添加的组件在最上方
        addHeaderView(headerView);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }
                //判断顶部轮播图是否完全显示只有完全显示才会下拉刷新
                boolean isDisplayTopNews = isDisplayTopNews();
                //如果没有完全显示就不存在下拉刷新的情况
                if (!isDisplayTopNews)
                    break;
                if (currentStatus == REFRESHING) {
                    break;
                }
                float endY = ev.getY();
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    int paddingTop = (int) (distanceY - pullDownRefreshHeight);
                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新ll_pull_down_refresh里面的控件
                        refreshViewState();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        //下拉刷新状态
                        currentStatus = RELEASE_REFRESH;
                        //更新ll_pull_down_refresh里面的控件
                        refreshViewState();
                    }

                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    //弹回去 不刷新 因为没有下拉到指定位置
                    ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
                } else if (currentStatus == RELEASE_REFRESH) {
                    //弹回到完全显示的状态 提示正在刷新
                    currentStatus = REFRESHING;
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    //更新ll_pull_down_refresh里面的控件
                    refreshViewState();

                    //回调(此回调函数导致一个BUG使得listview上拉后在不该刷新的情况调用了该刷新方法)
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.OnPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshViewState() {
        switch (currentStatus) {
            //提示需要继续下拉到指定位置才刷新
            case PULL_DOWN_REFRESH:
                //iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新...");
                break;
            //提示需要松开才刷新
            case RELEASE_REFRESH:
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("松开刷新...");
                break;
            case REFRESHING:
                tv_status.setText("正在刷新");
                pb_status.setVisibility(VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;

        }
    }

    /**
     * 当刷新数据时 请求服务器失败或成功后调用
     * 还原更新下拉状态
     *
     * @param b
     */
    public void onRefreshFinish(boolean b) {
        /*tv_status.setText("下拉刷新...");

        iv_arrow.clearAnimation();
        pb_status.setVisibility(GONE);
        iv_arrow.setVisibility(VISIBLE);*/
        if (isLoadMore) {
            //加载更多
            isLoadMore = false;
            footerView.setPadding(0, -footerViewHeight, 0, 0);
        } else {
            //默认
            //隐藏下拉刷新控件
            if (b) {
                //更新成功
                tv_time.setText("上次更新时间:" + getSystemTime());
                iv_arrow.clearAnimation();
                pb_status.setVisibility(GONE);
                iv_arrow.setVisibility(VISIBLE);
                ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
            } else {
                tv_status.setText("刷新失败...");
            }
            //还原可刷新状态
            currentStatus = PULL_DOWN_REFRESH;
        }


    }

    private String getSystemTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 添加顶部轮播图到头
     *
     * @param topNewsView
     */
    public void addTopNewsView(View topNewsView) {
        if (topNewsView != null) {
            this.topNewsView = topNewsView;
            headerView.addView(topNewsView);
        }
    }

    /**
     * @return 是否完全显示顶部轮播图
     */
    public boolean isDisplayTopNews() {
        if(topNewsView==null) return true;
        //得到list在屏幕上的坐标
        int[] location = new int[2];
        if(listViewOnscreenY==-1){
            getLocationOnScreen(location);
            listViewOnscreenY=location[1];
        }
        //得到顶部轮播图在屏幕上位置
        topNewsView.getLocationOnScreen(location);
        int topNewsViewOnScreenY=location[1];
        return listViewOnscreenY<=topNewsViewOnScreenY;
    }

    public interface OnRefreshListener {
        void OnPullDownRefresh();

        void OnLoadMore();
    }

    private OnRefreshListener mOnRefreshListener;

    /**
     * 设置接口的实例 并重写接口方法 实现松开刷新回调功能
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }
}
