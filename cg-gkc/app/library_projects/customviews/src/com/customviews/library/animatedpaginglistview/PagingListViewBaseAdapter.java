package com.customviews.library.animatedpaginglistview;

import java.util.ArrayList;

import android.widget.BaseAdapter;

public abstract class PagingListViewBaseAdapter<T> extends BaseAdapter {

	public ArrayList<T> items;

	public PagingListViewBaseAdapter() {
		this.items = new ArrayList<T>();
	}

	public PagingListViewBaseAdapter(ArrayList<T> items) {
		this.items = items;
	}

	public void addMoreItems(ArrayList<T> newItems) {
		this.items.addAll(newItems);
		notifyDataSetChanged();
	}

	public void setDataItem(ArrayList<T> newItems) {
		this.items.clear();
		this.items.addAll(newItems);
		notifyDataSetChanged();
	}

	public void removeAllItems() {
		this.items.clear();
		notifyDataSetChanged();
	}

}
