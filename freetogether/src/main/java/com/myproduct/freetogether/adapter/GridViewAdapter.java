package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.myapplication.yiqihi.task.YQHBaseTask;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.bean.CheckItem;

import java.util.List;

import de.greenrobot.event.EventBus;
import yiqihi.mobile.com.commonlib.AdapterBase;

public class GridViewAdapter extends AdapterBase<CheckItem> {
    private Context mContext;
    private boolean isMult;
    private String label;

    public GridViewAdapter(Context context, boolean isMult) {
        this.mContext = context;
        this.isMult = isMult;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public class ViewHolder {
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
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.cbLabel = (CheckBox) convertView.findViewById(R.id.cb_label);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CheckItem item = getList().get(position);
        viewHolder.cbLabel.setText(item.value);
        viewHolder.cbLabel.setChecked(item.selected);
        viewHolder.cbLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.selected = viewHolder.cbLabel.isChecked();
                item.selected = true;
                checked(item);
            }
        });
        return convertView;
    }

    private void checked(CheckItem item) {
        List<CheckItem> listItem = getList();
        for (CheckItem t : listItem) {
            if (!t.equals(item)) {
                t.selected = false;
            }
        }
        notifyDataSetChanged();
    }

    public void reset() {
        List<CheckItem> items = getList();
        for (CheckItem item : items) {
            item.selected = false;
        }
        notifyDataSetChanged();
    }

    public String getSelectValue() {
        List<CheckItem> items = getList();
        for (CheckItem item : items) {
            if (item.selected) {
                return item.value;
            }
        }
        return "";
    }

    public int getSelectPosition() {
        List<CheckItem> items = getList();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).selected) {
                return i;
            }
        }
        return 0;
    }
}
