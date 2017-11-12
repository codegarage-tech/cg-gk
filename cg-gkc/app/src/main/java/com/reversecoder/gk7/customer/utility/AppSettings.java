package com.reversecoder.gk7.customer.utility;

import android.content.Context;
import android.util.Log;

import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.SessionManager;
import com.customviews.library.utils.UnitConvertionManager;
import com.reversecoder.gk7.customer.model.User;

/**
 * Created by Rashed on 08-Mar-16.
 */
public class AppSettings {

    public static void saveUserDetail(Context context, String userType, User userInfo) {
//        HashMap userDetail=new HashMap();
        SessionManager.setBooleanSetting(context, AllConstants.SESSION_IS_LOGGED_IN, true);
        SessionManager.setStringSetting(context, AllConstants.SESSION_USER_TYPE, userType);
        SessionManager.setStringSetting(context, AllConstants.SESSION_USER_ID, userInfo.getId().toString());
        SessionManager.setObjectSetting(context, AllConstants.SESSION_USER_INFO, userInfo);
        Log.d("userdataoriginal", userInfo.toString());
        Log.d("userdatasaved", SessionManager.getObjectSetting(context, AllConstants.SESSION_USER_INFO, User.class).toString());
//        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_STATUS, ""))) {
        setDriverStatus(context, AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS);
//        }
//        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_WORKING_STATUS, ""))) {
        setDriverWorkingStatus(context, AllConstants.DRIVER_WORKING_STATUS.RIGHT_AWAY);
//        }
//        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(context, AllConstants.SESSION_CUSTOMER_STATUS, ""))) {
        setCustomerStatus(context, AllConstants.CUSTOMER_STATUS.IDLE);
//        }
//        userDetail.put(AllConstants.SESSION_IS_LOGGED_IN,true);
//        userDetail.put(AllConstants.SESSION_USER_TYPE,userType);
//        userDetail.put(AllConstants.SESSION_USER_ID,userInfo.getUser().getId().toString());
//        userDetail.put(AllConstants.SESSION_USER_INFO,userInfo);
//        SessionManager.setHashMapSetting(context, AllConstants.SESSION_USER_DETAIL, userDetail);
    }

    public static void removeUserDetail(Context context) {
        SessionManager.setBooleanSetting(context, AllConstants.SESSION_IS_LOGGED_IN, false);
        SessionManager.setStringSetting(context, AllConstants.SESSION_USER_TYPE, "");
        SessionManager.setStringSetting(context, AllConstants.SESSION_USER_ID, "");
        SessionManager.setObjectSetting(context, AllConstants.SESSION_USER_INFO, new User());
//        SessionManager.setBooleanSetting(context, AllConstants.SESSION_IS_CUSTOMER_SERVICE, false);
//        SessionManager.setBooleanSetting(context, AllConstants.SESSION_IS_DRIVER_SERVICE, false);
        setDriverStatus(context, AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS);
        setCustomerStatus(context, AllConstants.CUSTOMER_STATUS.IDLE);
        AllConstants.CURRENT_LATITUDE = 0.0;
        AllConstants.CURRENT_LONGITUDE = 0.0;
        AllConstants.CURRENT_ADDRESS = "";
    }

    public static void saveDriverState(AllConstants.DRIVER_STATUS driverStatus) {
        switch (driverStatus) {
            case IDLE_WITH_NO_JOBS:
                break;
            case IDLE_WITH_JOBS:
                break;
            case JOB_ACCEPTED:
                break;
            case JOB_STARTED:
                break;
        }
    }

    public static void saveCustomerState(AllConstants.CUSTOMER_STATUS customerStatus) {
        switch (customerStatus) {
            case IDLE:
                break;
            case REQUESTED_FOR_A_TAXI:
                break;
            case REQUEST_ACCEPTED:
                break;
            case RIDE_STARTED:
                break;
        }
    }

//    public static HashMap getUserDetail(Context context) {
//        HashMap userDetail = SessionManager.getHashMapSetting(context, AllConstants.SESSION_USER_DETAIL);
//        if (userDetail != null && !userDetail.isEmpty()) {
//            return userDetail;
//        }
//        return null;
//    }

    public static String getUserType(Context context) {
//        HashMap userDetail = SessionManager.getHashMapSetting(context, AllConstants.SESSION_USER_DETAIL);
        String userType = SessionManager.getStringSetting(context, AllConstants.SESSION_USER_TYPE, "");
        return userType;
    }

    public static String getUserID(Context context) {
//        HashMap userDetail = SessionManager.getHashMapSetting(context, AllConstants.SESSION_USER_DETAIL);
        String userID = SessionManager.getStringSetting(context, AllConstants.SESSION_USER_ID, "");
//        if (userDetail != null && !userDetail.isEmpty()) {
//            return (String) userDetail.get(AllConstants.SESSION_USER_ID);
//        }
        return userID;
    }

    public static boolean isUserLoggedIn(Context context) {
//        HashMap userDetail = SessionManager.getHashMapSetting(context, AllConstants.SESSION_USER_DETAIL);
        boolean isUserLoggedIn = SessionManager.getBooleanSetting(context, AllConstants.SESSION_IS_LOGGED_IN, false);
//        if (userDetail != null && !userDetail.isEmpty()) {
//            return (boolean) userDetail.get(AllConstants.SESSION_IS_LOGGED_IN);
//        }
        return isUserLoggedIn;
    }

    public static User getUserInfo(Context context) {
//        HashMap<String, Object> userDetail = SessionManager.getHashMapSetting(context, AllConstants.SESSION_USER_DETAIL);
        User userDetail = SessionManager.getObjectSetting(context, AllConstants.SESSION_USER_INFO, User.class);
//        if (userDetail != null && !userDetail.isEmpty()) {
//            return (WrapperUser) userDetail.get(AllConstants.SESSION_USER_INFO);
//        }
        Log.d("userfrompre", userDetail.getEmail());
        return userDetail;
    }

    public static String getDriverAveragePerMileFare(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_AVERAGE_PER_MILE_FARE, "");
    }

    public static void setDriverAveragePerMileFare(Context context, String fare) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_DRIVER_AVERAGE_PER_MILE_FARE, fare);
    }

    public static String getDriverAverageBaseFare(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_AVERAGE_BASE_FARE, "");
    }

    public static void setDriverAverageBaseFare(Context context, String fare) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_DRIVER_AVERAGE_BASE_FARE, fare);
    }

    public static double getEstimatedFare(Context context, double distance) {
        double estimatedFare = 0.0;
        double averageBaseFare = 0.0, averagePerMileFare = 0.0, paidDistance = distance;
        try {
            averageBaseFare = Double.parseDouble(getDriverAverageBaseFare(context));
            averagePerMileFare = Double.parseDouble(getDriverAveragePerMileFare(context));
            if (averageBaseFare > 0 && averagePerMileFare > 0 && paidDistance > 0) {
                estimatedFare = averageBaseFare + (averagePerMileFare * paidDistance);
            }
        } catch (Exception ex) {
            averageBaseFare = 0.0;
            averagePerMileFare = 0.0;
            estimatedFare = 0.0;
        }
        return estimatedFare;
    }

    public static double getPaidDistance(Context context, String distance) {
        double paidDistance = 0.0;
        Log.d("getPaidDistance", "distance " + distance + "");
        try {
            if (distance.contains("mi")) {
                Log.d("getPaidDistance", "distance before split " + distance);
                String[] mDistance = distance.split("mi");
                Log.d("getPaidDistance", "split count" + mDistance.length + "0=" + mDistance[0]);
                if (mDistance.length == 1) {
                    Log.d("getPaidDistance", "splited distance " + mDistance[0].trim());
                    paidDistance = Double.parseDouble(mDistance[0].trim());
                    Log.d("paidDistance", paidDistance + "");
                }
            } else if (distance.contains("ft")) {
                String[] mDistance = distance.split("ft");
                if (mDistance.length == 1) {
                    Log.d("paidDistance in foot", Double.parseDouble(mDistance[0].trim()) + "");
                    paidDistance = UnitConvertionManager.convertLength(Double.parseDouble(mDistance[0].trim()), UnitConvertionManager.Units.FOOT, UnitConvertionManager.Units.MILE);
                    Log.d("paidDistance in mile", paidDistance + "");
                }
            }
        } catch (Exception ex) {
            paidDistance = 0.0;
        }
        return paidDistance;
    }

    /*
    * Driver session data
    * */

    public static void setJobAcceptedCustomerDestinationLatitude(Context context, String customerDestinationLatitude) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LATITUDE, customerDestinationLatitude);
    }

    public static String getJobAcceptedCustomerDestinationLatitude(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LATITUDE, "");
    }

    public static void setJobAcceptedCustomerDestinationLongitude(Context context, String customerDestinationLongitude) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LONGITUDE, customerDestinationLongitude);
    }

    public static String getJobAcceptedCustomerDestinationLongitude(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_DESTINATION_LONGITUDE, "");
    }


    public static void setJobAcceptedCustomerLatitude(Context context, String customerLatitude) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_LATITUDE, customerLatitude);
    }

    public static String getJobAcceptedCustomerLatitude(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_LATITUDE, "");
    }

    public static void setJobAcceptedCustomerLongitude(Context context, String customerLongitude) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_LONGITUDE, customerLongitude);
    }

    public static String getJobAcceptedCustomerLongitude(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_LONGITUDE, "");
    }

    public static void setJobAcceptedCustomerID(Context context, String customerID) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_ID, customerID);
    }

    public static String getJobAcceptedCustomerID(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_ID, "");
    }

    public static void setJobAcceptedDriverReceiverType(Context context, String receiverType) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_RECEIVER_TYPE, receiverType);
    }

    public static String getJobAcceptedDriverReceiverType(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_RECEIVER_TYPE, "");
    }

    public static void setJobAcceptedDriverSenderType(Context context, String senderType) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_SENDER_TYPE, senderType);
    }

    public static String getJobAcceptedDriverSenderType(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_SENDER_TYPE, "");
    }

    public static void setDriverStatus(Context context, AllConstants.DRIVER_STATUS status) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_DRIVER_STATUS, status.name());
    }

    public static AllConstants.DRIVER_STATUS getDriverStatus(Context context) {
        return AllConstants.DRIVER_STATUS.valueOf(SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_STATUS, ""));
    }

    public static AllConstants.DRIVER_WORKING_STATUS getDriverWorkingStatus(Context context) {
        return AllConstants.DRIVER_WORKING_STATUS.valueOf(SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_WORKING_STATUS, ""));
    }

    public static void setDriverWorkingStatus(Context context, AllConstants.DRIVER_WORKING_STATUS workingStatus) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_DRIVER_WORKING_STATUS, workingStatus.name());
    }

    public static String getDriverStatusText(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_DRIVER_STATUS_TEXT, "");
    }

    public static void setDriverStatusText(Context context, String driverStatusText) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_DRIVER_STATUS_TEXT, driverStatusText);
    }

    public static void setJobAcceptedCustomerPhone(Context context, String driverPhone) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_PHONE, driverPhone);
    }

    public static String getJobAcceptedCustomerPhone(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_PHONE, "");
    }

    public static void setJobAcceptedDriverOrderID(Context context, String orderID) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_ORDER_ID, orderID);
    }

    public static String getJobAcceptedDriverOrderID(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_ORDER_ID, "");
    }

    /*
    * Customer session data
    * */

    public static void setJobAcceptedDriverID(Context context, String driverID) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_ID, driverID);
    }

    public static String getJobAcceptedDriverID(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_ID, "");
    }

    public static void setJobAcceptedDriverPhone(Context context, String driverPhone) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_PHONE, driverPhone);
    }

    public static String getJobAcceptedDrivePhone(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_DRIVER_PHONE, "");
    }

    public static void setJobAcceptedCustomerReceiverType(Context context, String receiverType) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_RECEIVER_TYPE, receiverType);
    }

    public static String getJobAcceptedCustomerReceiverType(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_RECEIVER_TYPE, "");
    }

    public static void setJobAcceptedCustomerSenderType(Context context, String senderType) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_SENDER_TYPE, senderType);
    }

    public static String getJobAcceptedCustomerSenderType(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_SENDER_TYPE, "");
    }

    public static void setCustomerStatus(Context context, AllConstants.CUSTOMER_STATUS status) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_CUSTOMER_STATUS, status.name());
    }

    public static AllConstants.CUSTOMER_STATUS getCustomerStatus(Context context) {
        return AllConstants.CUSTOMER_STATUS.valueOf(SessionManager.getStringSetting(context, AllConstants.SESSION_CUSTOMER_STATUS, ""));
    }


    public static String getCustomerStatusText(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_CUSTOMER_STATUS_TEXT, "");
    }

    public static void setCustomerStatusText(Context context, String customerStatusText) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_CUSTOMER_STATUS_TEXT, customerStatusText);
    }

    public static void setJobAcceptedCustomerOrderID(Context context, String orderID) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_ORDER_ID, orderID);
    }

    public static String getJobAcceptedCustomerOrderID(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_JOB_ACCEPTED_CUSTOMER_ORDER_ID, "");
    }

    public static void setCustomerDestinationLatitude(Context context, String customerDestinationLatitude) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_CUSTOMER_DESTINATION_LATITUDE, customerDestinationLatitude);
    }

    public static String getCustomerDestinationLatitude(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_CUSTOMER_DESTINATION_LATITUDE, "");
    }

    public static void setCustomerDestinationLongitude(Context context, String customerDestinationLongitude) {
        SessionManager.setStringSetting(context, AllConstants.SESSION_CUSTOMER_DESTINATION_LONGITUDE, customerDestinationLongitude);
    }

    public static String getCustomerDestinationLongitude(Context context) {
        return SessionManager.getStringSetting(context, AllConstants.SESSION_CUSTOMER_DESTINATION_LONGITUDE, "");
    }

}
