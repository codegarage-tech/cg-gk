package com.customviews.library.verticalstepper;

import android.view.View;
import android.view.ViewGroup;


public abstract class VerticalStepperContentHolder {

    public static final int INVALID = -1;
    private int stepIndex;
    private String stepIconText;
    private String title;
    private String description;
    private String stepStatus;
    private boolean visible;
    private VerticalStepperAdapter mAdapter;

    private final ContentInteractionListener contentInteractionListener;

    public VerticalStepperContentHolder(String stepIconText, String title, String description, ContentInteractionListener contentInteractionListener) {
        this.stepIndex = INVALID;
        this.stepIconText = stepIconText;
        this.title = title;
        this.description = description;
        this.stepStatus = VerticalStepperViewHolder.STEP_WAITING;
        this.contentInteractionListener = contentInteractionListener;
    }

    public VerticalStepperContentHolder(String title, String description, ContentInteractionListener contentInteractionListener) {
        this(" ", title, description, contentInteractionListener);
    }

    public VerticalStepperContentHolder(String title, ContentInteractionListener contentInteractionListener) {
        this(title, "", contentInteractionListener);
    }


    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getStepIconText() {
        return stepIconText;
    }

    public void setStepIconText(String stepIconText, boolean notifyAdapter) {
        this.stepIconText = stepIconText;
        if (notifyAdapter) {
            updateStepperViewHolder();
        }
    }


    public void setStepIconText(String stepIconText) {
        setStepIconText(stepIconText, true);
    }


    public boolean hasValidStepNumber() {
        return stepIndex > INVALID;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Updates the title of the step
     *
     * @param title
     */
    public void setTitle(String title) {
        setTitle(title, true);
    }
    
    public void setTitle(String title, boolean notifyAdapter) {
        this.title = title;
        if (notifyAdapter) {
            updateStepperViewHolder();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        setDescription(description, true);
    }

    public void setDescription(String description, boolean notifyAdapter) {
        this.description = description;
        if (notifyAdapter) {
            updateStepperViewHolder();
        }
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
        setStepStatus(stepStatus, true);
    }

    public void setStepStatus(String stepStatus, boolean notifyAdapter) {
        this.stepStatus = stepStatus;
        if (notifyAdapter) {
            updateStepperViewHolder();
        }
    }

    public void attachToAdapter(VerticalStepperAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void detachFromAdapter() {
        this.mAdapter = null;
    }

    protected void updateStepperViewHolder() {
        if (mAdapter != null && hasValidStepNumber()) {
            mAdapter.notifyItemChanged(stepIndex);
        }
    }

    /**
     * This method is called whenever the StepperAdapter has to rebind the StepperContent to a view holder.
     * This should happen whenever there was a change of StepStatus or any internal values.
     * Use this method for background processes that should be invisible to the user!
     *
     * The default implementation is left empty, override this method if you wish to perform tasks after an update.
     */
    public void onStepUpdate() {
    }

    public void activateStep(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            if (visible) {
                onStepActivated();
            } else {
                onStepDeactivated();
            }
        }
    }

    /**
     * This method is called whenever the step was disabled by the Adapter.
     * This can happen when the step was finished or when the user decides to move to another step before finishing.
     *
     * The default implementation is left empty, override this method if you wish to perform (or stop) tasks after deactivation.
     * Note: This method should stop any tasks started by onStepActivated!
     */
    protected void onStepDeactivated() {
    }

    /**
     * This method is called whenever the step was enabled by the Adapter.
     * This can happen when the step is selected as the current step or when there has been an error in the step.
     *
     * The default implementation is left empty, override this method if you wish to perform (or start) tasks after activation.
     * Note: This method should only start tasks that are once again stopped by onStepDeactivated!
     */
    protected void onStepActivated() {
    }

    /**
     * This method creates a new View to display the content of this step.
     * The context for the new view comes from the parent and any layout options can be found in the replacementView.
     *
     * @param parent          Parent of the new View, use for Context
     * @param replacementView View that is going to be replaced afterwards, use for layout options
     * @return A new View that shows the current content of the step
     */
    public abstract View inflateNewContentView(ViewGroup parent, View replacementView);

    public void onCancelClick() {
        contentInteractionListener.onCancelClick(this);
    }

    public void onContinueClick() {
        contentInteractionListener.onContinueClick(this);
    }

    public void onStepSelected() {
        contentInteractionListener.onStepSelected(this);
    }

    /**
     * This listener attaches to the StepperContentHolder and is used to receive user input.
     * The input is actually generated in the StepperViewHolder,
     * but is forwarded to this listener via the StepperAdapter
     */
    public interface ContentInteractionListener {
        void onContinueClick(VerticalStepperContentHolder stepContent);

        void onCancelClick(VerticalStepperContentHolder stepContent);

        void onStepSelected(VerticalStepperContentHolder stepContent);
    }


}
