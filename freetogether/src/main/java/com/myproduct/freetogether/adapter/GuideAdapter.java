package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.bean.Item;

import org.apmem.tools.layouts.FlowLayout;

import yiqihi.mobile.com.commonlib.AdapterBase;

public class GuideAdapter extends AdapterBase<Item> {
    private static final String TAG = "GuideAdapter";
    private Context mContext;

    public GuideAdapter(Context context) {
        this.mContext = context;
    }

    private class ViewHolder {
        TextView tv_publishtime, title, tv_address, tv_time, tv_money, tv_des, tv_peoplenum, tv_remainday;
        SimpleDraweeView sdv_face;
        Button btn_add;
        FlowLayout fl_container;
    }

    /**
     * 【getView】
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.guide_item, null);
            vh.tv_publishtime = (TextView) convertView.findViewById(R.id.tv_publishtime);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            vh.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            vh.tv_peoplenum = (TextView) convertView.findViewById(R.id.tv_peoplenum);
            vh.tv_remainday = (TextView) convertView.findViewById(R.id.tv_remainday);
            vh.fl_container = (FlowLayout) convertView.findViewById(R.id.fl_container);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            Item guide = getList().get(position);
            vh.tv_publishtime.setText("五分钟前");
            vh.title.setText("我是标题我是标题我是标题我是标题我是标题我是标题我是标题我是标题我是标题我是标题");
            vh.tv_address.setText("我是地址");
            vh.tv_time.setText("11月15日出发");
            vh.tv_money.setText("￥200");
            vh.tv_des.setText("我是描述，我是描述");
            vh.tv_peoplenum.setText("5人");
            vh.tv_remainday.setText("剩2天");
            String[] tags = new String[]{"还差一人", "会英语", "会日语"};
            for (int i = 0; i < tags.length; i++) {
                Button view = (Button) View.inflate(mContext, R.layout.flow_button, null);
                view.setText(tags[i]);
                vh.fl_container.addView(view, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
