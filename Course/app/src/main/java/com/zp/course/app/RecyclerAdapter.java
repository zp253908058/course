package com.zp.course.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zp.course.util.CollectionsUtils;
import com.zp.course.util.Preconditions;
import com.zp.course.util.Validator;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see RecyclerAdapter
 * @since 2019/3/14
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private static final String TAG = "RecyclerAdapter";

    /**
     * data source of the adapter.
     */
    private List<T> mItems;

    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public RecyclerAdapter(@Nullable List<T> items) {
        this.mItems = items;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutByViewType(viewType), parent, false);
        return new RecyclerViewHolder(view);
    }

    /**
     * return the layout's id by the given view type.
     *
     * @param viewType The view type of the new View.
     * @return the layout's id of the special view type.
     */
    @LayoutRes
    protected abstract int getLayoutByViewType(int viewType);

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        onBindViewHolder(holder, position, getItem(position));
    }

    /**
     * extend method of {@link #onBindViewHolder}.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param item     the  item of the special position within the adapter's data set.
     */
    protected abstract void onBindViewHolder(RecyclerViewHolder holder, int position, T item);

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
     * return the item of the special position within the adapter's data set.
     *
     * @param position position to get.
     * @return the  item of the special position within the adapter's data set.
     */
    public T getItem(int position) {
        return CollectionsUtils.get(mItems, position);
    }

    @Override
    public int getItemCount() {
        return CollectionsUtils.sizeOf(mItems);
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
        notifyItemRangeInserted(positionStart, items.size());
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
        notifyItemInserted(position);
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
        int startIndex = mItems.size();
        this.mItems.addAll(list);
        notifyItemRangeInserted(startIndex, list.size());
    }

    public void appends(@NonNull List<T> list) {
        this.appends(list, false);
    }

    public void append(@NonNull T t, boolean isRepeatable) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        if (!isRepeatable && this.mItems.contains(t)) {
            return;
        }
        int startIndex = mItems.size();
        mItems.add(t);
        notifyItemInserted(startIndex);
    }

    public void append(@NonNull T t) {
        this.append(t, false);
    }

    public void updates(@NonNull List<T> list, @IntRange(from = 0) int positionStart) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        int size = CollectionsUtils.sizeOf(list);
        for (int i = 0; i < size; i++) {
            mItems.set(positionStart + i, list.get(i));
        }
        notifyItemRangeChanged(positionStart, size);
    }

    public void update(@NonNull T t, @IntRange(from = 0) int position) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        mItems.set(position, t);
        notifyItemChanged(position);
    }

    public void removes(@IntRange(from = 0) int positionStart, int itemCount) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        int positionEnd = positionStart + itemCount - 1;
        for (; positionEnd >= positionStart; positionEnd--) {
            mItems.remove(positionEnd);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public void removes() {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        int itemCount = mItems.size();
        mItems.clear();
        notifyItemRangeRemoved(0, itemCount - 1);
    }

    public void remove(@IntRange(from = 0) int position) {
        Preconditions.checkNotNull(mItems, "You must set items before invoke this method!");
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void replaces(List<T> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }
}
