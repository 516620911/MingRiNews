package com.chenjunquan.mingrinews.menudetailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.adapter.PhotosMenuDetailPagerAdapter;
import com.chenjunquan.mingrinews.base.MenuDetailBasePager;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;
import com.chenjunquan.mingrinews.domain.PhotosMenuDetailPagerBean;
import com.chenjunquan.mingrinews.utils.CacheUtil;
import com.chenjunquan.mingrinews.utils.Constants;
import com.chenjunquan.mingrinews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 图组详细页面
 * Created by 516620911 on 2017.11.25.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean detailPagerData;
    @ViewInject(R.id.listview)
    private ListView listview;
    @ViewInject(R.id.gridview)
    private GridView gridview;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean.DataBean bean) {
        super(context);
        this.detailPagerData = bean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.photos_menudetail_pager, null);
        x.view().inject(this, view);

        return view;
    }

    public void initData() {
        url = Constants.BASE_URL + detailPagerData.getUrl();
        String saveJson = CacheUtil.getString(mContext, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNetByVolley();
    }

    /**
     * 解析json 显示数据
     *
     * @param json
     */
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = parsedJson(json);
        news = bean.getData().getNews();
        PhotosMenuDetailPagerAdapter adapter = new PhotosMenuDetailPagerAdapter(mContext, news);
        listview.setAdapter(adapter);
    }

    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }

    private void getDataFromNetByVolley() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CacheUtil.putString(mContext, url, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        VolleyManager.getRequestQueue().add(request);
    }

    private boolean isShowListView = true;

    /**
     * GridView和ListView切换
     * @param ib_swich_list_grid
     */
    public void switchListAndGrid(ImageButton ib_swich_list_grid) {
        //当我点击的时候 如果当前是listview则切换成gridview
        if (isShowListView) {
            //显示GridView 隐藏ListView
            gridview.setVisibility(View.VISIBLE);
            PhotosMenuDetailPagerAdapter adapter = new PhotosMenuDetailPagerAdapter(mContext, news);
            gridview.setAdapter(adapter);
            listview.setVisibility(View.GONE);
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);
            isShowListView = false;
        } else {
            //按钮显示GridView
            //隐藏ListView
            listview.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);
            PhotosMenuDetailPagerAdapter adapter = new PhotosMenuDetailPagerAdapter(mContext, news);
            listview.setAdapter(adapter);
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
            isShowListView = true;
        }
    }
}
