package com.chenjunquan.mingrinews.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chenjunquan.mingrinews.R;
import com.chenjunquan.mingrinews.activity.MainActivity;
import com.chenjunquan.mingrinews.base.BaseFragment;
import com.chenjunquan.mingrinews.domain.NewsCenterPagerBean;
import com.chenjunquan.mingrinews.pagers.NewsCenterPager;
import com.chenjunquan.mingrinews.utils.DensityUtil;
import com.chenjunquan.mingrinews.utils.LogUtil;

import java.util.List;

/**
 * 如何切换主内容里面的内容
 * Created by JunquanChen on 2017/11/23.
 */

public class LeftmenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> mData;
    private ListView listView;
    private leftmenuFragmentAdapter adapter;
    /**
     * 记录点击的位置
     */
    private int prePosition=0;

    @Override
    public View initView() {
        //LogUtil.e("LeftmenuFragmentinitView");
        listView = new ListView(mContext);
        listView.setPadding(0, DensityUtil.dip2px(mContext, 40), 0, 0);
        //设置分割线高度为0
        listView.setDividerHeight(0);
        //取消低版本的点击变灰
        listView.setCacheColorHint(Color.TRANSPARENT);
        //设置按下listview的item不变色
        listView.setSelector(android.R.color.transparent);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置点击为红色
                prePosition = position;
                adapter.notifyDataSetChanged();
                //关闭左侧菜单
                MainActivity mainActivity = (MainActivity) mContext;
                //开关状态互换
                mainActivity.getSlidingMenu().toggle();
                //切换到对应的页面
                switchPager(prePosition);
            }
        });
        return listView;
    }

    @Override
    public void initData() {
        LogUtil.e("LeftmenuFragmentinitData");
    }

    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.mData = data;
        for (int i = 0; i < mData.size(); i++) {
            Log.i("title", data.get(i).getTitle());
        }
        //设置适配器
        adapter = new leftmenuFragmentAdapter();
        listView.setAdapter(adapter);
        //修改默认页面
        switchPager(prePosition);
    }

    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) mContext;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.switchPager(position);
    }

    class leftmenuFragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textview = (TextView) View.inflate(mContext, R.layout.item_leftmenu, null);
            textview.setText(mData.get(position).getTitle());
            textview.setEnabled(position == prePosition);
            return textview;
        }
    }
}
