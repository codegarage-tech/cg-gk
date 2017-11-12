package com.reversecoder.gk7.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.Message;
import com.reversecoder.gk7.customer.utility.AllConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class OutboxListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Message> mData;
    private static LayoutInflater inflater = null;
    private int selectedItemPosition = -1; // no item selected by default
    AllConstants.SORT_ORDER sortOrder = AllConstants.SORT_ORDER.ASCENDING;

    public OutboxListAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Message>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AllConstants.LIST_SELECTED_POSITION_OUTBOX = selectedItemPosition;

    }

    public ArrayList<Message> getData() {
        return mData;
    }

    public void setData(ArrayList<Message> data) {
        mData = data;
        sortByDate(mData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Message getItem(int position) {
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
            vi = inflater.inflate(R.layout.list_item_outbox, null);

        Message address = getItem(position);
//        ((TextView) vi.findViewById(R.id.txt_address_title)).setText(address.getAddress_title());
        ((TextView) vi.findViewById(R.id.txt_receiver)).setText(address.getReceiverName());
        ((TextView) vi.findViewById(R.id.txt_send_date)).setText(address.getCreated());
        ((TextView) vi.findViewById(R.id.txt_message)).setText(address.getMessage());
//        ((TextView) vi.findViewById(R.id.txt_postal_code)).setText(address.getPostal_code());
//        RadioButton rbtnTick = (RadioButton) vi.findViewById(R.id.rbtn_address_tick);
//
//        if (selectedItemPosition == position) {
//            rbtnTick.setChecked(true);
//            rbtnTick.setVisibility(View.VISIBLE);
//        } else {
//            rbtnTick.setChecked(false);
//            rbtnTick.setVisibility(View.INVISIBLE);
//        }
//
//        vi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSelectedItem(parent, position);
//            }
//        });

        return vi;
    }

    public void setSelectedItem(ViewGroup view, int postion) {
        ListView listView = (ListView) view;
        selectedItemPosition = postion;
        AllConstants.LIST_SELECTED_POSITION_OUTBOX = postion;
        notifyDataSetChanged();
        listView.smoothScrollToPosition(postion);
    }

    public void setSortOrder(AllConstants.SORT_ORDER sortorder) {
        sortOrder = sortorder;
        sortByDate(mData);
        notifyDataSetChanged();
    }

    private ArrayList<Message> sortByDate(ArrayList<Message> data) {
        final ArrayList<Message> sortData = data;
        Collections.sort(sortData, new Comparator<Message>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1;
            Date date2;

            public int compare(Message msg1, Message msg2) {
                try {
                    date1 = dateFormat.parse(msg1.getCreated());
                    date2 = dateFormat.parse(msg2.getCreated());
                } catch (Exception ex) {
                }
                if (date1 == null || date2 == null) {
                    return 0;
                } else {
                    if (sortOrder == AllConstants.SORT_ORDER.ASCENDING) {
                        return date1.compareTo(date2);
                    } else {
                        return date2.compareTo(date1);
                    }
                }
            }
        });
        return sortData;
    }

}
