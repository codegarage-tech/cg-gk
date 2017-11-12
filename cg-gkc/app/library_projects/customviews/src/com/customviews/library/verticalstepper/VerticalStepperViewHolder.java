package com.customviews.library.verticalstepper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customviews.library.R;

/**
 * This class extends the default RecyclerView ViewHolder and adds several methods that are used to display the steps.
 * In uses a variable content that is defined by the StepperContentHolder that should be assigned when binding the ViewHolder!
 */
public class VerticalStepperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * Value for steps that are waiting to be opened by the user.
     */
    public static final String STEP_WAITING = "material.step.waiting";

    /**
     * Value for steps that have been handled and finished.
     */
    public static final String STEP_FINISHED = "material.step.finished";

    /**
     * Value for the step that is currently open
     */
    public static final String STEP_CURRENT = "material.step.current";

    /**
     * Value for steps that could not be finished because of errors, the step will stay open
     */
    public static final String STEP_ERROR = "material.step.error";

    private final TextView stepperTitle;
    private final TextView stepperIconNumber;
    private final ImageView stepperIconFinished;
    private final ImageView stepperIconError;
    private final TextView stepperDescription;

    private final LinearLayout stepperActiveContent;

    private final Button continueButton;
    private final Button cancelButton;

    private final StepperViewHolderListener viewHolderListener;

    private int stepNumber;

    /**
     * Content View of the Step
     */
    private View contentView;

    /**
     * Creates a new ViewHolder from the context of the given parent
     * and attaches the ViewHolder to the given ViewHolderListener for user interaction.
     * @param parent Parent of the ViewHolder, used for context
     * @param listener ViewHolderListener used for user interaction with the step
     */
    public VerticalStepperViewHolder(ViewGroup parent, StepperViewHolderListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_stepper, parent, false));
        viewHolderListener = listener;

        // Make the whole view clickable
        itemView.setOnClickListener(this);

        // Find the row where the content will be visible
        stepperActiveContent = (LinearLayout) itemView.findViewById(R.id.stepper_active_content_row);

        // Replace the placeholder view with the correct content view
        contentView = itemView.findViewById(R.id.step_content);

        // Set the visibility of the content to false. At first we don't want any content visible
        toggleVisibility(stepperActiveContent, false);


        /**
         * Fetch title, description and icons
         */
        stepperTitle = (TextView) itemView.findViewById(R.id.stepper_title);
        //stepperTitle.setText(title);

        stepperIconFinished = (ImageView) itemView.findViewById(R.id.stepper_icon_finished);
        toggleVisibility(stepperIconFinished, false);

        stepperIconError = (ImageView) itemView.findViewById(R.id.stepper_icon_error);
        toggleVisibility(stepperIconFinished, false);

        stepperIconNumber = (TextView) itemView.findViewById(R.id.stepper_icon_number);
        //this.stepperIconNumber.setText(String.valueOf(stepNumber+1));

        stepperDescription = (TextView) itemView.findViewById(R.id.stepper_description);
        toggleVisibility(stepperDescription, false);

        /**
         * Fetch the buttons and set their click listeners
         */
        continueButton = (Button) itemView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        cancelButton = (Button) itemView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        stepNumber = -1;
    }

    /**
     * Updates the View Holder to contain the content given by the StepperContentHolder.
     * Will alter the title, stepNumber and the content view of the ViewHolder if necessary
     * @param stepContent StepperContentHolder that contains the updated content
     */
    @SuppressWarnings("deprecation")
    public void setStep(VerticalStepperContentHolder stepContent) {
        stepNumber = stepContent.getStepIndex();

        // Check the step status, it has to be a valid value!
        String state = stepContent.getStepStatus();
        if(state == null || state.isEmpty()) {
            state = STEP_WAITING;
        }

        boolean stepActive = hasStepStatus(state, STEP_CURRENT, STEP_ERROR);

        // Update the Step Icon Number
        stepperIconNumber.setText(stepContent.getStepIconText());

        // Update the title of the Stepper
        stepperTitle.setText(stepContent.getTitle());

        String description = stepContent.getDescription();
        // Show the description only when there is an actual description available
        if (description != null && !description.isEmpty() && !hasStepStatus(state, STEP_CURRENT)) {
            toggleVisibility(stepperDescription, true);
            stepperDescription.setText(description);
        } else {
            toggleVisibility(stepperDescription, false);
        }

        // Display the correct type of icon: Images when the step is finished or there was an error, Numbers otherwise
        toggleVisibility(stepperIconNumber, hasStepStatus (state, STEP_WAITING, STEP_CURRENT));
        toggleVisibility(stepperIconFinished, hasStepStatus(state, STEP_FINISHED));
        toggleVisibility(stepperIconError, hasStepStatus(state, STEP_ERROR));

        // Display the content part when this step is the current step or there was an error in the step
        toggleVisibility(stepperActiveContent, stepActive);
        if(stepActive) {
            replaceContent(stepContent);
        }
        stepContent.activateStep(stepActive);

        // Alter the text colors. When the step has errors, color the text with the error color. Otherwise use the defaults
        if (hasStepStatus(state, STEP_ERROR)) {
            int errorColor = stepperTitle.getResources().getColor(R.color.colorError);
            stepperTitle.setTextColor(errorColor);
            stepperDescription.setTextColor(errorColor);
        } else {
            stepperTitle.setTextColor(stepperTitle.getResources().getColor(R.color.colorTextPrimary));
            stepperDescription.setTextColor(stepperDescription.getResources().getColor(R.color.colorTextTertiary));
        }

        // If we are waiting, set the number icon to inactive. If we are in the current step, set the icon active
        if (hasStepStatus(state, STEP_WAITING)) {
            stepperIconNumber.setBackgroundResource(R.drawable.vertical_stepper_circle_inactive);
        } else if (hasStepStatus(state, STEP_CURRENT)) {
            stepperIconNumber.setBackgroundResource(R.drawable.vertical_stepper_circle_active);
        }
    }

    /**
     * Replaces the current contentView of the StepperViewHolder with a new one generated by the StepperContentHolder
     * @param stepContent StepperContentHolder used to create the new contentView
     */
    private void replaceContent(VerticalStepperContentHolder stepContent) {
        int index = stepperActiveContent.indexOfChild(this.contentView);
        View newContent = stepContent.inflateNewContentView(stepperActiveContent, this.contentView);
        stepperActiveContent.removeView(this.contentView);
        stepperActiveContent.addView(newContent, index);
        this.contentView = newContent;
    }

    @Override
    public void onClick(View v) {
        if(v == continueButton) {
            viewHolderListener.onContinueClick(stepNumber);
        } else if(v == cancelButton){
            viewHolderListener.onCancelClick(stepNumber);
        } else {
            viewHolderListener.onStepClick(stepNumber);
        }
    }

    /**
     * Toggles the visibility of a view between View.GONE and View.VISIBLE
     * @param view View to be toggled
     * @param visible True: View will be visible, False: View will be GONE
     */
    public static void toggleVisibility(View view, boolean visible) {
        if(visible && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else if(!visible && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * An interface used to directly connect the view to a component
     * that is receiving input events from the three available buttons
     */
    public interface StepperViewHolderListener {

        /**
         * The cancel button in the view holder for the given step number was clicked.
         * @param stepNumber Number of the Step that was clicked. -1 if no valid number was set
         */
        void onCancelClick(int stepNumber);

        /**
         * The continue button in the view holder for the given step number was clicked.
         * @param stepNumber Number of the Step that was clicked. -1 if no valid number was set
         */
        void onContinueClick(int stepNumber);

        /**
         * The view holder for the given step number was clicked.
         * @param stepNumber Number of the Step that was clicked. -1 if no valid number was set
         */
        void onStepClick(int stepNumber);
    }

    /**
     * Returns whether the given StepperContentHolder has a status that matches any of the given statuses
     * @param stepContent StepperContentHolder that needs to be checked
     * @param status A list of statuses that the content holder should be in
     * @return True: Has a status in the list, False: Has no status or doesn't match any of the given
     */
    public static boolean hasStepStatus(VerticalStepperContentHolder stepContent, String... status) {
        return stepContent != null && hasStepStatus(stepContent.getStepStatus(), status);
    }

    /**
     * Returns whether the given status string matches any of the given statuses
     * @param currentStatus Status String that's meant to be checked
     * @param statuses List of statuses that are checked against
     * @return True: Matches a status in the list, False: Has no match in the list
     */
    public static boolean hasStepStatus(String currentStatus, String... statuses) {
        for(String stat : statuses) {
            if(currentStatus.equals(stat)) {
                return true;
            }
        }
        return false;
    }
}
