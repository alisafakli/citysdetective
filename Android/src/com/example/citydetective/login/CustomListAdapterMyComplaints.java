package com.example.citydetective.login;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import com.example.citydetective.R;
import com.example.citydetective.webservice.MyComplaints;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListAdapterMyComplaints extends BaseAdapter{
	TextView kullanici_email,
			sikayet_aciklama,
			sikayet_latitude,
			sikayet_longitude,
			sikayet_kategori_id,
			sikayet_onay,
			sikayet_onay_aciklama;


	ImageView image;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.mycomplaints_list, null );
		}

		image = (ImageView) v.findViewById(R.id.imageView1);
		TextView kullanici_email = (TextView)v.findViewById(R.id.kullanici_email);
		TextView sikayet_aciklama = (TextView)v.findViewById(R.id.sikayet_aciklama);
		TextView sikayet_latitude = (TextView)v.findViewById(R.id.sikayet_latitude);
		TextView sikayet_longitude = (TextView)v.findViewById(R.id.sikayet_longitude);
		TextView sikayet_kategori_id = (TextView)v.findViewById(R.id.sikayet_kategori_id);
		TextView sikayet_onay = (TextView)v.findViewById(R.id.sikayet_onay);
		TextView sikayet_onay_aciklama = (TextView)v.findViewById(R.id.sikayet_onay_aciklama);


		MyComplaints msg = _data.get(position);
		//          image.setImageResource(msg.hareket_image);
		new DownloadImageTask(image,msg.getSikayet_fotograf()).execute();
		kullanici_email.setText(msg.getKullanici_email());
		if(!msg.getSikayet_aciklama().equals(""))
			sikayet_aciklama.setText(msg.getSikayet_aciklama());
		else
			sikayet_aciklama.setText("");
		sikayet_latitude.setText("Latitude: "+msg.getSikayet_latitude());   
		sikayet_longitude.setText("Longitude: " + msg.getSikayet_longitude());
		
		//
		/*<item>Traffic</item>
        <item>Road,Sidewalk,Lighting</item>
        <item>Waste</item>
        <item>Street Animals</item>
        <item>Sewage</item>
        <item>Disabled Rights</item>
        <item>Others</item>
		 * 
		 */
		//
		switch(msg.getSikayet_kategori_id()){
		case "0":
			sikayet_kategori_id.setText("Traffic");
			break;
		case "1":
			sikayet_kategori_id.setText("Road,Sidewalk,Lighting");
			break;
		case "2":
			sikayet_kategori_id.setText("Waste");
			break;
		case "3":
			sikayet_kategori_id.setText("Street Animals");
			break;
		case "4":
			sikayet_kategori_id.setText("Sewage");
			break;
		case "5":
			sikayet_kategori_id.setText("Disabled Rights");
			break;
		case "6":
			sikayet_kategori_id.setText("Others");
			break;
		default:
			break;
		
		}
		sikayet_onay.setText(msg.getSikayet_onay());
		sikayet_onay_aciklama.setText(msg.getSikayet_onay_aciklama());

		return v;	
	}
	private ArrayList<MyComplaints> _data;
	Context _c;

	public CustomListAdapterMyComplaints (ArrayList<MyComplaints> hediyedetay, Context c){
		_data = hediyedetay;
		_c = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		String url;
		//private ProgressDialog dialog = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute() {
			//dialog.setMessage("Yükleniyor...");
			//dialog.show();
		}

		public DownloadImageTask(ImageView bmImage, String url) {
			this.bmImage = bmImage;
			this.url = url;
		}
		private static final String IMAGE_URL = "http://citydetective.safakli.com/upload2/uploads";
		protected Bitmap doInBackground(String... urls) {

			String urldisplay =IMAGE_URL + "/"+ url;
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			Bitmap res = result;
			bmImage.setImageBitmap(res);
			//dialog.dismiss();
		}
	}
}
