package com.viettel.ws;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The Class KThreadPoolExecutor.
 */
public class KThreadPoolExecutor {

	private static final int POOL_SIZE = 500;

	private static final int MAX_POOL_SIZE = 1000;

	private static final long KEEP_ALIVE_TIME = 10;

	/** The thread pool. */
	private static ThreadPoolExecutor threadPool;
	
	/** The access log thread pool. */
	private static ThreadPoolExecutor accessLogThreadPool;

	private static boolean isThreadEnable = true;

	static {
		threadPool = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
		threadPool.allowCoreThreadTimeOut(true);
		accessLogThreadPool = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
		accessLogThreadPool.allowCoreThreadTimeOut(true);
	}

	/**
	 * Run task.
	 * 
	 * @param task
	 *            the task
	 */
	public static void execute(Runnable task) {
		if (isThreadEnable) {
			threadPool.execute(task);
		} else {
			task.run();
		}
	}
	
	public static void executeAccessLog(Runnable task) {
		if (isThreadEnable) {
			accessLogThreadPool.execute(task);
		} else {
			task.run();
		}
	}

	/**
	 * Shut down.
	 */
	public static void shutDown() {
		threadPool.shutdown();
		accessLogThreadPool.shutdown();
	}

	public static void setThreadEnable(boolean isThreadEnable) {
		KThreadPoolExecutor.isThreadEnable = isThreadEnable;
	}
}
