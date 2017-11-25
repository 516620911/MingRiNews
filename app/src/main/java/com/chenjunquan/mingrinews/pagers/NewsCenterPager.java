package com.chenjunquan.mingrinews.pagers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chenjunquan.mingrinews.activity.MainActivity;
import com.chenjunquan.mingrinews.base.BasePager;
import com.chenjunquan.mingrinews.base.MenuDetailBasePager;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;
import com.chenjunquan.mingrinews.fragment.LeftmenuFragment;
import com.chenjunquan.mingrinews.menudetailpager.InteractMenuDetailPager;
import com.chenjunquan.mingrinews.menudetailpager.NewsMenuDetailPager;
import com.chenjunquan.mingrinews.menudetailpager.PhotosMenuDetailPager;
import com.chenjunquan.mingrinews.menudetailpager.TopicMenuDetailPager;
import com.chenjunquan.mingrinews.utils.CacheUtil;
import com.chenjunquan.mingrinews.utils.Constants;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 * Created by 516620911 on 2017.11.24.
 */

public class NewsCenterPager extends BasePager {

    private List<NewsCenterPagerBean.DataBean> mData;

    public NewsCenterPager(Context context) {
        super(context);
    }

    private ArrayList<MenuDetailBasePager> mDetailBasePagers;

    @Override
    public void initData() {
        super.initData();
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置标题
        tv_title.setText("新闻中心");
        ib_menu.setVisibility(View.VISIBLE);
        TextView textView = new TextView(mContext);
        textView.setText("新闻中心内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        fl_content_content.addView(textView);
        //获取已缓存数据
        String result = CacheUtil.getString(mContext, Constants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(result)) {
            processData(result);
        }
        //向服务器请求数据
        getDataFromNet();

    }

    public void getDataFromNet() {
        RequestParams requestParams = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // Log.i("result", result);
                //缓存数据
                CacheUtil.putString(mContext, Constants.NEWSCENTER_PAGER_URL, result);
                processData(result);

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

    /**
     * 解析Json数据
     *
     * @param result
     */
    private void processData(String result) {
        NewsCenterPagerBean bean = parsedJson(result);
        NewsCenterPagerBean bean2 = parsedJson2(result);
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        Log.i("title", title);
        //给左侧菜单传递数据
        mData = bean.getData();
        MainActivity mainActivity = (MainActivity) mContext;
        LeftmenuFragment leftmenuFragment = mainActivity.getLeftmenuFragment();
        //添加详情页面
        mDetailBasePagers = new ArrayList<>();
        mDetailBasePagers.add(new NewsMenuDetailPager(mContext, mData.get(0)));
        mDetailBasePagers.add(new TopicMenuDetailPager(mContext));
        mDetailBasePagers.add(new PhotosMenuDetailPager(mContext));
        mDetailBasePagers.add(new InteractMenuDetailPager(mContext));

        leftmenuFragment.setData(mData);

    }

    private NewsCenterPagerBean parsedJson2(String json) {
        NewsCenterPagerBean bean2 = new NewsCenterPagerBean();
        try {
            JSONObject object = new JSONObject(json);


            int retcode = object.optInt("retcode");
            bean2.setRetcode(retcode);//retcode字段解析成功

            JSONArray data = object.optJSONArray("data");
            if (data != null && data.length() > 0) {

                List<NewsCenterPagerBean.DataBean> detailPagerDatas = new ArrayList<>();
                //设置列表数据
                bean2.setData(detailPagerDatas);
                //for循环，解析每条数据
                for (int i = 0; i < data.length(); i++) {

                    JSONObject jsonObject = (JSONObject) data.get(i);

                    NewsCenterPagerBean.DataBean detailPagerData = new NewsCenterPagerBean.DataBean();
                    //添加到集合中
                    detailPagerDatas.add(detailPagerData);

                    int id = jsonObject.optInt("id");
                    detailPagerData.setId(id);
                    int type = jsonObject.optInt("type");
                    detailPagerData.setType(type);
                    String title = jsonObject.optString("title");
                    detailPagerData.setTitle(title);
                    String url = jsonObject.optString("url");
                    detailPagerData.setUrl(url);
                    String url1 = jsonObject.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl = jsonObject.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl = jsonObject.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl = jsonObject.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);


                    JSONArray children = jsonObject.optJSONArray("children");
                    if (children != null && children.length() > 0) {

                        List<NewsCenterPagerBean.DataBean.ChildrenData> childrenDatas = new ArrayList<>();

                        //设置集合-ChildrenData
                        detailPagerData.setChildren(childrenDatas);

                        for (int j = 0; j < children.length(); j++) {
                            JSONObject childrenitem = (JSONObject) children.get(j);

                            NewsCenterPagerBean.DataBean.ChildrenData childrenData = new NewsCenterPagerBean.DataBean.ChildrenData();
                            //添加到集合中
                            childrenDatas.add(childrenData);


                            int childId = childrenitem.optInt("id");
                            childrenData.setId(childId);
                            String childTitle = childrenitem.optString("title");
                            childrenData.setTitle(childTitle);
                            String childUrl = childrenitem.optString("url");
                            childrenData.setUrl(childUrl);
                            int childType = childrenitem.optInt("type");
                            childrenData.setType(childType);

                        }

                    }


                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return bean2;
    }

    private NewsCenterPagerBean parsedJson(String result) {
        return new Gson().fromJson(result, NewsCenterPagerBean.class);

    }

    /**
     * 根据位置切换页面
     *
     * @param position
     */
    public void switchPager(int position) {
        //设置标题
        tv_title.setText(mData.get(position).getTitle());
        //移除之前内容
        fl_content_content.removeAllViews();
        //添加新内容
        MenuDetailBasePager menuDetailBasePager = mDetailBasePagers.get(position);
        View rootView = menuDetailBasePager.initView();
        menuDetailBasePager.initData();
        fl_content_content.addView(rootView);
    }
}
