package com.probojnik.vk4activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity implements IListState {
	/**
	 * Called when the activity is first created.
	 */
	WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		webView = (WebView) findViewById(R.id.webView);
		
		webView.setWebViewClient(new VkWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearCache(true);
		webView.loadUrl(url());

	}

	class VkWebViewClient extends android.webkit.WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("log123", "LoginActivity VkWebViewClient onPageStarted url = " + url);
			super.onPageStarted(view, url, favicon);
			parseUrl(url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.d("log123", "LoginActivity VkWebViewClient onPageFinished");
			super.onPageFinished(view, url);
		}
		

		private void parseUrl(String url) {
			Log.d("log123", "LoginActivity parseUrl url = " + url);
			try {
				if (url == null)
					return;
				if (url.startsWith(IConst.REDIRECT_URI)) {
					if (!url.contains("error") && !url.contains("expires_in=0")) {
						next(url);
					} else
						Log.e("log123", "!url.contains(error) " + url);
				} else
					Log.e("log123", "url.startsWith(IConst.REDIRECT_URI) " + url);
			} catch (Exception e) {
				Log.e("log123", "Exception " + e);
			}
		}
	}

	@Override
	public String url(Object... par) {
		return "https://oauth.vk.com/authorize?client_id=" + IConst.CLIENT_ID + "&scope=photos&display=mobile&redirect_uri=" + IConst.REDIRECT_URI + "&response_type=token";
	}

	@Override
	public boolean next(Object... par){
		String[] auth;
		try {
			auth = VKUtil.parseRedirectUrl((String) par[0]);
			Log.d("log123", "token " + auth[0] + ", uid " + auth[1]);

			Intent intent = new Intent();
			intent.putExtra("access_token", auth[0]);
			intent.putExtra("user_id", auth[1]);
			intent.setClass(LoginActivity.this, GalleryActivity.class);
			startActivity(intent);	
			return true;
		} catch (Exception e) {
			Log.e("log123", "Exception " + e);
		}
		return false;
	}

	@Override
	public boolean prev(Object... par) {
		return false;
	}
}