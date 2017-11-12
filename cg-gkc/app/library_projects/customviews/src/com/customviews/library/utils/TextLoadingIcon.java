//package com.customviews.library.utils;
//
//import android.content.Context;
//
//import com.customviews.library.customasynctask.AsyncJob;
//import com.customviews.library.model.TaskResult;
//
///**
// * Created by rashed on 3/22/16.
// */
//public class TextLoadingIcon {
//
//    public interface TextLoadingUpdate {
//        public void getUpdate(String update);
//    }
//
//    public static class TextLoadingIconBuilder {
//
//        private static TextLoadingIconBuilder mtTextLoadingIconBuilder = null;
//        private TextLoadingUpdate mTextLoadingUpdate = null;
//        private boolean mCancelTextLoadingUpdate = false;
//        private String shape = "";
//
//        private TextLoadingIconBuilder() {
//        }
//
//        public static TextLoadingIconBuilder getInstance() {
//            if (mtTextLoadingIconBuilder == null) {
//                mtTextLoadingIconBuilder = new TextLoadingIconBuilder();
//            }
//            return mtTextLoadingIconBuilder;
//        }
//
//        private TextLoadingIconBuilder setTextLoadingUpdate(TextLoadingUpdate textLoadingUpdate) {
//            mTextLoadingUpdate = textLoadingUpdate;
//            return this;
//        }
//
//        public TextLoadingIconBuilder cancel(boolean isCanceled) {
//            mCancelTextLoadingUpdate = isCanceled;
//            return this;
//        }
//
//        public void create(TextLoadingUpdate textLoadingUpdate) {
//            setTextLoadingUpdate(textLoadingUpdate);
//            AsyncJob.doInBackground(new AsyncJob.OnBackgroundJobWithProgress<TaskResult<String>>() {
//                @Override
//                public TaskResult<String> doOnBackground() {
//                    shape = "/";
//                    int iCounter = 0; //1 unit = 250ms
//                    while (!mCancelTextLoadingUpdate) {
//                        iCounter++;
//                        switch (shape) {
//                            case "/":
//                                shape = "\\";
//                                break;
//                            case "\\":
//                                shape = "--";
//                                break;
//                            case "--":
//                                shape = "/";
//                                break;
//                        }
//                        //Update Animation
//                        AsyncJob.publishProgress(0L);
//                        //Sleep for 250ms;
//                        try {
//                            Thread.sleep(250);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (iCounter == 40) {
//                            //let's check if license is available
//
//                            //reset
//                            iCounter = 0;
//                        }
//                        if (mCancelTextLoadingUpdate) {
//                            shape = "";
//                            AsyncJob.publishProgress(0L);
//                            return new TaskResult<String>(shape);
//                        }
//                    }
//                    return null;
//                }
//
//                @Override
//                public void doOnProgress(Long... progress) {
//                    mTextLoadingUpdate.getUpdate(shape);
//                }
//
//                @Override
//                public void doWhenFinished(TaskResult<String> taskResult) {
//                }
//            });
//        }
//    }
//}
