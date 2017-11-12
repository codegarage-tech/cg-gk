package com.customviews.library.verticalstepper;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Vector;

public class VerticalStepperAdapter extends RecyclerView.Adapter<VerticalStepperViewHolder> implements VerticalStepperViewHolder.StepperViewHolderListener {

    /**
     * This generator creates simple arabic numerals: All numerals are the value of the position+1, e.g. 1 for position = 0
     */
    public static final StepNumeralGenerator ARABIC_NUMERAL_GENERATOR = new StepNumeralGenerator() {
        @Override
        public String generateNumeralFor(int position) {
            return String.valueOf(position + 1);
        }
    };

    /**
     * This generator creates simple alphabetical numerals: All numbers are the value of the position as an upper case letter, e.g. A for position = 0
     */
    public static final StepNumeralGenerator ALPHABET_NUMERAL_GENERATOR = new StepNumeralGenerator() {
        @Override
        public String generateNumeralFor(int position) {
            return String.valueOf((char) (65 + ((position) % 26)));
        }
    };

    private final Vector<VerticalStepperContentHolder> stepContent;
    private final StepNumeralGenerator numeralGenerator;

    private RecyclerView mRecyclerView;

    /**
     * Creates a new StepperAdapter with no steps and no listeners attached to it.
     * The Adapter will use the default Arabic Numeral Generator for indexing the steps
     */
    public VerticalStepperAdapter() {
        this(ARABIC_NUMERAL_GENERATOR);
    }

    public VerticalStepperAdapter(StepNumeralGenerator numeralGenerator) {
        super();
        if (numeralGenerator != null) {
            this.numeralGenerator = numeralGenerator;
        } else {
            this.numeralGenerator = ARABIC_NUMERAL_GENERATOR;
        }
        stepContent = new Vector<>();
        mRecyclerView = null;
    }

    @Override
    public VerticalStepperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerticalStepperViewHolder(parent, this);
    }

    @Override
    public void onBindViewHolder(VerticalStepperViewHolder holder, int position) {
        holder.setStep(stepContent.get(position));
        stepContent.get(position).onStepUpdate();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return stepContent.size();
    }

    @Override
    public void onCancelClick(int stepNumber) {
        if (stepNumber >= 0) {
            stepContent.get(stepNumber).onCancelClick();
        }
    }

    @Override
    public void onContinueClick(int stepNumber) {
        if (stepNumber >= 0) {
            stepContent.get(stepNumber).onContinueClick();
        }
    }

    @Override
    public void onStepClick(int stepNumber) {
        if (stepNumber >= 0) {
            stepContent.get(stepNumber).onStepSelected();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public void addStep(VerticalStepperContentHolder step) {
        addStep(step, true);
    }

    private void addStep(VerticalStepperContentHolder step, boolean notifyDataSet) {
        if (stepContent.isEmpty()) {
            step.setStepStatus(VerticalStepperViewHolder.STEP_CURRENT, false);
        }
        stepContent.add(step);
        step.attachToAdapter(this);
        step.setStepIndex(stepContent.size() - 1);
        step.setStepIconText(numeralGenerator.generateNumeralFor(stepContent.size() - 1), false);

        if (notifyDataSet) {
            notifyItemInserted(stepContent.size() - 1);

            if (mRecyclerView != null) {
                mRecyclerView.smoothScrollToPosition(step.getStepIndex());
            }
        }
    }

    public void removeStep(VerticalStepperContentHolder step) {
        removeStep(step.getStepIndex());
    }

    public void removeStep(int stepNumber) {
        VerticalStepperContentHolder step = stepContent.remove(stepNumber);
        if (step != null) {
            step.detachFromAdapter();
        }
        notifyItemRemoved(stepNumber);
    }

    public void addSteps(Vector<VerticalStepperContentHolder> steps) {
        addSteps(steps.toArray(new VerticalStepperContentHolder[steps.size()]));
    }

    public void addSteps(VerticalStepperContentHolder... steps) {
        int start = stepContent.size() - 1;
        for (VerticalStepperContentHolder step : steps) {
            addStep(step, false);
        }
        notifyItemRangeInserted(start, steps.length);
    }

    public void moveCurrentStep(int from, int to, String newStatus) {
        if (from >= 0 && to >= 0 && from < stepContent.size() && to < stepContent.size()) {
            VerticalStepperContentHolder fromContent = stepContent.elementAt(from);
            VerticalStepperContentHolder toContent = stepContent.elementAt(to);

            // We can only have one STEP_CURRENT step per Adapter!
            if (newStatus == null || newStatus.equals(VerticalStepperViewHolder.STEP_CURRENT)) {
                fromContent.setStepStatus(VerticalStepperViewHolder.STEP_FINISHED, false);

                // We only alter steps if they are not yet finished
            } else if (!fromContent.getStepStatus().equals(VerticalStepperViewHolder.STEP_FINISHED)) {
                fromContent.setStepStatus(newStatus, false);
            }

            // To Step becomes our current step
            toContent.setStepStatus(VerticalStepperViewHolder.STEP_CURRENT, false);

            notifyItemChanged(from);
            notifyItemChanged(to);

            if (mRecyclerView != null) {
                mRecyclerView.smoothScrollToPosition(to);
            }
        }
    }

    public void moveCurrentStep(VerticalStepperContentHolder from, VerticalStepperContentHolder to, String newStatus) {
        moveCurrentStep(from.getStepIndex(), to.getStepIndex(), newStatus);
    }

    public void jumpToStep(int step) {
        if (step >= 0 && step < stepContent.size()) {
            int current = getCurrentStepNumber();

            moveCurrentStep(current, step, VerticalStepperViewHolder.STEP_WAITING);
        }
    }

    public void jumpToStep(VerticalStepperContentHolder step) {
        jumpToStep(step.getStepIndex());
    }

    public int getCurrentStepNumber() {
        for (int i = 0; i < stepContent.size(); i++) {
            if (VerticalStepperViewHolder.STEP_CURRENT.equals(getStepStatus(i))) {
                return i;
            }
        }
        return 0;
    }

    public boolean isAllStepsCompleted() {
        for (int i = 0; i < stepContent.size(); i++) {
            if (!stepContent.get(i).getStepStatus().equalsIgnoreCase(VerticalStepperViewHolder.STEP_FINISHED)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPreviousStepsCompleted(int stepIndex) {
        for (int i = 0; i < stepIndex; i++) {
            if(stepIndex==0){
                continue;
            }
            if (stepContent.get(i).getStepStatus().equalsIgnoreCase(VerticalStepperViewHolder.STEP_FINISHED)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public String getStepStatus(int position) {
        return stepContent.elementAt(position).getStepStatus();
    }

    /**
     * Makes the given step STEP_FINISHED and moves to the next step in line.
     * Returns whether there is a next step available or not
     *
     * @param current Step index that needs to be finished
     * @return True: Moved onto next step in line, False: No next step available or the current step is not in scope
     */
    public boolean finishStep(int current) {
        if (current < stepContent.size() - 1) {
            moveCurrentStep(current, current + 1, VerticalStepperViewHolder.STEP_FINISHED);
            return true;
        } else if (current == stepContent.size() - 1) {
            stepContent.elementAt(current).setStepStatus(VerticalStepperViewHolder.STEP_FINISHED);
        }
        return false;
    }

    /**
     * Makes the given step STEP_FINISHED and moves to the next step in line.
     * Returns whether there is a next step available or not
     *
     * @param step Step that needs to be finished
     * @return True: Moved onto next step in line, False: No next step available or the current step is not in scope
     */
    public boolean finishStep(VerticalStepperContentHolder step) {
        return finishStep(step.getStepIndex());
    }

    /**
     * Makes the given step STEP_WAITING and moves to the previous step in line.
     *
     * @param step Step that needs to be cancelled
     */
    public void cancelStep(VerticalStepperContentHolder step) {
        cancelStep(step.getStepIndex());
    }

    /**
     * Makes the given step STEP_WAITING and moves to the previous step in line.
     *
     * @param step Index of the Step that needs to be cancelled
     */
    public void cancelStep(int step) {
        if (step > 0 && step < stepContent.size()) {
            moveCurrentStep(step, step - 1, VerticalStepperViewHolder.STEP_WAITING);
        }
    }

    /**
     * This interface is used to create the numerals displayed in the step icon left of the step title.
     * These numerals are based on the position of the step, starting with 0 for the first step
     */
    public interface StepNumeralGenerator {

        /**
         * Generates a new numeral for the given position.
         * Starts with 0 and ends at stepcontent.size -1.
         *
         * @param position Position of the Step in the list (starting with 0)
         * @return A string with a numeral of the position (e.g. 1 or A)
         */
        String generateNumeralFor(int position);
    }
}
