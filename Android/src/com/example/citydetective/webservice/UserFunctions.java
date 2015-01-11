package com.example.citydetective.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
/**
 * @author ${Ali Safakli}
 */

public class UserFunctions {

	private JSONParser jsonParser;

	// Testing in localhost using wamp or xampp
	// use http://10.0.2.2/ to connect to your localhost ie http://localhost/
//	private static String loginURL = "http://www.emreduman.com/software/";
//	private static String signupURL ="http://www.emreduman.com/software/";
	private static String loginURL ="http://citydetective.safakli.com/";
	private static String signupURL ="http://citydetective.safakli.com/";
	private static String add_complaintURL ="http://citydetective.safakli.com/";
	private static String getMyComplaintsURL ="http://citydetective.safakli.com/";
	private static String getApprovedComplaintsURL ="http://citydetective.safakli.com/";
	private static String getComplaintsViaCategory_URL ="http://citydetective.safakli.com/";
	private static String update_user_info_URL ="http://citydetective.safakli.com/";
	private static String update_password_URL ="http://citydetective.safakli.com/";
	private static String reset_password_URL ="http://citydetective.safakli.com/";
	// User Info Update Tag
	private static String login_tag = "login";
	private static String signup_tag = "signup";
	private static String add_complaint_tag = "add_complaint";
	private static String getMyComplaints_tag = "getMyComplaints";
	private static String getApprovedCompaints_tag = "getApprovedComplaints";
	
	private static String getComplaintsViaCategory_tag = "getComplaintsViaCategory";
	private static String update_user_info_tag = "update_user_info";
	private static String update_password_tag = "update_password";
	private static String reset_password_tag = "resetPassword";
	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject signupUser(String mail, String sifre,String ad,String soyad, String tel ) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", signup_tag));
		params.add(new BasicNameValuePair("mail", mail));
		params.add(new BasicNameValuePair("sifre", sifre));
		params.add(new BasicNameValuePair("ad", ad));
		params.add(new BasicNameValuePair("soyad", soyad));
		params.add(new BasicNameValuePair("tel", tel));
		JSONObject json = jsonParser.getJSONFromUrl(signupURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject loginUser(String mail, String sifre) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("mail", mail));
		params.add(new BasicNameValuePair("sifre", sifre));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject addComplaint(String kullanici_email,String kullanici_id,
			String sikayet_fotograf,String sikayet_aciklama,String sikayet_latitude,
			String sikayet_longitude,String sikayet_kategori_id, String sikayet_onay,String sikayet_onay_aciklama ) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", add_complaint_tag));
		params.add(new BasicNameValuePair("kullanici_email", kullanici_email));
		params.add(new BasicNameValuePair("kullanici_id", kullanici_id));
		params.add(new BasicNameValuePair("sikayet_fotograf", sikayet_fotograf));
		params.add(new BasicNameValuePair("sikayet_aciklama", sikayet_aciklama));
		params.add(new BasicNameValuePair("sikayet_latitude", sikayet_latitude));
		params.add(new BasicNameValuePair("sikayet_longitude", sikayet_longitude));
		params.add(new BasicNameValuePair("sikayet_kategori_id", sikayet_kategori_id));
		params.add(new BasicNameValuePair("sikayet_onay", sikayet_onay));
		params.add(new BasicNameValuePair("sikayet_onay_aciklama", sikayet_onay_aciklama));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		
		params.add(new BasicNameValuePair("sikayet_tarih", currentDateandTime));
		JSONObject json = jsonParser.getJSONFromUrl(add_complaintURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject getMyCompaints(String mail) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getMyComplaints_tag));
		params.add(new BasicNameValuePair("mail", mail));
		JSONObject json = jsonParser.getJSONFromUrl(getMyComplaintsURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject getApprovedCompaints() {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getApprovedCompaints_tag));
		params.add(new BasicNameValuePair("onay", "onaylandi"));
		JSONObject json = jsonParser.getJSONFromUrl(getApprovedComplaintsURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject getComplaintsViaCategory(String sikayet_kategori_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getComplaintsViaCategory_tag));
		params.add(new BasicNameValuePair("sikayet_kategori_id", sikayet_kategori_id));
		JSONObject json = jsonParser.getJSONFromUrl(getComplaintsViaCategory_URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}//updateUserInfo($kullanici_id, $kullanici_mail, $kullanici_telefon)
	public JSONObject updateUserInfo(String kullanici_id,String kullanici_mail,String kullanici_telefon) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", update_user_info_tag));
		params.add(new BasicNameValuePair("kullanici_id", kullanici_id));
		params.add(new BasicNameValuePair("kullanici_mail", kullanici_mail));
		params.add(new BasicNameValuePair("kullanici_telefon", kullanici_telefon));
		
		JSONObject json = jsonParser.getJSONFromUrl(update_user_info_URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	//updateUserPassword($kullanici_id, $kullanici_mail, $kullanici_yeni_sifre){
	public JSONObject updateUserPassword(String kullanici_id,String kullanici_mail,String kullanici_yeni_sifre) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", update_password_tag));
		params.add(new BasicNameValuePair("kullanici_id", kullanici_id));
		params.add(new BasicNameValuePair("kullanici_mail", kullanici_mail));
		params.add(new BasicNameValuePair("kullanici_yeni_sifre", kullanici_yeni_sifre));
		JSONObject json = jsonParser.getJSONFromUrl(update_password_URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject resetUserPassword(String kullanici_mail) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", reset_password_tag));
		params.add(new BasicNameValuePair("kullanici_email", kullanici_mail));
		JSONObject json = jsonParser.getJSONFromUrl(reset_password_URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}


	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

}
