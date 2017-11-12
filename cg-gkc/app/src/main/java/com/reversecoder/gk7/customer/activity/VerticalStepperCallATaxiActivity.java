package com.reversecoder.gk7.customer.activity;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.customasynctask.AdvancedAsyncTask;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.custommapview.GeocoderAsyncTask;
import com.customviews.library.custommapview.PlacesAutoCompleteAdapter;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.customviews.library.verticalstepper.VerticalStepperAdapter;
import com.customviews.library.verticalstepper.VerticalStepperContentHolder;
import com.customviews.library.verticalstepper.VerticalStepperLayout;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.WrapperCallATaxiResponse;
import com.reversecoder.gk7.customer.model.WrapperEstimatedFare;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;

import org.json.JSONObject;

public class VerticalStepperCallATaxiActivity extends BaseActivity {

    private VerticalStepperLayout vStepperLayout;
    private VerticalStepperAdapter vStepperAdapter;
    private EditText edtHouseInfo, edtNumberOfPassenger;
    private AutoCompleteTextView edtDestinationAddress;
    private TextView txtHouseAddress, txtSourceAddress, txtDestinationAddresss, txtEstimatedFare;
    private Context mContext;
    private String houseNoOrName = "", houseAddress = "", sourceAddress = "", destinationAddress = "", estimatedDuration = "", estimatedDistance = "", estimatedFare = "";
    private double destinationLatitude = 0.0, destinationLongitude = 0.0, sourceLatitude = 0.0, sourceLongitude = 0.0;
    private int numberOfPassengers = 0;
    //    private boolean isNumberOfPassengerFour = false;
//    RadioGroup radioGroupNumberOfPassenger;
//    RadioButton radioButtonPassengerFour, radioButtonPassengerMoreThanFour;
    LinearLayout llHiddenConfirm;
    Button btnConfirm;

    //google place data
    protected GoogleApiClient mGoogleApiClient;
    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(50.044239, -5.689753), new LatLng(60.833552, -0.768491));

    private final VerticalStepperContentHolder.ContentInteractionListener vStepperContentInteractionListener = new VerticalStepperContentHolder.ContentInteractionListener() {
        @Override
        public void onContinueClick(final VerticalStepperContentHolder stepContent) {
            if (stepContent.getStepIndex() == 0) {
//                if (vStepperAdapter.isPreviousStepsCompleted(stepContent.getStepIndex())) {
//                    if (AllSettingsManager.isNullOrEmpty(houseNoOrName)) {
//                        Toast.makeText(mContext, "Please fill house number/name.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    vStepperAdapter.finishStep(stepContent);
//                    AllSettingsManager.forceHideSoftKeyboard(VerticalStepperCallATaxiActivity.this, edtHouseInfo);
//                } else {
//                    Toast.makeText(getParentContext(), "Please complete previous steps.", Toast.LENGTH_SHORT).show();
//                }
                if (AllSettingsManager.isNullOrEmpty(houseNoOrName)) {
                    Toast.makeText(mContext, "You didn't input any house number/name.", Toast.LENGTH_SHORT).show();
                }
                vStepperAdapter.finishStep(stepContent);
                AllSettingsManager.forceHideSoftKeyboard(VerticalStepperCallATaxiActivity.this, edtHouseInfo);
            } else if (stepContent.getStepIndex() == 1) {
                if (vStepperAdapter.isPreviousStepsCompleted(stepContent.getStepIndex())) {
//                    if (isNumberOfPassengerFour) {
//                        numberOfPassengers = 0;
//                    } else {
//                        numberOfPassengers = 1;
//                    }
                    if (numberOfPassengers > 0) {
                        //forcely hide keyboard
                        AllSettingsManager.forceHideSoftKeyboard(VerticalStepperCallATaxiActivity.this, edtNumberOfPassenger);
                        vStepperAdapter.finishStep(stepContent);
                    } else {
                        Toast.makeText(mContext, "Please enter valid passenger number.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getParentContext(), "Please complete previous steps.", Toast.LENGTH_SHORT).show();
                }
            } else if (stepContent.getStepIndex() == 2) {
                if (vStepperAdapter.isPreviousStepsCompleted(stepContent.getStepIndex())) {
                    if (AllSettingsManager.isNullOrEmpty(destinationAddress) || destinationAddress.equalsIgnoreCase("_ _ _")) {
                        Toast.makeText(mContext, "Please fill your destination.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!NetworkManager.isConnected(mContext)) {
                        Toast.makeText(mContext, getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //get destination latitude and longitude, distance and estimated fare here
                    try {
                        //get latitude and longitude from address: 71 pethybridge Road,cardiff,uk
                        GeocoderAsyncTask geocodeAsyncTaskLocation = new GeocoderAsyncTask(mContext, GeocoderAsyncTask.GEOCODER_RETURN_TYPE.LOCATION, new AdvancedAsyncTask.AdvancedAsyncTaskListener() {
                            @Override
                            public void onProgress(Long... progress) {
                            }

                            @Override
                            public void onSuccess(Object result) {
                                Log.d("vertical stepper:", "Success response");
                                Address mAddress = (Address) result;
//                                Toast.makeText(mContext, "latitude: " + mAddress.getLatitude() + "\nlongitude: " + mAddress.getLongitude(), Toast.LENGTH_LONG).show();
                                Log.d("Geocoder result", "latitude: " + mAddress.getLatitude() + "\nlongitude: " + mAddress.getLongitude());

                                if (sourceLatitude != 0.0 && sourceLongitude != 0.0 && mAddress.getLatitude() != 0.0
                                        && mAddress.getLongitude() != 0.0) {


                                    destinationLatitude = mAddress.getLatitude();
                                    destinationLongitude = mAddress.getLongitude();
                                    if (!NetworkManager.isConnected(mContext)) {
                                        Toast.makeText(mContext, getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    doCalculateFare(AppSettings.getUserID(getParentContext()), sourceLatitude + "", sourceLongitude + ""
                                            , destinationLatitude + "", destinationLongitude + "", stepContent);

//                                    //calculate distance from destination latitude and longitude
//                                    ArrayList<LatLng> points = new ArrayList<LatLng>();
//                                    LatLng sourceLatLng = new LatLng(sourceLatitude, sourceLongitude);
//                                    points.add(sourceLatLng);
//
//                                    destinationLatitude = mAddress.getLatitude();
//                                    destinationLongitude = mAddress.getLongitude();
//                                    LatLng destinationLatLng = new LatLng(destinationLatitude, destinationLongitude);
//                                    points.add(destinationLatLng);
//
//                                    DirectionApiManager.getInstance().getDirectionApiUpdate(mContext, points, MapUtilManager.MOOD.DRIVING
//                                            , MapUtilManager.MAP_DISTANCE_UNIT.MILE_FOOT, getResources().getColor(R.color.red_btn_bg_color), new DirectionAPIUpdateData() {
//                                                @Override
//                                                public void getDirectionURLData(String directionData) {
//
//                                                }
//
//                                                @Override
//                                                public void getDistance(String distance) {
//                                                    if (!AllSettingsManager.isNullOrEmpty(distance)) {
//                                                        Log.d("Vertical stepper", "distance " + distance + "");
//                                                        paidDistance = AppSettings.getPaidDistance(getParentContext(), distance);
//                                                        Log.d("Vertical stepper", "paid distance " + paidDistance + "");
////                                                        Toast.makeText(mContext, paidDistance + "", Toast.LENGTH_LONG).show();
//                                                        // calculate estimated fare
//                                                        if (paidDistance > 0) {
//                                                            estimatedFare = AllSettingsManager.formatDoubleValue(AppSettings.getEstimatedFare(getParentContext(), paidDistance), 2);
//                                                            if (estimatedFare > 0) {
//                                                                vStepperAdapter.finishStep(stepContent);
//                                                                //forcely hide keyboard
//                                                                AllSettingsManager.forceHideSoftKeyboard(VerticalStepperCallATaxiActivity.this,edtDestinationAddress);
//                                                            }else{
//                                                                Toast.makeText(mContext, "Fare for selected distance is less than zero.", Toast.LENGTH_LONG).show();
//                                                            }
//                                                        } else {
//                                                            Toast.makeText(mContext, "Selected distance is less than zero.", Toast.LENGTH_LONG).show();
//                                                        }
//                                                    } else {
//                                                        Toast.makeText(mContext, "Could not retrieve distance.", Toast.LENGTH_LONG).show();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void getDuration(String duration) {
//                                                    if(!AllSettingsManager.isNullOrEmpty(duration)){
//                                                        totalDuration=duration;
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void getPolylineOptionsData(PolylineOptions directionData) {
//
//                                                }
//                                            });
                                }
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                Log.d("vertical stepper:", "failure occured");
                                Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        });
                        geocodeAsyncTaskLocation.execute(destinationAddress);
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(getParentContext(), "Please complete previous steps.", Toast.LENGTH_SHORT).show();
                }
            } else if (stepContent.getStepIndex() == 3) {
                if (vStepperAdapter.isPreviousStepsCompleted(stepContent.getStepIndex())) {
                    vStepperAdapter.finishStep(stepContent);
                } else {
                    Toast.makeText(getParentContext(), "Please complete previous steps.", Toast.LENGTH_SHORT).show();
                }
            }

            //check all steps are complete or not
            if (vStepperAdapter.isAllStepsCompleted()) {
                llHiddenConfirm.setVisibility(View.VISIBLE);
            } else {
                llHiddenConfirm.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onCancelClick(VerticalStepperContentHolder stepContent) {
            Log.d("onCancelClick", "step number is: " + stepContent.getStepIndex());
            if (stepContent.getStepIndex() == 0) {
                finish();
            } else {
                vStepperAdapter.cancelStep(stepContent);
            }
        }

        @Override
        public void onStepSelected(VerticalStepperContentHolder stepContent) {
            vStepperAdapter.jumpToStep(stepContent);

            //check all steps are complete or not
            if (vStepperAdapter.isAllStepsCompleted()) {
                llHiddenConfirm.setVisibility(View.VISIBLE);
            } else {
                llHiddenConfirm.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_vertical_stepper_call_a_taxi);
        setActivityTitle(AllConstants.TITLE_ACTIVITY_VERTICAL_STEPPER_CALL_A_TAXI);
        initVerticalStepperCallATaxiUI();
        initVerticalStepperCallATaxiFragmentAction();

        //Google place implementation
        mGoogleApiClient = new GoogleApiClient.Builder(VerticalStepperCallATaxiActivity.this)
                .addApi(Places.GEO_DATA_API)
                .build();

    }

    private void initVerticalStepperCallATaxiUI() {

        mContext = VerticalStepperCallATaxiActivity.this;

        Intent intent = getIntent();
        if (intent != null) {
            houseAddress = intent.getStringExtra(AllConstants.KEY_INTENT_CALL_A_TAXI_ADDRESS);
            sourceLatitude = intent.getDoubleExtra(AllConstants.KEY_INTENT_CALL_A_TAXI_LATITUDE, 0.0);
            sourceLongitude = intent.getDoubleExtra(AllConstants.KEY_INTENT_CALL_A_TAXI_LONGITUDE, 0.0);
        }

        llHiddenConfirm = (LinearLayout) findViewById(R.id.ll_hidden_confirm);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        vStepperLayout = (VerticalStepperLayout) findViewById(R.id.stepper_layout);
        vStepperAdapter = new VerticalStepperAdapter(VerticalStepperAdapter.ARABIC_NUMERAL_GENERATOR);
        vStepperLayout.setAdapter(vStepperAdapter);

        VerticalStepperContentHolder[] holders = new VerticalStepperContentHolder[4];
        for (int i = 0; i < holders.length; i++) {
            if (i == 0) {
                holders[i] = new VerticalStepperContentHolder("House number/name", vStepperContentInteractionListener) {
                    @Override
                    public View inflateNewContentView(ViewGroup parent, View replacementView) {
                        View view = (View) LayoutInflater.from(getParentContext()).inflate(R.layout.vertical_stepper_item_house, null);
                        edtHouseInfo = (EditText) view.findViewById(R.id.edt_house_info);
                        txtHouseAddress = (TextView) view.findViewById(R.id.txt_house_address);

                        final TextWatcher watcherHouseInfo = new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int start, int before, int count) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // TODO Auto-generated method stub
                                houseNoOrName = s.toString();
                                if (houseNoOrName.length() == 0) {
                                    sourceAddress = houseAddress;
                                } else {
                                    sourceAddress = houseNoOrName + ", " + houseAddress;
                                }
                                txtHouseAddress.setText(sourceAddress);
                            }
                        };

                        if (AllSettingsManager.isNullOrEmpty(houseNoOrName)) {
                            sourceAddress = houseAddress;
                        } else {
                            sourceAddress = houseNoOrName + ", " + houseAddress;
                        }
                        txtHouseAddress.setText(sourceAddress);
                        edtHouseInfo.addTextChangedListener(watcherHouseInfo);
                        if (!AllSettingsManager.isNullOrEmpty(houseNoOrName)) {
                            edtHouseInfo.setText(houseNoOrName);
                        }

                        return view;
                    }
                };
            } else if (i == 1) {
                holders[i] = new VerticalStepperContentHolder("Number of passengers", vStepperContentInteractionListener) {
                    @Override
                    public View inflateNewContentView(ViewGroup parent, View replacementView) {
                        View view = (View) LayoutInflater.from(getParentContext()).inflate(R.layout.vertical_stepper_item_number_of_passenger, null);

                        edtNumberOfPassenger = (EditText) view.findViewById(R.id.edt_number_of_passenger);
                        final TextWatcher watcherNumberOfPassenger = new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int start, int before, int count) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // TODO Auto-generated method stub
                                try {
                                    numberOfPassengers = Integer.parseInt(s.toString());
                                } catch (Exception ex) {
                                    numberOfPassengers = 0;
                                }
                            }
                        };

                        edtNumberOfPassenger.addTextChangedListener(watcherNumberOfPassenger);
                        if (numberOfPassengers > 0) {
                            edtNumberOfPassenger.setText(numberOfPassengers + "");
                        }
//                        radioGroupNumberOfPassenger = (RadioGroup) view.findViewById(R.id.rgroup_number_of_passenger);
//                        radioButtonPassengerFour = (RadioButton) view.findViewById(R.id.rbtn_four);
//                        radioButtonPassengerMoreThanFour = (RadioButton) view.findViewById(R.id.rbtn_more_than_four);
//
//                        if (radioGroupNumberOfPassenger.getCheckedRadioButtonId() == R.id.rbtn_four) {
//                            isNumberOfPassengerFour = true;
//                        } else {
//                            isNumberOfPassengerFour = false;
//                        }
//
//                        radioGroupNumberOfPassenger.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                if (checkedId == R.id.rbtn_four) {
//                                    isNumberOfPassengerFour = true;
//                                } else {
//                                    isNumberOfPassengerFour = false;
//                                }
//                            }
//                        });

                        return view;
                    }
                };
            } else if (i == 2) {
                holders[i] = new VerticalStepperContentHolder("Destination", vStepperContentInteractionListener) {
                    @Override
                    public View inflateNewContentView(ViewGroup parent, View replacementView) {
                        View view = (View) LayoutInflater.from(getParentContext()).inflate(R.layout.vertical_stepper_item_destination, null);

                        edtDestinationAddress = (AutoCompleteTextView) view.findViewById(R.id.edt_destination_address);
                        txtSourceAddress = (TextView) view.findViewById(R.id.txt_source_address);
                        txtDestinationAddresss = (TextView) view.findViewById(R.id.txt_destination_address);

                        //Google place implementation
                        mPlacesAdapter = new PlacesAutoCompleteAdapter(VerticalStepperCallATaxiActivity.this, R.layout.list_item_common,
                                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
                        edtDestinationAddress.setOnItemClickListener(mAutocompleteClickListener);
                        edtDestinationAddress.setAdapter(mPlacesAdapter);

                        //watcher for text change
                        final TextWatcher watcherDestination = new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int start, int before, int count) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // TODO Auto-generated method stub
                                destinationAddress = s.toString();
                                if (destinationAddress.length() == 0) {
                                    destinationAddress = "_ _ _";
                                }
                                txtDestinationAddresss.setText(destinationAddress);
                            }
                        };

                        txtSourceAddress.setText(sourceAddress);
                        if (!AllSettingsManager.isNullOrEmpty(destinationAddress)) {
                            txtDestinationAddresss.setText(destinationAddress);
                            edtDestinationAddress.setText(destinationAddress);
                        }
                        edtDestinationAddress.addTextChangedListener(watcherDestination);

                        return view;
                    }
                };
            } else if (i == 3) {
                holders[i] = new VerticalStepperContentHolder("Estimated fare", vStepperContentInteractionListener) {
                    @Override
                    public View inflateNewContentView(ViewGroup parent, View replacementView) {
                        View view = (View) LayoutInflater.from(getParentContext()).inflate(R.layout.vertical_stepper_item_estimated_fare, null);
                        txtEstimatedFare = (TextView) view.findViewById(R.id.txt_estimated_fare);
                        if (!AllSettingsManager.isNullOrEmpty(estimatedFare)) {
                            txtEstimatedFare.setText(getResources().getString(R.string.symbol_pound) + estimatedFare);
                        } else {
                            txtEstimatedFare.setText("_ _ _");
                        }
                        return view;
                    }
                };
            }

        }

        vStepperAdapter.addSteps(holders);
    }


    private void initVerticalStepperCallATaxiFragmentAction() {
        btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!vStepperAdapter.isAllStepsCompleted()) {
                    Toast.makeText(mContext, "Please continue the steps properly", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkManager.isConnected(mContext)) {
                    Toast.makeText(mContext, getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                doCallATaxi(AppSettings.getUserID(mContext), sourceLatitude + "", sourceLongitude + "", sourceAddress, destinationLatitude + ""
                        , destinationLongitude + "", destinationAddress, numberOfPassengers + "", estimatedDuration, estimatedDistance + "", estimatedFare + "", "Please come quickly");
            }
        });
    }

    private void doCallATaxi(final String customerID, final String sourceLatitude, final String sourceLongitude
            , final String sourceAddress, final String destinationLatitude, final String destinationLongitude
            , final String destinationAddress, final String numberOfPassengers, final String duration
            , final String paidDistance, final String estimatedCost, final String note) {


        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                JSONObject param = AllURLs.getCallATaxiParams(customerID, sourceLatitude, sourceLongitude, sourceAddress
                        , destinationLatitude, destinationLongitude, destinationAddress, numberOfPassengers
                        , duration, paidDistance, estimatedCost, note);
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getCallATaxiURL(), param, null);
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperCallATaxiResponse responseData = WrapperCallATaxiResponse.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        AppSettings.setCustomerStatus(mContext, AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI);
                        AppSettings.setJobAcceptedCustomerOrderID(mContext, responseData.getOrderId());
//                        setStatusText("Connecting you with a nearest taxi driver");
                        AppSettings.setCustomerStatusText(mContext, "Connecting you with a nearest taxi driver");
//                        reInitializeCallATaxiUI(AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI);
//                        setStatusText(responseData.getSuccess());
                        AppSettings.setCustomerStatusText(mContext, responseData.getSuccess());
                        AppSettings.setCustomerDestinationLatitude(mContext, destinationLatitude);
                        AppSettings.setCustomerDestinationLongitude(mContext, destinationLongitude);
                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(mContext, responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    private void doCalculateFare(final String customerID, final String sourceLatitude, final String sourceLongitude
            , final String destinationLatitude, final String destinationLongitude, final VerticalStepperContentHolder stepcontent) {

        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                JSONObject param = AllURLs.getCalculateFareParams(customerID, sourceLatitude, sourceLongitude
                        , destinationLatitude, destinationLongitude);
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getCalculateFareURL(), param, null);
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperEstimatedFare responseData = WrapperEstimatedFare.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        estimatedDistance = responseData.getResult().getDistance();
                        estimatedFare = responseData.getResult().getEstimatedFare();
                        estimatedDuration = responseData.getResult().getDuration();
                        vStepperAdapter.finishStep(stepcontent);
                        //forcely hide keyboard
                        AllSettingsManager.forceHideSoftKeyboard(VerticalStepperCallATaxiActivity.this, edtDestinationAddress);
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(mContext, responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                        estimatedDuration = "";
                        estimatedDistance = "";
                        estimatedFare = "";
                    }
                }
            }
        });
    }

    //google place implementation
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
        }
    };


}
