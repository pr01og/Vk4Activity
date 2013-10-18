package com.probojnik.vk4activities;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class BitmapAsynkTask extends AsyncTask<String, Void, Bitmap>{
	PhotoActivity ctx;
	BitmapAsynkTask(PhotoActivity ctx){
		this.ctx = ctx;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		Log.d("log123", "BitmapAsynkTask doInBackground params = " + params[0]);
		Bitmap bitmap = null;
		
		DefaultHttpClient httpclient  = new DefaultHttpClient();	
		HttpPost postMethod = new HttpPost(params[0]);
		try{
			HttpResponse httpResponse = httpclient.execute(postMethod);
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null){
				String entityStr = EntityUtils.toString(httpEntity); 
				Log.d("log123", "BitmapAsynkTask doInBackground entityStr = " + entityStr);
 
				String src_big = new JSONObject(entityStr).getJSONArray("response").getJSONObject(0).getString("src_big");

				URL url = new URL(src_big);
                    
				bitmap = BitmapFactory.decodeStream(url.openStream());
			}
		}catch(JSONException e){
			Log.e("log123", "JSONException = " + e);
		} catch (ClientProtocolException e) {
			Log.e("log123", "ClientProtocolException = " + e);
		} catch (IOException e) {
			Log.e("log123", "IOException = " + e);
		}
		
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		ctx.imageView.setImageBitmap(result);
	}

}
