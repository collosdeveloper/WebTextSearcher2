package com.webtextsearcher.data;

import android.util.Log;
import android.util.Pair;

import com.webtextsearcher.ui.main.WebSearchStatus;
import com.webtextsearcher.utils.ValidatorUtils;

public class RequiredSearchData {
	private static final String TAG = RequiredSearchData.class.getSimpleName();
	
	private final String startUrl;
	private final int threadCount;
	private final String searchedText;
	private final int countOfScannedUrl;
	
	public RequiredSearchData(String startUrl, String threadCount, String searchedText, String countOfScannedUrl) {
		this.startUrl = startUrl;
		this.threadCount = convertLocalStringToInteger(threadCount);
		this.searchedText = searchedText;
		this.countOfScannedUrl = convertLocalStringToInteger(countOfScannedUrl);
	}
	
	public RequiredSearchData(String startUrl, Integer threadCount, String searchedText, Integer countOfScannedUrl) {
		this.startUrl = startUrl;
		this.threadCount = threadCount;
		this.searchedText = searchedText;
		this.countOfScannedUrl = countOfScannedUrl;
	}
	
	public String getStartUrl() {
		return startUrl;
	}
	
	public int getThreadCount() {
		return threadCount;
	}
	
	public String getSearchedText() {
		return searchedText;
	}
	
	public int getCountOfScannedUrl() {
		return countOfScannedUrl;
	}
	
	public Pair <Boolean, WebSearchStatus> isCorrect() {
		if(!ValidatorUtils.isTextCorrect(startUrl) ||
				ValidatorUtils.isTextCorrect(startUrl) &&
				!ValidatorUtils.isUrlCorrect(startUrl)) {
			return new Pair<>(false, WebSearchStatus.URL_FORMAT_ERROR);
		} else if(!ValidatorUtils.isTextCorrect(searchedText)) {
			return new Pair<>(false, WebSearchStatus.SEARCHED_TEXT_ERROR);
		} else if(!ValidatorUtils.isIntCorrectAndNotEmpty(threadCount)) {
			return new Pair<>(false, WebSearchStatus.THREAD_COUNT_ERROR);
		} else if(!ValidatorUtils.isIntCorrectAndNotEmpty(countOfScannedUrl)) {
			return new Pair<>(false, WebSearchStatus.MAX_SCAN_URL_ERROR);
		} else {
			return new Pair<>(true, null);
		}
	}
	
	private Integer convertLocalStringToInteger(String value) {
		Log.d(TAG, value + " : isTextCorrect : " + ValidatorUtils.isTextCorrect(value));
		Log.d(TAG, value + " : isTextOnlyContainsNumbers : " + ValidatorUtils.isTextOnlyContainsNumbers(value));
		return ValidatorUtils.isTextCorrect(value) &&
				ValidatorUtils.isTextOnlyContainsNumbers(value)
				? Integer.valueOf(value) : 0;
	}
	
	@Override
	public String toString() {
		return "RequiredSearchData{" +
				"startUrl='" + startUrl + '\'' +
				", threadCount=" + threadCount +
				", searchedText='" + searchedText + '\'' +
				", countOfScannedUrl=" + countOfScannedUrl +
				'}';
	}
}