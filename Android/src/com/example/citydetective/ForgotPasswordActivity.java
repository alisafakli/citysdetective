package com.example.citydetective;

import org.json.JSONObject;

import com.example.citydetective.webservice.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity {
	EditText etFemail;
	Button btnSendEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		etFemail = (EditText)findViewById(R.id.etFemail);
		btnSendEmail = (Button)findViewById(R.id.btnSendEmail);
		
		btnSendEmail.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(!etFemail.getText().toString().equals("")){
					new ServiceAsyncTaskForgotPassword().execute();
				}
				else
					Toast.makeText(getApplicationContext(), "Please write your e-mail.", Toast.LENGTH_LONG).show();
				
			}
		});
	}
	String error_msg;
	protected class ServiceAsyncTaskForgotPassword extends AsyncTask<String, String, String> {
		private ProgressDialog dialog = new ProgressDialog(ForgotPasswordActivity.this);
		Intent i;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Please wait...");
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
		
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.resetUserPassword(etFemail.getText().toString());				
			Log.e("JSON Parser", json.toString());
			try {
				if (json.getString("success") != null) {
					String res = json.getString("success");
					String errormsg = json.getString("error");
					if (Integer.parseInt(res) == 1 && Integer.parseInt(errormsg) != 1 ) {
						runOnUiThread(new Runnable(){
						    public void run() {
						    	try {
									Toast.makeText(getApplicationContext(), "Registered successfully.", Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}}});
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "Password successfully sent your e-mail.", Toast.LENGTH_LONG).show();
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
