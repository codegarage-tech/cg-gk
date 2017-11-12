package com.reversecoder.gk7.driver.utility;

import android.location.Address;
import android.util.Log;
import android.widget.Toast;

import com.customviews.library.customasynctask.AdvancedAsyncTask;
import com.customviews.library.custommapview.GeocoderAsyncTask;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rashed on 3/7/16.
 */
public class AllConstants {

    public static final String APP_NAME = "Taxi No-7";

    public static final String SESSION_IS_LOGGED_IN = "SESSION_IS_LOGGED_IN";
    public static final String SESSION_USER_TYPE = "SESSION_USER_TYPE";
    public static final String SESSION_USER_ID = "SESSION_USER_ID";
    public static final String SESSION_USER_INFO = "SESSION_USER_INFO";
    public static final String SESSION_IS_CUSTOMER_SERVICE = "SESSION_IS_CUSTOMER_SERVICE";
    public static final String SESSION_IS_DRIVER_SERVICE = "SESSION_IS_DRIVER_SERVICE";
    public static final String SESSION_DRIVER_STATUS_TEXT = "SESSION_DRIVER_STATUS_TEXT";
    public static final String SESSION_CUSTOMER_STATUS_TEXT = "SESSION_CUSTOMER_STATUS_TEXT";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_ORDER_ID = "SESSION_JOB_ACCEPTED_CUSTOMER_ORDER_ID";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_LATITUDE = "SESSION_JOB_ACCEPTED_CUSTOMER_LATITUDE";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_LONGITUDE = "SESSION_JOB_ACCEPTED_CUSTOMER_LONGITUDE";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LATITUDE = "SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LATITUDE";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LONGITUDE = "SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LONGITUDE";
    public static final String SESSION_CUSTOMER_DESTINATION_LATITUDE = "SESSION_CUSTOMER_DESTINATION_LATITUDE";
    public static final String SESSION_CUSTOMER_DESTINATION_LONGITUDE = "SESSION_CUSTOMER_DESTINATION_LONGITUDE";
    public static final String SESSION_JOB_ACCEPTED_DRIVER_ORDER_ID = "SESSION_JOB_ACCEPTED_DRIVER_ORDER_ID";
    public static final String SESSION_JOB_ACCEPTED_DRIVER_ID = "SESSION_JOB_ACCEPTED_DRIVER_ID";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_SENDER_TYPE = "SESSION_JOB_ACCEPTED_CUSTOMER_SENDER_TYPE";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_RECEIVER_TYPE = "SESSION_JOB_ACCEPTED_CUSTOMER_RECEIVER_TYPE";
    public static final String SESSION_JOB_ACCEPTED_DRIVER_SENDER_TYPE = "SESSION_JOB_ACCEPTED_DRIVER_SENDER_TYPE";
    public static final String SESSION_JOB_ACCEPTED_DRIVER_RECEIVER_TYPE = "SESSION_JOB_ACCEPTED_DRIVER_RECEIVER_TYPE";
    public static final String SESSION_JOB_ACCEPTED_DRIVER_PHONE = "SESSION_JOB_ACCEPTED_DRIVER_PHONE";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_ID = "SESSION_JOB_ACCEPTED_CUSTOMER_ID";
    public static final String SESSION_JOB_ACCEPTED_CUSTOMER_PHONE = "SESSION_JOB_ACCEPTED_CUSTOMER_PHONE";
    public static final String SESSION_CUSTOMER_STATUS = "SESSION_CUSTOMER_STATUS";
    public static final String SESSION_DRIVER_STATUS = "SESSION_DRIVER_STATUS";
    public static final String SESSION_DRIVER_WORKING_STATUS = "SESSION_DRIVER_WORKING_STATUS";
//    public static final String SESSION_CUSTOMER_LOADING_ICON_CANCELLED = "SESSION_CUSTOMER_LOADING_ICON_CANCELLED";
//    public static final String SESSION_DRIVER_LOADING_ICON_CANCELLED = "SESSION_DRIVER_LOADING_ICON_CANCELLED";
    public static final String SESSION_DRIVER_AVERAGE_PER_MILE_FARE = "SESSION_DRIVER_AVERAGE_PER_MILE_FARE";
    public static final String SESSION_DRIVER_AVERAGE_BASE_FARE = "SESSION_DRIVER_AVERAGE_BASE_FARE";

    public static int LIST_SELECTED_POSITION_ADDRESS_BOOK = -1;
    public static int LIST_SELECTED_POSITION_INBOX = -1;
    public static int LIST_SELECTED_POSITION_OUTBOX = -1;
    public static int LIST_SELECTED_POSITION_MY_JOBS = -1;
    public static int LIST_SELECTED_POSITION_BID_JOBS = -1;
    public static int LIST_SELECTED_POSITION_NEW_JOBS = -1;

    public static final String TEXT_DRIVER = "driver";
    public static final String TEXT_CUSTOMER = "customer";

    public static final String FRAGMENT_NAME_CALL_A_TAXI = "FRAGMENT_NAME_CALL_A_TAXI";
    public static final String FRAGMENT_NAME_INBOX = "FRAGMENT_NAME_INBOX";
    public static final String FRAGMENT_NAME_OUTBOX = "FRAGMENT_NAME_OUTBOX";
    public static final String FRAGMENT_NAME_MY_JOBS = "FRAGMENT_NAME_MY_JOBS";
    public static final String FRAGMENT_NAME_BID_JOBS = "FRAGMENT_NAME_BID_JOBS";
    public static final String FRAGMENT_NAME_NEW_JOBS = "FRAGMENT_NAME_NEW_JOBS";
    public static final String FRAGMENT_NAME_JOB_STATUS = "FRAGMENT_NAME_JOB_STATUS";
    public static final String FRAGMENT_NAME_WORKING_STATUS = "FRAGMENT_NAME_WORKING_STATUS";

    public static final String INTENT_KEY_NOTIFICATION_TO_INBOX = "INTENT_KEY_NOTIFICATION_TO_INBOX";
    public static final String INTENT_KEY_NOTIFICATION_TO_NORMAL_JOB = "INTENT_KEY_NOTIFICATION_TO_NORMAL_JOB";
    public static final String INTENT_KEY_NOTIFICATION_TO_BID_JOB = "INTENT_KEY_NOTIFICATION_TO_BID_JOB";
    public static final String INTENT_KEY_NOTIFICATION_TO_ACCEPT_JOB = "INTENT_KEY_NOTIFICATION_TO_ACCEPT_JOB";
    public static final String INTENT_KEY_MESSAGE_DETAIL_FROM_OUTBOX = "INTENT_KEY_MESSAGE_DETAIL_FROM_OUTBOX";
    public static final String INTENT_KEY_MESSAGE_DETAIL_FROM_INBOX = "INTENT_KEY_MESSAGE_DETAIL_FROM_INBOX";
    public static final String INTENT_KEY_MESSAGE_DETAIL = "INTENT_KEY_MESSAGE_DETAIL";

    public static int NOTIFICATION_ID_NEW_MESSAGE = 420;
    public static int PENDING_INTENT_REQUEST_ID_NEW_MESSAGE = 420;
    public static int NOTIFICATION_ID_NEW_NORMAL_JOB = 421;
    public static int PENDING_INTENT_REQUEST_ID_NEW_NORMAL_JOB = 421;
    public static int NOTIFICATION_ID_NEW_BID_JOB = 422;
    public static int PENDING_INTENT_REQUEST_ID_NEW_BID_JOB = 422;
    public static int NOTIFICATION_ID_ACCEPT_JOB = 423;
    public static int PENDING_INTENT_REQUEST_ID_ACCEPT_JOB = 423;

    public static final String TITLE_ACTIVITY_INBOX = "Inbox";
    public static final String TITLE_ACTIVITY_OUTBOX = "Outbox";
    public static final String TITLE_ACTIVITY_WORKING_STATUS = "Working Status";
    public static final String TITLE_ACTIVITY_CUSTOMER_DASHBOARD = "Customer Dashboard";
    public static final String TITLE_ACTIVITY_DRIVER_DASHBOARD = "Driver Dashboard";
    public static final String TITLE_ACTIVITY_VERTICAL_STEPPER_CALL_A_TAXI = "Call A Taxi";
    public static final String TITLE_ACTIVITY_ACCEPT_JOB_DETAIL = "Job Detail";
    public static final String TITLE_ACTIVITY_JOB_STATUS = "Job Status";
    public static final String TITLE_ACTIVITY_CALL_A_TAXI = "Call A Taxi";
    public static final String TITLE_ACTIVITY_MY_JOBS = "My Jobs";
    public static final String TITLE_ACTIVITY_BID_JOBS = "Bid Jobs";
    public static final String TITLE_ACTIVITY_NEW_JOBS = "New Jobs";
    public static final String TITLE_ACTIVITY_MESSAGE_DETAIL = "Message Detail";
    public static final String TITLE_ACTIVITY_SEND_MESSAGE = "Send Message";
    public static final String TITLE_ACTIVITY_LOG_IN_CUSTOMER = "Customer LogIn";
    public static final String TITLE_ACTIVITY_LOG_IN_DRIVER= "Driver LogIn";
    public static final String TITLE_ACTIVITY_LOG_IN= "Log In";

    public static final String KEY_INTENT_CALL_A_TAXI_ADDRESS= "KEY_INTENT_CALL_A_TAXI_ADDRESS";
    public static final String KEY_INTENT_CALL_A_TAXI_LATITUDE= "KEY_INTENT_CALL_A_TAXI_LATITUDE";
    public static final String KEY_INTENT_CALL_A_TAXI_LONGITUDE= "KEY_INTENT_CALL_A_TAXI_LONGITUDE";
    public static final String KEY_INTENT_ACCEPT_JOB_DETAIL= "KEY_INTENT_ACCEPT_JOB_DETAIL";
    public static final String KEY_INTENT_ACCEPT_JOB_TYPE= "KEY_INTENT_ACCEPT_JOB_TYPE";

    public static final long SERVICE_NOTIFY_INTERVAL = 30 * 1000;
    public static final long INBOX_NOTIFY_INTERVAL = 30 * 1000;

    public static enum USER_TYPE {DRIVER, CUSTOMER}

    public static enum ADDRESS_TYPE {ADDRESS_BOOK, CURRENT_LOCATION, CUSTOM_ADDRESS, NOTHING}

    public static enum SORT_ORDER {ASCENDING, DESCENDING}

    public static enum DRIVER_STATUS {IDLE_WITH_NO_JOBS, IDLE_WITH_JOBS, JOB_ACCEPTED, JOB_STARTED,JOB_COMPLETED}

    public static enum DRIVER_WORKING_STATUS {RIGHT_AWAY, NOT_WORKING}

    public static enum CUSTOMER_STATUS {IDLE, REQUESTED_FOR_A_TAXI, REQUEST_ACCEPTED, RIDE_STARTED,RIDE_COMPLETED}

    public static enum SERVICE_RETURN_TYPE {DRIVER_NEW_JOB_ALERT, NEW_MESSAGE_ALERT, CUSTOMER_ACCEPTED_JOB_ALERT, CUSTOMER_GET_DRIVER_CURRENT_LOCATION}

    public static enum DRIVER_JOB_TYPE {NORMAL,BID}

    public static double CURRENT_LATITUDE = 0.0, CURRENT_LONGITUDE = 0.0;
    public static String CURRENT_ADDRESS = "";
    public static double PADDING_RATIO = 0;
//    public static AllConstants.DRIVER_STATUS driverStatus = AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS;
//    public static AllConstants.CUSTOMER_STATUS customerStatus = AllConstants.CUSTOMER_STATUS.IDLE;


    //task to be done
    /*
    * 1. messaging from map for both customer and driver
    * 2. show all markers on the visible screen
    * 3. draw path and update location on map
    * 4. show dialog while driver with in 300m
    * */

//    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//    10£-15£


//        //get address from latitude: 51.4822956 longitude: -3.254026
//        GeocoderAsyncTask geocodeAsyncTaskAddress = new GeocoderAsyncTask(mContext, GeocoderAsyncTask.GEOCODER_RETURN_TYPE.ADDRESS, new AdvancedAsyncTask.AdvancedAsyncTaskListener() {
//            @Override
//            public void onProgress(Long... progress) {
//
//            }
//
//            @Override
//            public void onSuccess(Object result) {
//                Log.d("vertical stepper:", "Success response");
//                Address mAddress = (Address) result;
//                String address = GeocoderAsyncTask.getAddress(mAddress);
//                Toast.makeText(mContext, "Address: " + address, Toast.LENGTH_LONG).show();
//                Log.d("Geocoder result", "Address: " + address);
//            }
//
//            @Override
//            public void onFailure(Exception exception) {
//                Log.d("vertical stepper:", "failure occured");
//                Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//        geocodeAsyncTaskAddress.execute(new LatLng(51.4822956, -3.254026));
//
//
//        Log.d("vertical stepper:", "request sent");
//    } catch (Exception e) {
//        e.printStackTrace();
//        Log.d("vertical stepper:", "got exception sent");
//    }

}
