package com.probojnik.vk4activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
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
			Log.e("log123", "JSONException = " + e);
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
			if (jsonObject[i].has("title")) {
				viewHolder.textView.setText(jsonObject[i].getString("title") + " - " + i);
			} else {
				viewHolder.textView.setText(jsonObject[i].getString("pid") + " - " + i);
			}
		} catch (JSONException e) {
			Log.e("log123", "JSONException = " + e);
		}

		viewHolder.position = i;
		DownloadImage task = new DownloadImage(i, viewHolder);
//		task.execute(jsonObject[i]);
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, jsonObject[i]);

		return convertView;
	}

	private static class ViewHolder {
		public int position;
		public ImageView imageView;
		public TextView textView;
		public ProgressBar progressBar;
	}

	private class DownloadImage extends AsyncTask<JSONObject, Integer, Bitmap> {
		private int position;
		private ViewHolder viewHolder;

		public DownloadImage(int position, ViewHolder viewHolder) {
			this.position = position;
			this.viewHolder = viewHolder;
		}

		@Override
		protected void onPreExecute() {
			viewHolder.imageView.setVisibility(View.GONE);
			viewHolder.progressBar.setVisibility(View.VISIBLE);
			viewHolder.progressBar.setIndeterminate(true);
		}

		@Override
		protected Bitmap doInBackground(JSONObject... params) {
			URL url;
			Bitmap bitMap = null;
			String uri;

			String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vk4activities";
			File dir = new File(file_path);
			dir.mkdirs();
			
			try {
				if (params[0].has("thumb_src")) {
					uri = params[0].getString("thumb_src");
				} else {
					uri = params[0].getString("src");
				}
				File file = new File(dir, URLEncoder.encode("small_" + uri, "utf-8")); // "img_" + (startIndex_at + i)
				if (file.exists()) {
					Log.d("log123", "file.exists uri = " + uri);
					FileInputStream fis = new FileInputStream(file);
					bitMap = BitmapFactory.decodeStream(fis);
					fis.close();
				} else {
					
					Log.d("log123", "NO file.exists uri = " + uri);
					InputStream in = new URL(uri).openStream();
					bitMap = BitmapFactory.decodeStream(in);
					in.close();

					FileOutputStream fOut = new FileOutputStream(file);
					bitMap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
					fOut.close();
				}
				return bitMap;
			} catch (JSONException e) {
				Log.e("log123", "JSONException = " + e);
			} catch (IOException e) {
				Log.e("log123", "IOException = " + e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap image) {
			if (viewHolder.position == position) {
				viewHolder.imageView.setImageBitmap(image);
			}
			viewHolder.progressBar.setVisibility(View.GONE);
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.imageView.setImageBitmap(image);
		}
	}

}
