package com.webtextsearcher.base.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.webtextsearcher.R;
import com.webtextsearcher.net.WebSearchManager;
import com.webtextsearcher.net.task.SearchResultUpdateTask;
import com.webtextsearcher.net.task.SearchTextTask;

import java.util.ArrayList;
import java.util.List;

public class UrlsListAdapter extends RecyclerView.Adapter<UrlsListAdapter.UrlViewHolder> {
	private List<String> urls = new ArrayList<>();
	
	@Override
	public UrlViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new UrlViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(),
				parent, false));
	}
	
	private int getLayoutId() {
		return R.layout.item_url;
	}
	
	@Override
	public void onBindViewHolder(@NonNull UrlViewHolder holder, int position) {
		holder.bind(urls.get(position));
	}
	
	@Override
	public int getItemCount() {
		return urls == null ? 0 : urls.size();
	}
	
	public void setUrls(List<String> urls) {
		this.urls = urls;
		notifyDataSetChanged();
	}
	
	class UrlViewHolder extends RecyclerView.ViewHolder {
		String url;
		
		TextView txtViewUrlName;
		TextView txtViewSearchingStatus;
		ProgressBar progressBar;
		
		UrlViewHolder(View itemView) {
			super(itemView);
			
			txtViewUrlName = itemView.findViewById(R.id.txt_url_name);
			txtViewSearchingStatus = itemView.findViewById(R.id.txt_status);
			progressBar = itemView.findViewById(R.id.progress_bar);
		}
		
		void bind(String url) {
			this.url = url;
			txtViewUrlName.setText(url);
			startSearching();
		}
		
		private void startSearching() {
			if (!WebSearchManager.getWebSearchManager().isShutdown()) {
				progressBar.setVisibility(View.VISIBLE);
				SearchResultUpdateTask updateTask = new SearchResultUpdateTask(txtViewSearchingStatus, progressBar);
				SearchTextTask searchTextTask = new SearchTextTask(url, updateTask);
				WebSearchManager.getWebSearchManager().run(searchTextTask);
			}
		}
	}
}