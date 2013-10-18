package com.probojnik.vk4activities;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONAsynkTask extends AsyncTask<String, Void, JSONObject[]>{

	@Override
	protected JSONObject[] doInBackground(String... params) {
		Log.d("log123", "JSONAsynkTask doInBackground params = " + params[0]);
		JSONObject[] jsonObjectArr = null;
		
		DefaultHttpClient httpclient  = new DefaultHttpClient();	
		HttpPost postMethod = new HttpPost(params[0]);
		try{
			HttpResponse httpResponse = httpclient.execute(postMethod);
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null){
				String entityStr = EntityUtils.toString(httpEntity); 
				Log.d("log123", "JSONAsynkTask doInBackground entityStr = " + entityStr);
				
				Thread.sleep(1000);
				
				JSONObject jsonObject = new JSONObject(entityStr);
				JSONArray  jsonArray  = jsonObject.getJSONArray("response");
				
				jsonObjectArr = new JSONObject[jsonArray.length()];
				for (int i=0; i < jsonArray.length(); i++)  {
					jsonObjectArr[i] = jsonArray.getJSONObject(i);
			    }     
			}
		}catch(JSONException e){
			Log.e("log123", "JSONException = " + e);
		} catch (ClientProtocolException e) {
			Log.e("log123", "ClientProtocolException = " + e);
		} catch (IOException e) {
			Log.e("log123", "IOException = " + e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObjectArr;
	}

}
