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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumActivity extends ListActivity implements OnItemClickListener, IListState {
	
    JSONObject[] jSONObject;
    long aid;
    String user_id, access_token;
	ListView listView;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = getListView();
		listView.setOnItemClickListener(this);
        
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        aid = intent.getLongExtra("aid", 0);
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
		Log.d("log123", "AlbumActivity onItemClick id = " + id);
		next(id);
	}
	
	@Override
	public String url(Object... par) {
		return "https://api.vk.com/method/photos.get?uid=" + user_id + "&aid=" + aid + "&access_token="+access_token;
	}
	
	@Override
	public boolean next(Object... par) {
		Intent intent = new Intent();
        intent.putExtra("user_id", user_id);
        intent.putExtra("access_token", access_token);
        intent.putExtra("pid",(Long) par[0]);
        intent.setClass(getApplicationContext(), PhotoActivity.class);
        startActivity(intent);
        return true;
	}

	@Override
	public boolean prev(Object... par) {
		finish();
		return true;
	}

}