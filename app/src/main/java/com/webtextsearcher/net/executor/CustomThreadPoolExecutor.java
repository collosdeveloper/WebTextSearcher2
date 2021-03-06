package com.webtextsearcher.net.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {
	private boolean isPaused;
	private ReentrantLock lock;
	private Condition condition;
	
	public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
	                                TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		lock = new ReentrantLock();
		condition = lock.newCondition();
	}
	
	@Override
	protected void beforeExecute(Thread thread, Runnable runnable) {
		super.beforeExecute(thread, runnable);
		lock.lock();
		try {
			while (isPaused) condition.await();
		} catch (InterruptedException ie) {
			thread.interrupt();
		} finally {
			lock.unlock();
		}
	}
	
	public boolean isRunning() {
		return !isPaused;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void pause() {
		lock.lock();
		try {
			isPaused = true;
		} finally {
			lock.unlock();
		}
	}
	
	public void resume() {
		lock.lock();
		try {
			isPaused = false;
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
}