package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myproduct.freetogether.R;
import com.myproduct.freetogether.bean.Item;

import yiqihi.mobile.com.commonlib.AdapterBase;

/**
 * Created by mahaifeng on 2015/4/25.
 */
public class LocationAdapter extends AdapterBase<Item> {
    private Context mContext;
    private String mCategory;
    private String countryName;
    private String currentType;

    public LocationAdapter(Context context, String category) {
        this.mCategory = category;
        this.mContext = context;
    }

    private class ViewHolder {
        TextView tv_value;
        TextView tv_continent;
    }

    public void setTag(String currentType, String countryName) {
        this.currentType = currentType;
        this.countryName = countryName;
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.location_item, null);
            viewHolder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            viewHolder.tv_continent = (TextView) convertView.findViewById(R.id.tv_continent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Item country = getList().get(position);
        viewHolder.tv_value.setText(country.value);
        String rightText = TextUtils.isEmpty(country.continent) ? country.country : country.continent;
        viewHolder.tv_continent.setText("");
        return convertView;
    }
}
