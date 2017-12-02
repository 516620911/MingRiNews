package com.chenjunquan.mingrinews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.domain.PhotosMenuDetailPagerBean;
import com.chenjunquan.mingrinews.utils.Constants;
import com.chenjunquan.mingrinews.volley.VolleyManager;

import java.util.List;

/**
 * Created by JunquanChen on 2017/12/1.
 */

public class PhotosMenuDetailPagerAdapter extends BaseAdapter {
    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news;
    private Context mContext;

    public PhotosMenuDetailPagerAdapter(Context context, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news) {
        this.news = news;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_photos_menudetail_pager, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据位置请求数据
        PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
        viewHolder.tv_title.setText(newsEntity.getTitle());
        //设置图片
        loaderImager(viewHolder, Constants.BASE_URL + newsEntity.getSmallimage());
        return convertView;
    }

    /**
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        //设置tag
        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            //设置默认图片
                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }
}
