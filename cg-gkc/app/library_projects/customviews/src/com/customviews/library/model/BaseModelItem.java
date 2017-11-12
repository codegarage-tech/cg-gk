package com.customviews.library.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BaseModelItem {
	private String mItemId = "";
	private String mItemTitle = "";
	private int mItemPosition = -1;
	private boolean mItemIsActive = false;

	public BaseModelItem() {
		super();
	}

	public BaseModelItem(String mItemId, String mItemTitle) {
		this();
		this.mItemId = mItemId;
		this.mItemTitle = mItemTitle;
	}

	public BaseModelItem(String mItemId, String mItemTitle, int mItemPosition,
			boolean mItemIsActive) {
		super();
		this.mItemId = mItemId;
		this.mItemTitle = mItemTitle;
		this.mItemPosition = mItemPosition;
		this.mItemIsActive = mItemIsActive;
	}

	public String getItemId() {
		return mItemId;
	}

	public void setItemId(String mItemId) {
		this.mItemId = mItemId;
	}

	public String getItemTitle() {
		return mItemTitle;
	}

	public void setItemTitle(String mItemTitle) {
		this.mItemTitle = mItemTitle;
	}

	public int getItemPosition() {
		return mItemPosition;
	}

	public void setItemPosition(int mItemPosition) {
		this.mItemPosition = mItemPosition;
	}

	public boolean isItemActive() {
		return mItemIsActive;
	}

	public void setIsItemActive(boolean mItemIsActive) {
		this.mItemIsActive = mItemIsActive;
	}

	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("Id", mItemId);
			obj.put("Name", mItemTitle);
			obj.put("ItemPosition", mItemPosition);
			obj.put("ItemIsActive", mItemIsActive);
		} catch (JSONException e) {
			Log.e("DefaultListItem.toString JSONException: ", e.getMessage());
		}
		return obj;
	}

	@Override
	public String toString() {
		return "BaseModelItem [mItemId=" + mItemId + ", mItemTitle="
				+ mItemTitle + ", getItemId()=" + getItemId()
				+ ", getItemTitle()=" + getItemTitle() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
