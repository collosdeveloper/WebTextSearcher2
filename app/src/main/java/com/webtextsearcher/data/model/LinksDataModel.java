package com.webtextsearcher.data.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

public class LinksDataModel {
	private MutableLiveData<List<String>> links = new MutableLiveData<>();
	
	public LiveData<List<String>> getLinks() {
		return links;
	}
	
	public void setLinks(List<String> urls) {
		links.postValue(urls);
	}
}