package com.reversecoder.gk7.driver.utility;

import android.util.Log;

import com.customviews.library.model.TaskParameter;

import org.json.JSONObject;

/**
 * Created by rashed on 3/2/16.
 */
public class AllURLs {

    public static String getLoginURL() {
        String url = "http://taxino7.com/api/login";
        Log.d("getLoginURL", url);
        return url;
    }

    public static JSONObject getLoginParams(String userType, String email, String password) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("type", userType)
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .getJSONParam();
        Log.d("getLoginParams", params.toString());
        return params;
    }

    public static String getDriverNewJobsURL(String driverID) {
        String url = "http://taxino7.com/api/drivernewjobs/did/" + driverID;
        Log.d("getDriverNewJobsURL", url);
        return url;
    }

    public static String getDriverBidJobsURL(String driverID) {
        String url = "http://taxino7.com/api/driverbidjobs/did/" + driverID;
        Log.d("getDriverBidJobsURL", url);
        return url;
    }

    public static String getDriverJobsURL(String driverID) {
        String url = "http://taxino7.com/api/driverjobs/did/" + driverID;
        Log.d("getDriverJobsURL", url);
        return url;
    }

    public static String getDriverAcceptJobURL(String driverID, String jobOrderID) {
        String url = "http://taxino7.com/api/acceptjob/did/" + driverID + "/orderId/" + jobOrderID;
        Log.d("getDriverAcceptJobURL", url);
        return url;
    }

//    public static String getDriverCompleteJobURL(String driverID, String orderID) {
//        String url = "http://taxino7.com/api/iscomplete/did/" + driverID + "/orderId/" + orderID;
//        Log.d("getDriverCompleteJobURL", url);
//        return url;
//    }

    public static String getDriverCompleteJobURL() {
        String url = "http://taxino7.com/api/jobcompleted";
        Log.d("getDriverCompleteJobURL", url);
        return url;
    }

    public static JSONObject getDriverCompleteJobParam(String driverID, String orderID
            , String currentLatitude, String currentLongitude, String currentAddress) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("did", driverID)
                .addJSONParam("orderId", orderID)
                .addJSONParam("lat", currentLatitude)
                .addJSONParam("lon", currentLongitude)
                .addJSONParam("address_string", currentAddress)
                .getJSONParam();
        Log.d("driverCompleteJobParam", params.toString());
        return params;
    }

    public static String getInboxMessagesURL(String driverOrCustomerID) {
        String url = "http://taxino7.com/api/inbox/cid/" + driverOrCustomerID;
        Log.d("getInboxMessagesURL", url);
        return url;
    }

    public static String getOutboxMessagesURL(String driverOrCustomerID) {
        String url = "http://taxino7.com/api/outbox/cid/" + driverOrCustomerID;
        Log.d("getInboxMessagesURL", url);
        return url;
    }

    public static String getSendMessageURL() {
        String url = "http://taxino7.com/api/sendmessage/";
        Log.d("getSendMessageURL", url);
        return url;
    }

    public static JSONObject getSendMessageParams(String driverOrCustomerID, String orderID
            , String receiverID, String receiverType, String senderType, String message) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("cid", driverOrCustomerID)
                .addJSONParam("order_id", orderID)
                .addJSONParam("receiver_id", receiverID)
                .addJSONParam("receiver_type", receiverType)
                .addJSONParam("sender_type", senderType)
                .addJSONParam("message", message)
                .getJSONParam();
        Log.d("getSendMessageParams", params.toString());
        return params;
    }

    public static String getDeleteMessageURL(String messageID, String driverOrCustomerID) {
        String url = "http://taxino7.com/api/deletemessage/id/" + messageID + "/cid/" + driverOrCustomerID;
        Log.d("getDeleteMessageURL", url);
        return url;
    }

    public static String getMarkAsReadMessageURL(String messageID, String driverOrCustomerID) {
        String url = "http://taxino7.com/api/markasread/id/" + messageID + "/cid/" + driverOrCustomerID;
        Log.d("getMarkAsReadMessageURL", url);
        return url;
    }

    public static String getNewMessageAlertURL(String driverOrCustomerID) {
        String url = "http://taxino7.com/api/newmsgalert/cid/" + driverOrCustomerID;
        Log.d("getNewMessageAlertURL", url);
        return url;
    }

    public static String getAreaURL() {
        String url = "http://taxino7.com/api/areas";
        Log.d("getAreaURL", url);
        return url;
    }

    public static String getAddressURL(String customerID) {
        String url = "http://taxino7.com/api/address/cid/" + customerID;
        Log.d("getAddressURL", url);
        return url;
    }

    public static String getAddOrEditAddressURL() {
        String url = "http://taxino7.com/api/saveaddress";
        Log.d("getAddOrEditAddressURL", url);
        return url;
    }

    public static JSONObject getAddAddressParams(String customerID, String addressTitle
            , String streetName, String areaID, String postalCode) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("cid", customerID)
                .addJSONParam("address_title", addressTitle)
                .addJSONParam("street", streetName)
                .addJSONParam("area_id", areaID)
                .addJSONParam("postal_code", postalCode)
                .getJSONParam();
        Log.d("getAddAddressParams", params.toString());
        return params;
    }

    public static JSONObject getEditAddressParams(String customerID, String addressTitle
            , String streetName, String areaID, String postalCode, String editableAddressID, String editableAddressStatus) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("cid", customerID)
                .addJSONParam("address_title", addressTitle)
                .addJSONParam("street", streetName)
                .addJSONParam("area_id", areaID)
                .addJSONParam("postal_code", postalCode)
                .addJSONParam("id", editableAddressID)
                .addJSONParam("status", editableAddressStatus)
                .getJSONParam();
        Log.d("getEditAddressParams", params.toString());
        return params;
    }

//    public static String getCallATaxiURL() {
//        String url = "http://taxino7.com/api/callataxi/";
//        Log.d("getCallATaxiURL", url);
//        return url;
//    }
//
//    public static JSONObject getCallATaxiUsingAddressBookParams(String customerID, String messageID, String addressTitle, String driveToAddress) {
//        JSONObject params = TaskParameter.getInstance()
//                .addJSONParam("cid", customerID)
//                .addJSONParam("address_type", "1")
//                .addJSONParam("existing_address", messageID + "|" + addressTitle)
//                .addJSONParam("drive_to_address", driveToAddress)
//                .getJSONParam();
//        Log.d("callATaxiUsingAddBook", params.toString());
//        return params;
//    }
//
//    public static JSONObject getCallATaxiUsingCurrentLocationParams(String customerID, String latitude, String longitude, String driveFromAddress, String driveToAddress) {
//        JSONObject params = TaskParameter.getInstance()
//                .addJSONParam("cid", customerID)
//                .addJSONParam("address_type", "2")
//                .addJSONParam("lat", latitude)
//                .addJSONParam("lon", longitude)
//                .addJSONParam("drive_from_address", driveFromAddress)
//                .addJSONParam("drive_to_address", driveToAddress)
//                .getJSONParam();
//        Log.d("callATaxiUsingCurrLoc", params.toString());
//        return params;
//    }
//
//    public static JSONObject getCallATaxiUsingCustomAddressParams(String customerID, String driveFromAddress, String driveToAddress) {
//        JSONObject params = TaskParameter.getInstance()
//                .addJSONParam("cid", customerID)
//                .addJSONParam("address_type", "3")
//                .addJSONParam("drive_from_address_custom", driveFromAddress)
//                .addJSONParam("drive_to_address", driveToAddress)
//                .getJSONParam();
//        Log.d("callATaxiUsingCusAdd", params.toString());
//        return params;
//    }

    public static String getSendDriverUpdatedLocationURL() {
        String url = "http://taxino7.com/api/updatelocation";
        Log.d("sendUpdatedLocURL", url);
        return url;
    }

    public static JSONObject getSendDriverUpdatedLocationParams(String dirverID, String latitude, String longitude, String address) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("cid", dirverID)
                .addJSONParam("type", "2")
                .addJSONParam("lat", latitude)
                .addJSONParam("lon", longitude)
                .addJSONParam("address_string", address)
                .getJSONParam();
        Log.d("sendUpdatedLocParams", params.toString());
        return params;
    }

    public static String getNewJobAlertURL(String driverID) {
        String url = "http://taxino7.com/api/newjobalert/did/" + driverID;
        Log.d("getNewJobAlertURL", url);
        return url;
    }

    public static String getCallATaxiURL() {
        String url = "http://taxino7.com/api/calling";
        Log.d("getCallATaxiURL", url);
        return url;
    }

    public static JSONObject getCallATaxiParams(String customerID, String sourceLatitude, String sourceLongitude
            , String sourceAddress, String destinationLatitude, String destinationLongitude, String destinationAddress
            , String numberOfPassengers, String duration, String paidDistance, String estimatedCost, String note) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("cid", customerID)
                .addJSONParam("lat", sourceLatitude)
                .addJSONParam("lon", sourceLongitude)
                .addJSONParam("drive_from_address", sourceAddress)
                .addJSONParam("to_lat", destinationLatitude)
                .addJSONParam("to_lon", destinationLongitude)
                .addJSONParam("drive_to_address", destinationAddress)
                .addJSONParam("is_greater_than_four", numberOfPassengers)
                .addJSONParam("duration", duration)
                .addJSONParam("paid_distance", paidDistance)
                .addJSONParam("estimated_cost", estimatedCost)
                .addJSONParam("note", note)
                .getJSONParam();
        Log.d("CallATaxiParams", params.toString());
        return params;
    }

    public static String getDriversUpdatedLocationURL() {
        String url = "http://taxino7.com/api/driverlocation";
        Log.d("driverUpdatedLocURL", url);
        return url;
    }

    public static String getSpecificDriverUpdatedLocationURL(String driverID) {
        String url = "http://taxino7.com/api/driveruplocation/did/" + driverID;
        Log.d("specificDriverUpLocURL", url);
        return url;
    }

    public static String getAcceptJobNotificationURL(String customerID, String orderID) {
        String url = "http://taxino7.com/api/acceptnotify/cid/" + customerID + "/orderId/" + orderID;
        Log.d("AcceptJobNotifiyURL", url);
        return url;
    }

    public static String getStartJobURL(String driverID, String orderID) {
        String url = "http://taxino7.com/api/startjob/did/" + driverID + "/orderId/" + orderID;
        Log.d("getStartJobURL", url);
        return url;
    }

    public static String getPaidURL() {
        String url = "http://taxino7.com/api/pay";
        Log.d("getPaidURL", url);
        return url;
    }


    public static JSONObject getPaidParams(String driverID, String orderID) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("did", driverID)
                .addJSONParam("orderId", orderID)
                .getJSONParam();
        Log.d("getPaidParams", params.toString());
        return params;
    }

    public static String getUnpaidURL() {
        String url = "http://taxino7.com/api/upay";
        Log.d("getUnpaidURL", url);
        return url;
    }

    public static JSONObject getUnpaidParams(String driverID, String orderID) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("did", driverID)
                .addJSONParam("orderId", orderID)
                .getJSONParam();
        Log.d("getUnpaidParams", params.toString());
        return params;
    }

    public static String getCalculateFareURL() {
        String url = "http://taxino7.com/api/calculatefare";
        Log.d("getCalculateFareURL", url);
        return url;
    }

    public static JSONObject getCalculateFareParams(String customerID, String sourceLatitude
            , String sourceLongitude, String destinationLatitude, String destinationLongitude) {
        JSONObject params = TaskParameter.getInstance()
                .addJSONParam("cid", customerID)
                .addJSONParam("from_lat", sourceLatitude)
                .addJSONParam("from_lon", sourceLongitude)
                .addJSONParam("to_lat", destinationLatitude)
                .addJSONParam("to_lon", destinationLongitude)
                .getJSONParam();
        Log.d("getCalculateFareParams", params.toString());
        return params;
    }

}
