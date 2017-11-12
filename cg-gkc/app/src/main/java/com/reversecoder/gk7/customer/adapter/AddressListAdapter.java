package com.reversecoder.gk7.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.customviews.library.event.OnSingleClickListener;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.Address;
import com.reversecoder.gk7.customer.utility.AllConstants;

import java.util.ArrayList;


public class AddressListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Address> mData;
    private static LayoutInflater inflater = null;
    private int selectedItemPosition = -1; // no item selected by default

    public AddressListAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Address>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AllConstants.LIST_SELECTED_POSITION_ADDRESS_BOOK = selectedItemPosition;

    }

    public ArrayList<Address> getData() {
        return mData;
    }

    public void setData(ArrayList<Address> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Address getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_address, null);

        Address address = getItem(position);
        ((TextView) vi.findViewById(R.id.txt_address_title)).setText(address.getAddress_title());
        ((TextView) vi.findViewById(R.id.txt_street)).setText(address.getStreet());
        ((TextView) vi.findViewById(R.id.txt_postal_code)).setText(address.getPostal_code());
        RadioButton rbtnTick = (RadioButton) vi.findViewById(R.id.rbtn_address_tick);

        if (selectedItemPosition == position) {
            rbtnTick.setChecked(true);
            rbtnTick.setVisibility(View.VISIBLE);
        } else {
            rbtnTick.setChecked(false);
            rbtnTick.setVisibility(View.INVISIBLE);
        }

        vi.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                setSelectedItem(parent, position);
            }
        });

        return vi;
    }

    public void setSelectedItem(ViewGroup view,int postion) {
        ListView listView = (ListView)view;
        selectedItemPosition = postion;
        AllConstants.LIST_SELECTED_POSITION_ADDRESS_BOOK = postion;
        notifyDataSetChanged();
        listView.smoothScrollToPosition(postion);
    }

    public Address getSelectedItemData() {
        return getItem(AllConstants.LIST_SELECTED_POSITION_ADDRESS_BOOK);
    }

    public int getSelectedItemPosition() {
        return AllConstants.LIST_SELECTED_POSITION_ADDRESS_BOOK;
    }

}
