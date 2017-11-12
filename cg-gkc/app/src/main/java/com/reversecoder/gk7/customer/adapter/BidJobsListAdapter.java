package com.reversecoder.gk7.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.event.OnSingleClickListener;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.activity.AcceptJobDetailActivity;
import com.reversecoder.gk7.customer.model.DriverBidJob;
import com.reversecoder.gk7.customer.model.NewJobAlertDetail;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AppSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class BidJobsListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<DriverBidJob> mData;
    private static LayoutInflater inflater = null;
    private int selectedItemPosition = -1; // no item selected by default
    AllConstants.SORT_ORDER sortOrder = AllConstants.SORT_ORDER.ASCENDING;

    public BidJobsListAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<DriverBidJob>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AllConstants.LIST_SELECTED_POSITION_BID_JOBS = selectedItemPosition;

    }

    public ArrayList<DriverBidJob> getData() {
        return mData;
    }

    public void setData(ArrayList<DriverBidJob> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DriverBidJob getItem(int position) {
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
            vi = inflater.inflate(R.layout.list_item_bid_jobs, null);

        final DriverBidJob driverJob = getItem(position);
//        ((TextView) vi.findViewById(R.id.txt_address_title)).setText(address.getAddress_title());
        ((TextView) vi.findViewById(R.id.txt_sender)).setText(driverJob.getCustomerName());
        ((TextView) vi.findViewById(R.id.txt_send_date)).setText(driverJob.getCreated());
        ((TextView) vi.findViewById(R.id.txt_drive_from)).setText("From: " + driverJob.getDrive_from_address());
        ((TextView) vi.findViewById(R.id.txt_drive_to)).setText("To: " + driverJob.getDrive_to_address());
        ((TextView) vi.findViewById(R.id.txt_message)).setText(driverJob.getOrderStatusResult());
        Button acceptBidJob = (Button) vi.findViewById(R.id.btn_accept_bid_job);
        if (driverJob.getOrderStatus().equalsIgnoreCase("2")) {
            acceptBidJob.setVisibility(View.VISIBLE);
        } else {
            acceptBidJob.setVisibility(View.INVISIBLE);
        }
        acceptBidJob.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (AppSettings.getDriverStatus(mActivity) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
                        || AppSettings.getDriverStatus(mActivity) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
//                    Toast.makeText(mActivity, "Under processing", Toast.LENGTH_SHORT).show();
                    //accept job

                    NewJobAlertDetail jobDetail = new NewJobAlertDetail();
                    jobDetail.setOrderId(driverJob.getOrderId());
                    jobDetail.setCustomerId(driverJob.getCustom_id());
                    jobDetail.setCustomerLat(driverJob.getDrive_from_lat());
                    jobDetail.setCustomerLon(driverJob.getDrive_from_lon());
                    jobDetail.setCustomerAddress(driverJob.getDrive_from_address());
                    jobDetail.setIsBid(driverJob.getIs_bid());
                    jobDetail.setIsPhone(driverJob.getIs_phone());
                    jobDetail.setIsCorporate(driverJob.getIs_corporate());
                    jobDetail.setOrderRequestId(driverJob.getOrderStatus());
                    jobDetail.setCustomerName(driverJob.getCustomerName());
                    jobDetail.setCustomerEmail(driverJob.getCustomerEmail());
                    jobDetail.setCustomerMobile(driverJob.getCustomerMobile());
                    jobDetail.setCustomerPhone(driverJob.getCustomerPhone());
                    jobDetail.setEstimatedFare(driverJob.getCost());
                    jobDetail.setCustomerDestinationLat(driverJob.getDrive_to_lat());
                    jobDetail.setCustomerDestinationLon(driverJob.getDrive_to_lon());
                    jobDetail.setCustomerDestinationAddress(driverJob.getDrive_to_address());
                    jobDetail.setIsFourPassenger(driverJob.getIs_four());
                    jobDetail.setDistance(driverJob.getPaid_distance());


                    Intent intent = new Intent(mActivity, AcceptJobDetailActivity.class);
                    intent.putExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_DETAIL, NewJobAlertDetail.getObjectData(jobDetail));
                    intent.putExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_TYPE, AllConstants.DRIVER_JOB_TYPE.BID.name());
                    mActivity.startActivity(intent);

                } else {
                    Toast.makeText(mActivity, "You can not accept before finishing current job.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return vi;
    }

    public void setSelectedItem(ViewGroup view, int postion) {
        ListView listView = (ListView) view;
        selectedItemPosition = postion;
        AllConstants.LIST_SELECTED_POSITION_BID_JOBS = postion;
        notifyDataSetChanged();
        listView.smoothScrollToPosition(postion);
    }

    public void setSortOrder(AllConstants.SORT_ORDER sortorder) {
        sortOrder = sortorder;
        sortByDate(mData);
        notifyDataSetChanged();
    }

    private ArrayList<DriverBidJob> sortByDate(ArrayList<DriverBidJob> data) {
        final ArrayList<DriverBidJob> sortData = data;
        Collections.sort(sortData, new Comparator<DriverBidJob>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1;
            Date date2;

            public int compare(DriverBidJob job1, DriverBidJob job2) {
                try {
                    date1 = dateFormat.parse(job1.getCreated());
                    date2 = dateFormat.parse(job2.getCreated());
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
