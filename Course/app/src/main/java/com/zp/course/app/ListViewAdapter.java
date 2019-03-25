package com.zp.course.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zp.course.util.CollectionsUtils;
import com.zp.course.util.Preconditions;
import com.zp.course.util.Validator;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * Class description:
 *
 * @author ZP
 * @version 1.0
 * @see ListViewAdapter
 * @since 2019/3/25
 */
public abstract class ListViewAdapter<T> extends BaseAdapter {

    /**
     * data source of the adapter.
     */
    private List<T> mItems;

    public ListViewAdapter(List<T> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return CollectionsUtils.sizeOf(mItems);
    }

    @Override
    public T getItem(int position) {
        return CollectionsUtils.get(mItems, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        onBindViewHolder(holder, position, getItem(position));

        return convertView;
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void onBindViewHolder(ViewHolder holder, int position, T item);

    /**
     * return the data source of the adapter.
     *
     * @return the data source of the adapter.
     */
    public List<T> getItems() {
        return this.mItems;
    }

    /**
     * set the data source to the adapter.
     *
     * @param items new data source to set.
     */
    public void setItems(List<T> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    /**
     * inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation)
     *
     * @param items         items to be insert into this list.
     * @param positionStart position at which to insert the first element from the
     *                      specified items.
     * @param isRepeatable  if true, the data in the list can be repeatable.
     */
    public void inserts(@NonNull List<T> items, @IntRange(from = 0) int positionStart, boolean isRepeatable) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        if (Validator.isEmpty(items)) {
            return;
        }
        if (!isRepeatable && this.mItems.containsAll(items)) {
            return;
        }
        this.mItems.addAll(positionStart, items);
        notifyDataSetChanged();
    }

    /**
     * inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation)
     *
     * @param items         items to be insert into this list.
     * @param positionStart position at which to insert the first element from the
     *                      specified items.
     */
    public void inserts(@NonNull List<T> items, @IntRange(from = 0) int positionStart) {
        inserts(items, positionStart, false);
    }

    public void insert(@NonNull T t, @IntRange(from = 0) int position, boolean isRepeatable) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        if (!isRepeatable && this.mItems.contains(t)) {
            return;
        }
        mItems.add(position, t);
        notifyDataSetChanged();
    }

    public void insert(@NonNull T t, @IntRange(from = 0) int position) {
        insert(t, position, false);
    }

    public void appends(@NonNull List<T> list, boolean isRepeatable) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        if (Validator.isEmpty(list)) {
            return;
        }
        if (!isRepeatable && this.mItems.containsAll(list)) {
            return;
        }
        this.mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void appends(@NonNull List<T> list) {
        this.appends(list, false);
    }

    public void append(@NonNull T t, boolean isRepeatable) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        if (!isRepeatable && this.mItems.contains(t)) {
            return;
        }
        mItems.add(t);
        notifyDataSetChanged();
    }

    public void append(@NonNull T t) {
        this.append(t, false);
    }

    public void removes(@IntRange(from = 0) int positionStart, int itemCount) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        int positionEnd = positionStart + itemCount - 1;
        for (; positionEnd >= positionStart; positionEnd--) {
            mItems.remove(positionEnd);
        }
        notifyDataSetChanged();
    }

    public void removes() {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        int itemCount = mItems.size();
        mItems.clear();
        notifyDataSetChanged();
    }

    public void remove(@IntRange(from = 0) int position) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void replace(List<T> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }
}
