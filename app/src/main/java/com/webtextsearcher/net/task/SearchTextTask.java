package com.webtextsearcher.net.task;

import com.webtextsearcher.net.WebSearchManager;

import org.jsoup.Jsoup;

import java.io.IOException;

public class SearchTextTask implements Runnable {
	private final String url;
	private final SearchResultUpdateTask resultUpdateTask;
	
	public SearchTextTask(String url, SearchResultUpdateTask resultUpdateTask){
		this.url = url;
		this.resultUpdateTask = resultUpdateTask;
	}
	
	@Override
	public void run() {
		try {
			String html = Jsoup.connect(url).userAgent("Mozilla").get().html();
			resultUpdateTask.handleSearchStatus(html);
		} catch (IOException e) {
			e.printStackTrace();
			resultUpdateTask.handleSearchErrorStatus(e.getMessage());
		}
		
		WebSearchManager.getWebSearchManager().getMainThreadExecutor()
				.execute(resultUpdateTask);
	}
}