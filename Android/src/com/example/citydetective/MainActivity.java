package com.example.citydetective;

import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.UserFunctions;

public class MainActivity extends ActionBarActivity  {
	TextView tvForgotPassword,tvRegister;
	EditText etEmail,etPassword;
	Button btnLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etEmail = (EditText)findViewById(R.id.etE_mail);
		etEmail.setText("ali@safakli.com");
		etPassword = (EditText)findViewById(R.id.etPassword);
		etPassword.setText("123123123");
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
//				Intent i = new Intent(LoginActivity.this, com.example.citydetective.login.LoginActivity.class);
//				startActivity(i);
//				finish();
				if(!etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")){
					new ServiceAsyncTask().execute();
				}
				
			}
		});
		tvForgotPassword = (TextView)findViewById(R.id.tvForgotpassword);
		tvForgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
			
				
			}
		});
		tvRegister = (TextView)findViewById(R.id.tvRegister);
		tvRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SignUpActivity.class);
				startActivity(i);
			}
		});
	}
	String error_msg;
	DatabaseHandler db;
	protected class ServiceAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
			JSONObject json = userFunction.loginUser(etEmail.getText().toString(), etPassword.getText().toString());					
			Log.e("JSON Parser", json.toString());
			try {
				if (json.getString("success") != null) {
					String res = json.getString("success");
					String errormsg = json.getString("error");
					if (Integer.parseInt(res) == 1 && Integer.parseInt(errormsg) != 1 ) {
						final JSONObject json_user = json.getJSONObject("user");
						db = new DatabaseHandler(getApplicationContext());
						userFunction.logoutUser(getApplicationContext());	
						
						db.addUser(json_user.getString("id"),
								json_user.getString("ad"),
								json_user.getString("soyad"),
								json_user.getString("mail"),
								json_user.getString("sifre"),
								json_user.getString("telefon"));
						runOnUiThread(new Runnable(){
						    public void run() {
						    	try {
									Toast.makeText(getApplicationContext(), "Welcome "+json_user.getString("ad") + " " + json_user.getString("soyad")+ "!"  , Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}}});
						dialog.dismiss();
						
						Log.e("LOGIN OK","PASS!!!" );
						i = new Intent(MainActivity.this,com.example.citydetective.LoginActivity.class);
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
