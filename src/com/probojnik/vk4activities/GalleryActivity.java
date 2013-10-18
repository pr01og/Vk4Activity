package com.probojnik.vk4activities;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class GalleryActivity extends ListActivity implements OnItemClickListener, IListState {

	String user_id, access_token;
	JSONObject[] jSONObject;
	ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		Log.d("log123", "GalleryActivity onCreate savedInstanceState = " + savedInstanceState);
		super.onCreate(savedInstanceState);

		listView = getListView();
		listView.setOnItemClickListener(this);
		
		Intent intent = getIntent();
		user_id = intent.getStringExtra("user_id");
		access_token = intent.getStringExtra("access_token");
		
		JSONAsynkTask jsonAsynkTask = new JSONAsynkTask();
		try {
			jSONObject = jsonAsynkTask.execute(url()).get();
		} catch (InterruptedException e) {
			Log.e("log123", "InterruptedException = " + e);
		} catch (ExecutionException e) {
			Log.e("log123", "ExecutionException = " + e);
		}
		
		ImagesAdapter imageAdapter = new ImagesAdapter(this, jSONObject);
		listView.setAdapter(imageAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d("log123", "GalleryActivity onItemClick id = " + id);
		next(id);
	}

	@Override
	public String url(Object... par) {
		return "https://api.vk.com/method/photos.getAlbums?uid=" + user_id + "&need_covers=1&access_token=" + access_token;
	}

	@Override
	public boolean next(Object... par) {

		Intent intent = new Intent();
		intent.putExtra("user_id", user_id);
		intent.putExtra("access_token", access_token);
		intent.putExtra("aid",(Long) par[0]);
		intent.setClass(getApplicationContext(), AlbumActivity.class);
		startActivity(intent);
		return true;

	}

	@Override
	public boolean prev(Object... par) {
		finish();
		return true;
	}



}