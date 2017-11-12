package com.reversecoder.gk7.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.utils.NotificationUtilManager;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.activity.MessageDetailActivity;
import com.reversecoder.gk7.driver.model.Message;
import com.reversecoder.gk7.driver.utility.AllConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class InboxListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Message> mData;
    private static LayoutInflater inflater = null;
    private int selectedItemPosition = -1; // no item selected by default
    AllConstants.SORT_ORDER sortOrder = AllConstants.SORT_ORDER.ASCENDING;

    public InboxListAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Message>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AllConstants.LIST_SELECTED_POSITION_INBOX = selectedItemPosition;

    }

    public ArrayList<Message> getData() {
        return mData;
    }

    public void setData(ArrayList<Message> data) {
        mData = data;
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
            vi = inflater.inflate(R.layout.list_item_inbox, null);

        final Message message = getItem(position);
//        ((TextView) vi.findViewById(R.id.txt_address_title)).setText(address.getAddress_title());
        ((TextView) vi.findViewById(R.id.txt_sender)).setText(message.getSenderName());
        ((TextView) vi.findViewById(R.id.txt_send_date)).setText(message.getCreated());
        ((TextView) vi.findViewById(R.id.txt_message)).setText(message.getMessage());
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

        if(message.getIs_read().equalsIgnoreCase("1")){
            highLightRow(vi);
        }

        vi.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
//                if (!NetworkManager.isConnected(mActivity)) {
//                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
//                } else {
                    Intent messageDetailIntent = new Intent(mActivity, MessageDetailActivity.class);
                    messageDetailIntent.putExtra(AllConstants.INTENT_KEY_MESSAGE_DETAIL, message.toStringObject());
                    messageDetailIntent.putExtra(AllConstants.INTENT_KEY_MESSAGE_DETAIL_FROM_INBOX, true);
                    messageDetailIntent.putExtra(AllConstants.INTENT_KEY_MESSAGE_DETAIL_FROM_OUTBOX, false);
                    mActivity.startActivity(messageDetailIntent);
                    NotificationUtilManager.getNotificationManager(mActivity).cancel(NotificationUtilManager.NOTIFICATION_ID);
//                }
            }
        });

        return vi;
    }

    public void setSelectedItem(ViewGroup view, int postion) {
        ListView listView = (ListView) view;
        selectedItemPosition = postion;
        AllConstants.LIST_SELECTED_POSITION_INBOX = postion;
        notifyDataSetChanged();
        listView.smoothScrollToPosition(postion);
    }

    public void highLightRow(View view){
        view.setBackgroundResource(R.color.grey_bg);
        notifyDataSetChanged();
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
