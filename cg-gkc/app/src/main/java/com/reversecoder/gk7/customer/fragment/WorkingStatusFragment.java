package com.reversecoder.gk7.customer.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.activity.DashboardDriverNavigationDrawerActivity;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AppSettings;

public class WorkingStatusFragment extends Fragment {

    private View parentView;
    RadioButton rbtnRightAway, rbtnNotLooking;
    RadioGroup rGroupStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_working_status, container, false);
        initStatusUI();
        initStatusAction();
        return parentView;
    }

    private void initStatusUI() {
        rbtnRightAway = (RadioButton) parentView.findViewById(R.id.rbtn_right_away);
        rbtnNotLooking = (RadioButton) parentView.findViewById(R.id.rbtn_not_looking);
        rGroupStatus = (RadioGroup) parentView.findViewById(R.id.rgroup_status);
    }

    public DashboardDriverNavigationDrawerActivity getDashboardDriverNavigationDrawerActivityObject() {
        return ((DashboardDriverNavigationDrawerActivity) getActivity());
    }

    private void initStatusAction() {
        //set right away text
        String txtRightAway = getActivity().getResources().getString(R.string.txt_status_right_away);
        SpannableString spannableStringRightAway = new SpannableString(txtRightAway);
        int rightAwayStart = txtRightAway.indexOf(getActivity().getResources().getString(R.string.txt_right_away));
        final int rightAwayStop = rightAwayStart + getActivity().getResources().getString(R.string.txt_right_away).length();
        spannableStringRightAway.setSpan(new StyleSpan(Typeface.BOLD), rightAwayStart, rightAwayStop,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rbtnRightAway.setText(spannableStringRightAway);
        //set not looking text
        String txtNotLooking = getActivity().getResources().getString(R.string.txt_status_not_looking);
        SpannableString spannableStringNotLooking = new SpannableString(txtNotLooking);
        int notLookingStart = txtNotLooking.indexOf(getActivity().getResources().getString(R.string.txt_not_looking));
        int notLookingStop = notLookingStart + getActivity().getResources().getString(R.string.txt_not_looking).length();
        spannableStringNotLooking.setSpan(new StyleSpan(Typeface.BOLD), notLookingStart, notLookingStop,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rbtnNotLooking.setText(spannableStringNotLooking);
        //set primary status of driver
        if (AppSettings.getDriverWorkingStatus(getActivity()) == AllConstants.DRIVER_WORKING_STATUS.RIGHT_AWAY) {
            rbtnRightAway.setChecked(true);
        } else {
            rbtnNotLooking.setChecked(true);
        }
        //set radio group change listener
        rGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_right_away) {
                    if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
                            || AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                        if (getDashboardDriverNavigationDrawerActivityObject().isGoogleApiClient()
                                && getDashboardDriverNavigationDrawerActivityObject().isRequestingLocationUpdates()) {
                            getDashboardDriverNavigationDrawerActivityObject().startLocationUpdates();
                        }
                        getDashboardDriverNavigationDrawerActivityObject().startDriverServices();
                        AppSettings.setDriverWorkingStatus(getActivity(), AllConstants.DRIVER_WORKING_STATUS.RIGHT_AWAY);
                    }
                } else if (checkedId == R.id.rbtn_not_looking) {
                    if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
                            || AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                        if (getDashboardDriverNavigationDrawerActivityObject().isGoogleApiClient()) {
                            getDashboardDriverNavigationDrawerActivityObject().stopLocationUpdates();
                            // forced for getting location update while not looking to right away
                            getDashboardDriverNavigationDrawerActivityObject().setRequestingLocationUpdates(true);
                        }
                        getDashboardDriverNavigationDrawerActivityObject().stopDriverServices();
                        AppSettings.setDriverWorkingStatus(getActivity(), AllConstants.DRIVER_WORKING_STATUS.NOT_WORKING);
                    } else {
                        Toast.makeText(getActivity(), "Please finish your job first.", Toast.LENGTH_SHORT).show();
                        rbtnRightAway.setChecked(true);
                    }
                }
            }
        });
    }

}
