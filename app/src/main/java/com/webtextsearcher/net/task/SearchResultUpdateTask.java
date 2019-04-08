package com.webtextsearcher.net.task;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.webtextsearcher.R;
import com.webtextsearcher.net.WebSearchManager;

public class SearchResultUpdateTask implements Runnable {
	private TextView txtViewStatus;
	private ProgressBar progressBar;
	private String status;
	
	public SearchResultUpdateTask(TextView txtViewStatus, ProgressBar progressBar) {
		this.txtViewStatus = txtViewStatus;
		this.progressBar = progressBar;
	}
	
	public void handleSearchStatus(String html) {
		Context context = txtViewStatus.getContext();
		if (html.contains(WebSearchManager.getWebSearchManager().getSearchedText())) {
			this.status = context.getString(R.string.find);
		} else {
			this.status = context.getString(R.string.not_find);
		}
	}
	
	public void handleSearchErrorStatus(String error) {
		Context context = txtViewStatus.getContext();
		this.status = context.getString(R.string.error_with_body, error);
	}
	
	@Override
	public void run() {
		progressBar.setVisibility(View.GONE);
		txtViewStatus.setText(status);
	}
}