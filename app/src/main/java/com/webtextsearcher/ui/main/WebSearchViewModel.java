package com.webtextsearcher.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.webtextsearcher.data.RequiredSearchData;
import com.webtextsearcher.data.model.LinksDataModel;
import com.webtextsearcher.net.WebSearchManager;
import com.webtextsearcher.net.task.FindLinksTask;

import java.util.List;

public class WebSearchViewModel extends AndroidViewModel {
	private LinksDataModel linksDataModel;
	private MutableLiveData<WebSearchStatus> webSearchStatus = new MutableLiveData<>();
	
	public LiveData<List<String>> getLinks() {
		return linksDataModel.getLinks();
	}
	
	public LiveData<WebSearchStatus> getWebSearchStatus() {
		return webSearchStatus;
	}
	
	public WebSearchViewModel(@NonNull Application application) {
		super(application);
		
		this.linksDataModel = new LinksDataModel();
	}
	
	@Override
	protected void onCleared() {
		WebSearchManager.getWebSearchManager().shutdown();
	}
	
	public void startSearch(RequiredSearchData requiredSearchData, boolean isTest) {
		Pair <Boolean, WebSearchStatus> isDataCorrect = requiredSearchData.isCorrect();
		if (isDataCorrect.first) {
			webSearchStatus.postValue(WebSearchStatus.REMOVE_ALL_ERROR);
			webSearchStatus.postValue(WebSearchStatus.SHOW_LOADER);
			
			String url = requiredSearchData.getStartUrl();
			WebSearchManager.getWebSearchManager().init(requiredSearchData.getThreadCount(),
					urls -> linksDataModel.setLinks(urls),
					requiredSearchData.getSearchedText());
			if(isTest) {
				WebSearchManager.getWebSearchManager().run(new FindLinksTask(getApplication(),
						requiredSearchData.getCountOfScannedUrl()));
			} else {
				WebSearchManager.getWebSearchManager().run(new FindLinksTask(url,
						requiredSearchData.getCountOfScannedUrl()));
			}
		} else {
			webSearchStatus.postValue(isDataCorrect.second);
		}
	}
	
	public void pauseSearch() {
		boolean isPauseEnable = WebSearchManager.getWebSearchManager().pause();
		if(isPauseEnable) {
			webSearchStatus.postValue(WebSearchStatus.PAUSE_ENABLE);
		} else {
			webSearchStatus.postValue(WebSearchStatus.PAUSE_DISABLE);
		}
	}
	
	public void stopSearch() {
		webSearchStatus.postValue(WebSearchStatus.HIDE_LOADER);
		WebSearchManager.getWebSearchManager().shutdown();
	}
}