package com.example.citydetective.login;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.citydetective.R;
import com.example.citydetective.location.MyComplaintLocation;
import com.example.citydetective.webservice.MyComplaints;

public class CustomListAdapterApprovedComplaints extends BaseAdapter{



	ImageView image,ivMap;
	Context context;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.allcomplaints_list, null );
		}
		context = v.getContext();
		image = (ImageView) v.findViewById(R.id.imageView1);
		ivMap = (ImageView) v.findViewById(R.id.ivMap);
		TextView kullanici_email = (TextView)v.findViewById(R.id.kullanici_email);
		TextView sikayet_aciklama = (TextView)v.findViewById(R.id.sikayet_aciklama);
		TextView sikayet_kategori_id = (TextView)v.findViewById(R.id.sikayet_kategori_id);
		TextView sikayet_onay = (TextView)v.findViewById(R.id.sikayet_onay);
		TextView sikayet_onay_aciklama = (TextView)v.findViewById(R.id.sikayet_onay_aciklama);
		TextView sikayet_tarih = (TextView)v.findViewById(R.id.sikayet_tarih);


		final MyComplaints msg = _data.get(position);
		//          image.setImageResource(msg.hareket_image);
		new DownloadImageTask(image,msg.getSikayet_fotograf()).execute();
		
		String kul_mail = msg.getKullanici_email();
		String[] mail = kul_mail.split("@");
		String befoneAt_part1 = mail[0]; // 004
		befoneAt_part1= befoneAt_part1.replace(befoneAt_part1.substring(1, befoneAt_part1.length()),"***");
		
		String afterAt = mail[1]; // 034556
		String[] point2 = afterAt.split("\\.");
		String beforePoint = point2[0];
		beforePoint= beforePoint.replace(beforePoint.substring(1, beforePoint.length()),"***");
		
		String afterPoint = point2[1];
		afterPoint= afterPoint.replace(afterPoint.substring(1, afterPoint.length()),"***");
		
		String censoredEmail = befoneAt_part1+"@"+beforePoint+"."+afterPoint;
		
		kullanici_email.setText(censoredEmail);
		
		if(!msg.getSikayet_aciklama().equals(""))
			sikayet_aciklama.setText(msg.getSikayet_aciklama());
		else
			sikayet_aciklama.setText("");
		
		if(!msg.getSikayet_latitude().equals("") && !msg.getSikayet_longitude().equals("")){
			ivMap.setVisibility(View.VISIBLE);
		}
		else
			ivMap.setVisibility(View.INVISIBLE);
		
		ivMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(this,"Latitude:" + msg.getSikayet_latitude().toString() + "\nLongitude:"+msg.getSikayet_longitude() , Toast.LENGTH_LONG).show();
				 Intent intent=new Intent(context,MyComplaintLocation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 intent.putExtra("latitude", msg.getSikayet_latitude());
				 intent.putExtra("longitude", msg.getSikayet_longitude());
				 context.startActivity(intent);
			}
		});
		if(!msg.getSikayet_tarih().equals("")){
			sikayet_tarih.setVisibility(View.VISIBLE);
			sikayet_tarih.setText(msg.getSikayet_tarih());
		}else
			sikayet_tarih.setVisibility(View.INVISIBLE);
//		sikayet_latitude.setText("Latitude: "+msg.getSikayet_latitude());   
//		sikayet_longitude.setText("Longitude: " + msg.getSikayet_longitude());
		
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

	public CustomListAdapterApprovedComplaints (ArrayList<MyComplaints> hediyedetay, Context c){
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
