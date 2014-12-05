package com.example.citydetective;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends ActionBarActivity {
	Button btnCancel,btnRegister;
	EditText etName,etLname,etE_mail,etPw1,etPw2,etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		initialiseUI();
		
	}

	private void initialiseUI() {
		// TODO Auto-generated method stub
		etName= (EditText)findViewById(R.id.etName);
		etLname= (EditText)findViewById(R.id.etLname);
		etE_mail= (EditText)findViewById(R.id.etE_mail);
		etPw1= (EditText)findViewById(R.id.etPw1);
		etPw2 = (EditText)findViewById(R.id.etPw2);
		etPhone = (EditText)findViewById(R.id.etPhone);
		 
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SignUpActivity.this,MainActivity.class);
				startActivity(i);
			}
		});
		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//new ServiceAsyncTask().execute();
				if(loadDataOK() == "OK")
					new ServiceAsyncTask().execute();
				else
					Toast.makeText(getApplicationContext(), loadDataOK(), Toast.LENGTH_LONG).show();
			}
		});
	}
	String mail="",sifre="",sifreTekrar="",ad="",soyad="", tel="";
	private String loadDataOK() {
		mail = etE_mail.getText().toString();
		sifre = etPw1.getText().toString();
		sifreTekrar = etPw2.getText().toString();
		ad =etName.getText().toString();
		soyad = etLname.getText().toString();
		tel = etPhone.getText().toString();
		
		//check valid data
		if(!mail.equals("")&& !sifre.equals("")&& !sifreTekrar.equals("")&& !ad.equals("")&& !soyad.equals("")
				&& !tel.equals("")){
			if(sifre.equals(sifreTekrar)){
				if(sifre.length()>7){
					if(mail.contains("@")){
						return "OK";
					}else{
						return "E-mail address not correct!";
					}
				}else
					return "Password must contain at least 8 character!";
			}else
				return "Passwords are not matching!";
		}else
			return "Please fill all fields.";
	}
	DatabaseHandler db;
	String error_msg;
	protected class ServiceAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
		Intent i;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Please wait...");
//			dialog.
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
		
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.signupUser(mail, sifre, ad, soyad, tel);					
			Log.e("JSON Parser", json.toString());
			try {
				if (json.getString("success") != null) {
					String res = json.getString("success");
					String errormsg = json.getString("error");
					if (Integer.parseInt(res) == 1 && Integer.parseInt(errormsg) != 1 ) {
						//final JSONObject json_user = json.getJSONObject("user");
//						db = new DatabaseHandler(getApplicationContext());
//						userFunction.logoutUser(getApplicationContext());	
//						
//						db.addUser(json_user.getString(KEY_ID),
//								json_user.getString(KEY_NAME),json_user.getString(KEY_SURNAME),
//								json_user.getString(KEY_EMAIL),json_user.getString(KEY_ADDRESS),
//								json_user.getString(KEY_TELEPHONE),json_user.getString(KEY_BIRTHDATE));
						runOnUiThread(new Runnable(){
						    public void run() {
						    	try {
									Toast.makeText(getApplicationContext(), "Registered successfully.", Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}}});
						//Progress dialoðu kaldýrýyoruz ve giriþ yapýldýðý için kullanýcýyý 
						//kendi bilgilerinin bulunduðu yeni bir Activity'e yönlendiriyoruz.
						dialog.dismiss();
						i = new Intent(SignUpActivity.this,MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						finish();
					}else{
						error_msg = json.getString("error_msg");
						dialog.dismiss();
						runOnUiThread(new Runnable(){
						    public void run() {
						    	try {
									Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						});}}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {


			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	
}
