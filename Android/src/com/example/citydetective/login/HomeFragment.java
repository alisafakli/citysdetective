package com.example.citydetective.login;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.citydetective.R;
import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.MyComplaints;
import com.example.citydetective.webservice.UserFunctions;

public class HomeFragment extends Fragment {
	String email="";
	private static final String KEY_EMAIL = "email";
	public HomeFragment(){}
	ListView list;
	public static ArrayList<MyComplaints> arrList;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        list = (ListView) rootView.findViewById(R.id.list_mycomplaints);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		DatabaseHandler db = new DatabaseHandler(getActivity());
		email = db.getUserDetails().get(KEY_EMAIL);
		if(email != null){
			if(isNetworkConnected())
				new ServiceAsyncTaskAll().execute();
			else
				Toast.makeText(getActivity(), "Internet baðlantýnýzý kontrol ediniz.", Toast.LENGTH_LONG).show();		
		}else
			Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_LONG).show();
		return rootView;
	}
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}
	
	boolean check = false;
	protected class ServiceAsyncTaskAll extends AsyncTask<String, String, String> {
		private ProgressDialog dialog = new ProgressDialog(getActivity());
		Intent i;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Lütfen bekleyiniz...");
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getApprovedCompaints();
			Log.e("JSON Parser", json.toString());
			try {
				if (json.getString("success") != null) {
					String res = json.getString("success");
					if (Integer.parseInt(res) == 1) {
						arrList = new ArrayList<MyComplaints>();
						JSONArray cast = json.getJSONArray("complaints");
						for (int i=0; i<cast.length(); i++) {
							MyComplaints sikayetler = new MyComplaints();
						    JSONObject actor = cast.getJSONObject(i);
						    String kullanici_email = actor.getString("kullanici_email");
						    String kullanici_id = actor.getString("kullanici_id");
						    String sikayet_fotograf = actor.getString("sikayet_fotograf");
						    String sikayet_aciklama = actor.getString("sikayet_aciklama");
						    String sikayet_latitude = actor.getString("sikayet_latitude");
						    String sikayet_longitude = actor.getString("sikayet_longitude");
						    String sikayet_kategori_id = actor.getString("sikayet_kategori_id");
						    String sikayet_onay = actor.getString("sikayet_onay");
						    String sikayet_onay_aciklama = actor.getString("sikayet_onay_aciklama");
						    String sikayet_tarih = actor.getString("sikayet_tarih");
						    
						    sikayetler.setKullanici_email(kullanici_email);
						    sikayetler.setKullanici_id(kullanici_id);
						    sikayetler.setSikayet_aciklama(sikayet_aciklama);
						    sikayetler.setSikayet_fotograf(sikayet_fotograf);
						    sikayetler.setSikayet_kategori_id(sikayet_kategori_id);
						    sikayetler.setSikayet_latitude(sikayet_latitude);
						    sikayetler.setSikayet_longitude(sikayet_longitude);
						    sikayetler.setSikayet_onay(sikayet_onay);
						    sikayetler.setSikayet_onay_aciklama(sikayet_onay_aciklama);
						    sikayetler.setSikayet_tarih(sikayet_tarih);
						    arrList.add(sikayetler);
						    check = true;
						}
					}else{
						check = false;
						Handler handler = new Handler(Looper.getMainLooper());

						handler.post(new Runnable() {

						        @Override
						        public void run() {
						            //Your UI code here
						        	Toast.makeText(getActivity(), "Not Found!", Toast.LENGTH_LONG).show();
						        }
						    });
					}
				}	
			} catch (final Exception e) {

			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				if(check){
					arrList = reverse(arrList);
				 list.setAdapter(new CustomListAdapterApprovedComplaints(arrList,
							getActivity()));
				}
				dialog.dismiss();

			} catch (final Exception e) {
				// TODO: handle exception
				Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {

			        @Override
			        public void run() {
			            //Your UI code here
			        	Toast.makeText(getActivity(), "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
			        }
			    });
				dialog.dismiss();
			}
		}
	}
	public ArrayList<MyComplaints> reverse(ArrayList<MyComplaints> list) {
	    if(list.size() > 1) {                   
	        MyComplaints value = list.remove(0);
	        reverse(list);
	        list.add(value);
	    }
	    return list;
	}
}
