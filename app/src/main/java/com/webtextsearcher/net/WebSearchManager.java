package com.webtextsearcher.net;

import android.util.Log;

import com.webtextsearcher.net.executor.CustomThreadPoolExecutor;
import com.webtextsearcher.net.executor.MainThreadExecutor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class WebSearchManager {
	private static final String TAG = WebSearchManager.class.getSimpleName();
	
	private CustomThreadPoolExecutor searchingThreadPool;
	private BlockingQueue<Runnable> searchingWorkQueue;
	private WebSearchManagerCallback webSearchManagerCallback;
	
	private static WebSearchManager webSearchManager;
	private static MainThreadExecutor handler;
	
	private String searchedText;
	
	static {
		webSearchManager = new WebSearchManager();
		handler = new MainThreadExecutor();
	}
	
	public void init(int threadCount,
	                 WebSearchManagerCallback webSearchManagerCallback, String searchedText) {
		Log.d(TAG, "ThreadCount : " + threadCount);
		if(searchingWorkQueue != null || searchingThreadPool != null) {
			shutdown();
		}
		
		this.webSearchManagerCallback = webSearchManagerCallback;
		this.searchedText = searchedText;
		
		searchingWorkQueue = new LinkedBlockingQueue<>();
		searchingThreadPool = new CustomThreadPoolExecutor(threadCount, threadCount,
				50, TimeUnit.MILLISECONDS, searchingWorkQueue);
	}
	
	public String getSearchedText() {
		return searchedText;
	}
	
	public void shutdown() {
		searchingWorkQueue.clear();
		searchingThreadPool.shutdownNow();
	}
	
	public boolean isShutdown() {
		return searchingThreadPool.isShutdown();
	}
	
	public static WebSearchManager getWebSearchManager(){
		return webSearchManager;
	}
	
	public void run(Runnable task){
		try {
			if(!isShutdown()) {
				searchingThreadPool.execute(task);
			}
		} catch (RejectedExecutionException ignored) { }
	}
	
	public boolean pause() {
		if(!searchingThreadPool.isPaused()) {
			searchingThreadPool.pause();
			
			return true;
		} else {
			searchingThreadPool.resume();
			
			return false;
		}
	}
	
	public void setListOfLinks(List<String> urls) {
		Log.d(TAG, "setListOfLinks : " + urls.toString());
		webSearchManagerCallback.onResult(urls);
	}
	
	public MainThreadExecutor getMainThreadExecutor(){
		return handler;
	}
	
	public interface WebSearchManagerCallback {
		void onResult(List<String> urls);
	}
}