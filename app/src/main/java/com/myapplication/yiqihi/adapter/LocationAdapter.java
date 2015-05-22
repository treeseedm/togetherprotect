package com.myapplication.yiqihi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.myapplication.yiqihi.R;
import com.myapplication.yiqihi.bean.Country;
import com.myapplication.yiqihi.bean.Item;
import com.myapplication.yiqihi.bean.LocationResponse;
import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.event.MessageEvent;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;

import de.greenrobot.event.EventBus;
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
//    convertView.setOnClickListener(new onItemClick(country));
        return convertView;
    }
//    private class onItemClick implements View.OnClickListener{
//        private Item mCountry;
//        public onItemClick(Item country){
//              this.mCountry=country;
//        }
//
//        @Override
//        public void onClick(View v) {
//            String continent="";
//            String country="";
//            String city="";
//            if("1".equals(mCountry.type)){
//                country=mCountry.value;
//                city="";
//            }else if("2".equals(mCountry.type)){
//                country=mCountry.country;
//                city=mCountry.value;
//            }else {
//                if(Constants.CONTINENT.equals(currentType)){
//                    continent=mCountry.value;
//                }else if(Constants.COUNTRY.equals(currentType)){
//                    country=mCountry.value;
//                }else if(Constants.LOCATION.equals(currentType)){
//                    city=mCountry.value;
//                    country=countryName;
//                }
//            }
//          EventBus.getDefault().post(new MessageEvent(mCountry.value,YQHBaseTask.ACTION_CHANGETEXT));
//            new YQHBaseTask(mContext,true,YQHBaseTask.ACTION_SEARCHTRIP
//                    , MethodHelper.searchTrip( continent, country, city, mCategory,1).toString()).exec(false);
//        }
//    }
}
