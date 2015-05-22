package yiqihi.mobile.com.commonlib;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterBase<T> extends BaseAdapter {

    protected final ArrayList<T> mList = new ArrayList<T>();

    public List<T> getList() {
        return mList;
    }

    /**
     * 向列表添加数据
     * 
     * @param list
     */
    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + list.size());
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 向列表顶部添加数据
     * 
     * @param list
     */
    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + list.size());
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 刷新页面数据
     * 
     * @param list
     *            新数据列表
     */
    public void refresh(List<T> list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.ensureCapacity(list.size());
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getExView(position, convertView, parent);
    }

    /**
     * 【getView】
     * 
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected abstract View getExView(int position, View convertView, ViewGroup parent);

}
