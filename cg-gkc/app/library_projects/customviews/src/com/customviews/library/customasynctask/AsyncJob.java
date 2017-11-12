package com.customviews.library.customasynctask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.model.TaskResult;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Rashed on 07/10/15.
 */
public class AsyncJob<JobResult> {

	private static Handler uiHandler = new Handler(Looper.getMainLooper());

	// Action to do in background
	private static AsyncAction actionInBackground;
	private static OnBackgroundJobWithProgress actionInBackgroundWithProgress;
	private static OnAsyncJob actionAsyncJob;
	// Action to do when the background action ends
	// private AsyncResultAction actionOnMainThread;

	// private OnProgressUpdateJob actionUpdate;

	// An optional ExecutorService to enqueue the actions
	private ExecutorService executorService;

	// The thread created for the action
	private Thread asyncThread;
	// The FutureTask created for the action
	private FutureTask asyncFutureTask;

	// The result of the background action
	private JobResult result;

	private static volatile JobType mJobType = JobType.DOINBACKGROUNDWITHPROGRESS;

	/**
	 * Instantiates a new AsyncJob
	 */
	public AsyncJob() {
	}

	/**
	 * Executes the provided code immediately on the UI Thread
	 *
	 * @param onMainThreadJob
	 *            Interface that wraps the code to execute
	 */
	public static void doOnMainThread(final OnMainThreadJob onMainThreadJob) {
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				onMainThreadJob.doInUIThread();
			}
		});
	}

	/**
	 * Executes the provided code immediately on a background thread
	 *
	 * @param onBackgroundJob
	 *            Interface that wraps the code to execute
	 */
	public static void doInBackground(final OnBackgroundJob onBackgroundJob) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				onBackgroundJob.doOnBackground();
			}
		}).start();
	}

	public static OnBackgroundJobWithProgress doInBackground(final OnBackgroundJobWithProgress onBackgroundJob) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				onBackgroundJob.doOnBackground();
			}
		}).start();
		return onBackgroundJob;
	}

	public static OnAsyncJob doOnAsyncJob(final OnAsyncJob onBackgroundJob) {
		new Thread(new Runnable() {
			@Override
			public void run() {
//				onBackgroundJob.doInBackground();
				actionAsyncJob = onBackgroundJob;
				//foreground
				doOnMainThread(new OnMainThreadJob() {
					@Override
					public void doInUIThread() {
						actionAsyncJob.doOnForeground();
					}
				});
				//background
				mJobType = JobType.DOANASYNCJOB;
				new AsyncJob().onResult(JobType.DOANASYNCJOB, actionAsyncJob.doInBackground());
				try {
					Log.d("Sleep time:", "yes");
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}

			}
		}).start();
		return onBackgroundJob;
	}

	public static void doInBackground(final AsyncAction onBackgroundJob) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// onBackgroundJob.doOnBackground();
				actionInBackground = onBackgroundJob;
				mJobType = JobType.ASYNCACTION;
				new AsyncJob().onResult(JobType.ASYNCACTION, actionInBackground.doOnBackground());
				try {
					Log.d("Sleep time:", "yes");
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}

			}
		}).start();
	}

	/**
	 * Executes the provided code immediately on a background thread that will
	 * be submitted to the provided ExecutorService
	 *
	 * @param onBackgroundJob
	 *            Interface that wraps the code to execute
	 * @param executor
	 *            Will queue the provided code
	 */
	public static FutureTask doInBackground(final OnBackgroundJob onBackgroundJob, ExecutorService executor) {
		FutureTask task = (FutureTask) executor.submit(new Runnable() {
			@Override
			public void run() {
				onBackgroundJob.doOnBackground();
			}
		});
		return task;
	}

	public static FutureTask doInBackground(final OnBackgroundJobWithProgress onBackgroundJob,
			ExecutorService executor) {
		FutureTask task = (FutureTask) executor.submit(new Runnable() {
			@Override
			public void run() {
				actionInBackgroundWithProgress = onBackgroundJob;
				mJobType = JobType.DOINBACKGROUNDWITHPROGRESS;
				// result=(JobResult)actionInBackground.doOnBackground();
				new AsyncJob().onResult(JobType.DOINBACKGROUNDWITHPROGRESS,
						actionInBackgroundWithProgress.doOnBackground());
				// this thread sleep must needed, otherwise we cant see output
				// on ui thread. Cause, it start next
				// queued task immediately.
				try {
					Log.d("Sleep time:", "yes");
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				// onBackgroundJob.doOnBackground();
			}
		});
		return task;
	}

	public static FutureTask doAsyncJob(final OnAsyncJob onAsyncJob,
											ExecutorService executor) {
		FutureTask task = (FutureTask) executor.submit(new Runnable() {
			@Override
			public void run() {
				actionAsyncJob = onAsyncJob;
				//foreground
				//foreground
				doOnMainThread(new OnMainThreadJob() {
					@Override
					public void doInUIThread() {
						actionAsyncJob.doOnForeground();
					}
				});
				//background
				mJobType = JobType.DOANASYNCJOB;
				// result=(JobResult)actionInBackground.doOnBackground();
				new AsyncJob().onResult(JobType.DOANASYNCJOB,
						actionAsyncJob.doInBackground());
				// this thread sleep must needed, otherwise we cant see output
				// on ui thread. Cause, it start next
				// queued task immediately.
				try {
					Log.d("Sleep time:", "yes");
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				// onBackgroundJob.doOnBackground();
			}
		});
		return task;
	}

	public static FutureTask doInBackground(final AsyncAction onBackgroundJob, ExecutorService executor) {
		FutureTask task = (FutureTask) executor.submit(new Runnable() {
			@Override
			public void run() {
				actionInBackground = onBackgroundJob;
				mJobType = JobType.ASYNCACTION;
				// result=(JobResult)actionInBackground.doOnBackground();
				new AsyncJob().onResult(JobType.ASYNCACTION, actionInBackground.doOnBackground());
				// this thread sleep must needed, otherwise we cant see output
				// on ui thread. Cause, it start next
				// queued task immediately.
				try {
					Log.d("Sleep time:", "yes");
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}

			}
		});

		return task;
	}

	/**
	 * Begins the background execution providing a result, similar to an
	 * AsyncTask. It will execute it on a new Thread or using the provided
	 * ExecutorService
	 */
	public void start() {
		if (actionInBackground != null) {

			Runnable jobToRun = new Runnable() {
				@Override
				public void run() {
					result = (JobResult) actionInBackground.doOnBackground();
					mJobType = JobType.ASYNCACTION;
					onResult(JobType.ASYNCACTION, result);
				}
			};

			if (getExecutorService() != null) {
				asyncFutureTask = (FutureTask) getExecutorService().submit(jobToRun);
			} else {
				asyncThread = new Thread(jobToRun);
				asyncThread.start();
			}
		}
	}

	/**
	 * Cancels the AsyncJob interrupting the inner thread.
	 */
	public void cancel() {
		if (actionInBackground != null) {
			if (executorService != null) {
				if (asyncFutureTask.cancel(true)) {
					mCancelled.set(true);
				}
			} else {
				asyncThread.interrupt();
			}
		}
	}

	// private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_PROGRESS = 0x2;
	private static AtomicBoolean mCancelled = new AtomicBoolean();
	private static InternalHandler sHandler = new InternalHandler();

	public static boolean isCancelled() {
		return mCancelled.get();
	}

	public static void publishProgress(Long... values) {
		if (!isCancelled()) {
			sHandler.obtainMessage(MESSAGE_POST_PROGRESS, new TaskResult(values)).sendToTarget();
		}
	}

	@SuppressWarnings({ "RawUseOfParameterizedType" })
//	public static class TaskResult<T> {
//		private Long[] mData;
//		private Exception mError;
//		private T mValue;
//
//		public TaskResult(Long... data) {
//			mData = data;
//		}
//
//		public TaskResult(T value) {
//			mValue = value;
//		}
//
//		public TaskResult(T value, Exception error) {
//			mValue = value;
//			mError = error;
//		}
//
//		public TaskResult(Exception error) {
//			mError = error;
//		}
//
//		public Exception getError() {
//			return mError;
//		}
//
//		public T getResult() {
//			return mValue;
//		}
//
//		private Long[] getData() {
//			return mData;
//		}
//	}

	private static class InternalHandler extends Handler {
		@SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
		@Override
		public void handleMessage(Message msg) {
			TaskResult result = (TaskResult) msg.obj;
			switch (msg.what) {
			case MESSAGE_POST_PROGRESS:

				switch (mJobType) {
				case ASYNCACTION:
					if (actionInBackground != null) {
						actionInBackground.doOnProgress(result.getData());
					}
					break;
				case DOINBACKGROUNDWITHPROGRESS:
					if (actionInBackgroundWithProgress != null) {
						actionInBackgroundWithProgress.doOnProgress(result.getData());
					}
					break;
				}
				break;
			}
		}
	}

	private volatile Status mStatus = Status.PENDING;

	public final Status getStatus() {
		return mStatus;
	}

	public final JobType getJobType() {
		return mJobType;
	}

	public enum Status {
		/**
		 * Indicates that the task has not been executed yet.
		 */
		PENDING,
		/**
		 * Indicates that the task is running.
		 */
		RUNNING,
		/**
		 * Indicates that has finished.
		 */
		FINISHED,
	}

	public enum JobType {
		/**
		 * Indicates sequential task with progress.
		 */
		ASYNCACTION,
		/**
		 * Indicates single task with progress.
		 */
		DOINBACKGROUNDWITHPROGRESS,

		DOANASYNCJOB
	}

	private void onResult(JobType jobType, final JobResult result) {
		// if (actionOnMainThread != null) {
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				if (actionInBackground != null) {
					actionInBackground.doWhenFinished(result);
					actionInBackground=null;
				}
				if (actionInBackgroundWithProgress != null) {
					actionInBackgroundWithProgress.doWhenFinished(result);
					actionInBackgroundWithProgress=null;
				}
				if (actionAsyncJob != null) {
					actionAsyncJob.doWhenFinished(result);
					actionAsyncJob=null;
				}
			}
		});
		// }
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public AsyncAction getActionInBackground() {
		return actionInBackground;
	}

	/**
	 * Specifies which action to run in background
	 *
	 * @param actionInBackground
	 *            the action
	 */
	public void setActionInBackground(AsyncAction actionInBackground) {
		this.actionInBackground = actionInBackground;
	}

	// public AsyncResultAction getActionOnResult() {
	// return actionOnMainThread;
	// }
	//
	// /**
	// * Specifies which action to run when the background action is finished
	// *
	// * @param actionOnMainThread the action
	// */
	// public void setActionOnResult(AsyncResultAction actionOnMainThread) {
	// this.actionOnMainThread = actionOnMainThread;
	// }

	public interface AsyncAction<ActionResult> {
		public ActionResult doOnBackground();

		public void doOnProgress(Long... progress);

		public void doWhenFinished(ActionResult result);
	}

	// public interface AsyncResultAction<ActionResult> {
	// public void onResult(ActionResult result);
	// }

	public interface OnMainThreadJob {
		public void doInUIThread();
	}

	public interface OnBackgroundJob {
		public void doOnBackground();
	}

	public interface OnBackgroundJobWithProgress<ActionResult> {
		public ActionResult doOnBackground();

		public void doOnProgress(Long... progress);

		public void doWhenFinished(ActionResult result);
	}

	public interface OnAsyncJob<ActionResult> {

		public void doOnForeground();

		public ActionResult doInBackground();

		public void doOnProgress(Long... progress);

		public void doWhenFinished(ActionResult result);
	}

	/**
	 * Builder class to instantiate an AsyncJob in a clean way
	 *
	 * @param <JobResult>
	 *            the type of the expected result
	 */
	public static class AsyncJobBuilder<JobResult> {

		private AsyncAction<JobResult> asyncAction;
		// private AsyncResultAction asyncResultAction;
		private ExecutorService executor;

		public AsyncJobBuilder() {

		}

		/**
		 * Specifies which action to run on background
		 *
		 * @param action
		 *            the AsyncAction to run
		 * @return the builder object
		 */
		public AsyncJobBuilder<JobResult> doInBackground(AsyncAction<JobResult> action) {
			asyncAction = action;

			return this;
		}

		/**
		 * Specifies which action to run when the background action ends
		 *
		 * @param action
		 *            the AsyncAction to run
		 * @return the builder object
		 */
		// public AsyncJobBuilder<JobResult> doWhenFinished(AsyncResultAction
		// action) {
		// asyncResultAction = action;
		// return this;
		// }

		/**
		 * Used to provide an ExecutorService to launch the AsyncActions
		 *
		 * @param executor
		 *            the ExecutorService which will queue the actions
		 * @return the builder object
		 */
		public AsyncJobBuilder<JobResult> withExecutor(ExecutorService executor) {
			this.executor = executor;
			return this;
		}

		/**
		 * Instantiates a new AsyncJob of the given type
		 *
		 * @return a configured AsyncJob instance
		 */
		public AsyncJob<JobResult> create() {
			AsyncJob<JobResult> asyncJob = new AsyncJob<JobResult>();
			asyncJob.setActionInBackground(asyncAction);
			// asyncJob.setActionOnResult(asyncResultAction);
			asyncJob.setExecutorService(executor);
			return asyncJob;
		}

	}

}
