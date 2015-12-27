package com.taksila.veda.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleUtils {

	public ResourceBundleUtils() {

	}

	public static ResourceBundle getResourceBundle(String resourceBundlePath, Locale locale) {
		ResourceBundle resourceBundle = null;

		if(resourceBundle == null) {
			try {
				resourceBundle = ResourceBundle.getBundle(resourceBundlePath);
			} catch (MissingResourceException e) {
				e.printStackTrace();
			}
		}

		return resourceBundle;
	}

	public static String getStringFromBundle(String resourceBundlePath, Locale locale, String key) {
		ResourceBundle resourceBundle = getResourceBundle(resourceBundlePath, locale);
		return getStringFromBundle( resourceBundle, key );
	}

	public static String getStringFromBundle(ResourceBundle resourceBundle, String key) {
		String value = "";

		try {
			value = resourceBundle.getString(key).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}
}
