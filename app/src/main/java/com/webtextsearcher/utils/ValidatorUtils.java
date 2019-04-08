package com.webtextsearcher.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidatorUtils {
	
	public static boolean isTextCorrect(String text) {
		return !TextUtils.isEmpty(text);
	}
	
	public static boolean isIntCorrectAndNotEmpty(Integer value) {
		return value != null && value != 0;
	}
	
	public static boolean isUrlCorrect(String url) {
		return Patterns.WEB_URL.matcher(url).matches();
	}
	
	public static boolean isTextOnlyContainsNumbers(String text) {
		return text.matches("[0-9]+");
	}
}