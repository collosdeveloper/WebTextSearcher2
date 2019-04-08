package com.webtextsearcher.net.task;

import android.content.Context;
import android.util.Log;

import com.webtextsearcher.net.WebSearchManager;
import com.webtextsearcher.utils.ValidatorUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FindLinksTask implements Runnable {
	private static final String TAG = FindLinksTask.class.getSimpleName();
	
	private final String url;
	private final int maxCountOfLinks;
	private Context context;
	
	public FindLinksTask(String url, int maxCountOfLinks){
		this.url = url;
		this.maxCountOfLinks = maxCountOfLinks;
	}
	
	public FindLinksTask(Context context, int maxCountOfLinks){
		this.url = null;
		this.maxCountOfLinks = maxCountOfLinks;
		this.context = context;
	}
	
	@Override
	public void run() {
		WebSearchManager.getWebSearchManager().setListOfLinks(parseWebPageForLinks());
	}
	
	private List<String> parseWebPageForLinks() {
		List<String> linksList = new ArrayList<>();
		Document document = null;
		try {
			if(url != null) {
				document = Jsoup.connect(url).userAgent("Mozilla").get();
			} else {
				try (InputStream is = context.getAssets().open("test.htm")) {
					document = Jsoup.parse(is, "UTF-8", "http://example.com/");
				}
			}
			Elements links = document.select("a[href]");
			for (Element link : links) {
				String foundLink = link.attr("abs:href");
				Log.d(TAG, "Link : " + foundLink);
				// Only Http
				if(ValidatorUtils.isUrlCorrect(foundLink) && !foundLink.contains("https")) {
					linksList.add(foundLink);
				}
				if(linksList.size() == maxCountOfLinks) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return linksList;
	}
}