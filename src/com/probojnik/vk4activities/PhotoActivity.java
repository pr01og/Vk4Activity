package com.probojnik.vk4activities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class PhotoActivity extends Activity implements IListState {
    ImageView imageView;
    String user_id, access_token;
    Long pid;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent parentIntent = getIntent();
        user_id = parentIntent.getStringExtra("user_id");
        access_token = parentIntent.getStringExtra("access_token");
        pid = parentIntent.getLongExtra("pid",0);

        imageView = (ImageView)findViewById(R.id.imageView);
        
        BitmapAsynkTask jsonAsynkTask = new BitmapAsynkTask(this);        
        jsonAsynkTask.execute(url());
        
        Log.d("log123", "PhotoActivity url() = " + url());

    }
    
	@Override
	public String url(Object... par) {
		return "https://api.vk.com/method/photos.getById?photos=" + user_id + "_" + pid + "&access_token=" + access_token;
	}

	@Override
	public boolean next(Object... par) {
		return false;
	}

	@Override
	public boolean prev(Object... par) {
		finish();
		return true;
	}
	
	class ImageTask extends AsyncTask<String, Void, Bitmap>{
   		protected Bitmap doInBackground(String... params) {
			return null;
		}
        
		@Override
		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}
		
	}
}