package com.myapplication.yiqihi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.myapplication.yiqihi.R;
import com.myapplication.yiqihi.bean.Item;
import com.myapplication.yiqihi.event.MessageEvent;
import com.myapplication.yiqihi.task.YQHBaseTask;

import java.util.List;

import de.greenrobot.event.EventBus;
import yiqihi.mobile.com.commonlib.AdapterBase;

public class GridViewAdapter extends AdapterBase<Item> {
    private Context mContext;
    private boolean isMult;
    private String label;
    public GridViewAdapter(Context context,boolean isMult){
        this.mContext=context;
        this.isMult=isMult;
    }
    public  void setLabel(String label){
        this.label=label;
    }
    public class ViewHolder{
        CheckBox cbLabel;
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
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.gridview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.cbLabel = (CheckBox) convertView.findViewById(R.id.cb_label);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
       final Item item=getList().get(position);
        viewHolder.cbLabel.setText(item.value);
        viewHolder.cbLabel.setChecked(item.selected);
        viewHolder.cbLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.selected=viewHolder.cbLabel.isChecked();
                if(viewHolder.cbLabel.isChecked()&&!isMult){
                    item.selected=true;
                    checked(item);
                    EventBus.getDefault().post(new MessageEvent(label, YQHBaseTask.ACTION_UPDATEFILTER));
                } else if(isMult){
                    EventBus.getDefault().post(new MessageEvent(label, YQHBaseTask.ACTION_UPDATEPANEL));
                }
            }
        });
        return convertView;
    }
    private void checked(Item item){
        List<Item> listItem=getList();
        for(Item t:listItem){
            if(!t.equals(item)){
               t.selected=false;
            }
        }
        notifyDataSetChanged();
    }
    public void reset(){
       List<Item> items= getList();
        for(Item item:items){
            item.selected=false;
        }
        notifyDataSetChanged();
    }
    public String getSelectValue(){
        List<Item> items= getList();
        for(Item item:items){
            if(item.selected){
                return item.value;
            }
        }
       return "";
    }
    public String getSelectId(){
        StringBuilder builder = new StringBuilder();
        List<Item> items= getList();
        for(Item item:items){
            if(item.selected){
                builder.append(item.id+"-");
            }
        }
        String ids=builder.toString();

        if(ids.length()>0){
            ids=ids.substring(0,ids.length()-1);
            return ids;
        }

        return "";
    }

}
