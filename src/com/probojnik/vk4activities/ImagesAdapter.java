package com.probojnik.vk4activities;

import java.lang.ref.WeakReference;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImagesAdapter extends BaseAdapter {

	JSONObject[] jsonObject;
	Context context;
	
	public ImagesAdapter(Context c, JSONObject[] jsonObject) {
		Log.d("log123", "jsonObject = " + jsonObject.length);
		context = c;
		this.jsonObject = new JSONObject[jsonObject.length];
		this.jsonObject = jsonObject;
	}
	
	@Override
	public int getCount() {
		return jsonObject.length;
	}

	@Override
	public JSONObject getItem(int position) {
		return jsonObject[position];
	}
	
	@Override
	public long getItemId(int position) {
		try {
			if (jsonObject[position].has("pid"))
				return jsonObject[position].getLong("pid");
			else
				return jsonObject[position].getLong("aid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // getContext()
			convertView = layoutInflater.inflate(R.layout.list_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		try {
			viewHolder.textView.setText((String) jsonObject[i].get("description") + " - " + i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DownloadImage task = new DownloadImage(viewHolder);
		task.execute(getItem(i));

		return convertView; // To change body of implemented methods use File | Settings | File Templates.
	}


	private static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
		public ProgressBar progressBar;
	}

	private class DownloadImage extends AsyncTask<JSONObject, Integer, Bitmap> {
		private ProgressBar progressBar;
		private ImageView imageView;
		private TextView textView;
		String textViewStr;

		public DownloadImage(ViewHolder viewHolder) {
			progressBar = (ProgressBar) viewHolder.progressBar;
			imageView = (ImageView) viewHolder.imageView;
			textView = (TextView) viewHolder.textView;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			imageView.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setIndeterminate(true);
		}

		@Override
		protected Bitmap doInBackground(JSONObject... params) {
			URL url;
			Bitmap bmImage = null;
			String s;
			try {
				if (params[0].has("thumb_src")) {
					s = params[0].getString("thumb_src");
					textViewStr = params[0].getString("title");
				} else {
					s = params[0].getString("src");
				}
				url = new URL(s);
				bmImage = BitmapFactory.decodeStream(url.openStream());
				
//				Thread.sleep(1000);
				
				return bmImage;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap image) {
			progressBar.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(image);
			textView.setText(textViewStr);
		}
	}


	
}
