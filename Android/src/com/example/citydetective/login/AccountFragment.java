package com.example.citydetective.login;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.citydetective.MainActivity;
import com.example.citydetective.R;
import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.UserFunctions;

public class AccountFragment extends Fragment {

	public AccountFragment(){}
	TextView tvUName,tvMail;
	EditText etNewPhone,etCurrentPassword,etNewPassword1,etNewPassword2;
	Button btnSavePassword,btnSavePhone;
	DatabaseHandler db;
	HashMap<String, String> lst;
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_SURNAME = "surname";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_TELEPHONE = "telephone";
	private static final String TABLE_LOGIN = "login";
	ProgressDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_account, container, false);

		db = new DatabaseHandler(getActivity());
		lst = db.getUserDetails();

		tvUName = (TextView)v.findViewById(R.id.tvUName);
		tvMail = (TextView)v.findViewById(R.id.tvMail);
		etNewPhone = (EditText)v.findViewById(R.id.etNewPhone);
		etCurrentPassword = (EditText)v.findViewById(R.id.etCurrentPassword);
		//etCurrentPassword.setText(lst.get(KEY_PASSWORD));
		etNewPassword1 = (EditText)v.findViewById(R.id.etNewPassword1);
		etNewPassword2 = (EditText)v.findViewById(R.id.etNewPassword2);
		btnSavePhone = (Button)v.findViewById(R.id.btnSavePhone);

		//initialise UI
		tvUName.setText(lst.get(KEY_NAME));
		tvUName.append(" "+lst.get(KEY_SURNAME));
		tvMail.setText(lst.get(KEY_EMAIL));
		etNewPhone.setText(lst.get(KEY_TELEPHONE));

		dialog = new ProgressDialog(getActivity());

		btnSavePhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!etNewPhone.getText().equals(lst.get(KEY_TELEPHONE))){
					if(etNewPhone.getText().length()>=10 && etNewPhone.getText().length()<12){
						UserFunctions userFunctions = new UserFunctions();
						if(isNetworkConnected()){
							JSONObject json = userFunctions.updateUserInfo(lst.get(KEY_ID), lst.get(KEY_EMAIL), etNewPhone.getText().toString());					
							Log.e("JSON Parser", json.toString());
							dialog.setMessage("Please wait...");
							dialog.show();
							try {
								if (json.getString("success") != null) {
									String res = json.getString("success");
									String errormsg = json.getString("error");
									if (Integer.parseInt(res) == 1 && Integer.parseInt(errormsg) != 1 ) {
										db.updatePhone(lst.get(KEY_ID), etNewPhone.getText().toString());
										Toast.makeText(getActivity(), "Phone Number Updated as:" + etNewPhone.getText().toString(), Toast.LENGTH_LONG).show();
										dialog.dismiss();
									}else{
										String error_msg = json.getString("error_msg");
										Toast.makeText(getActivity(), error_msg, Toast.LENGTH_LONG).show();
										dialog.dismiss();
									} 
								}
							}catch (Exception e) {
								dialog.dismiss();
								e.printStackTrace();
							}
						}else{
							Toast.makeText(getActivity(), "Internet baðlantýnýzý kontrol ediniz.", Toast.LENGTH_LONG).show();
						}
					}else
						Toast.makeText(getActivity(), "Phone number must at least 10 digit!", Toast.LENGTH_LONG).show();
				}else
					Toast.makeText(getActivity(), "New phone number same as current phone number!", Toast.LENGTH_LONG).show();
			}
		});
		btnSavePassword = (Button)v.findViewById(R.id.btnSavePassword);
		btnSavePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!etCurrentPassword.getText().equals(lst.get(KEY_PASSWORD))){
					if(etNewPassword1.getText().toString().equals(etNewPassword2.getText().toString())){
						if(etNewPassword1.getText().toString().length()>=8){
							UserFunctions userFunctions = new UserFunctions();
							if(isNetworkConnected()){
								JSONObject json = userFunctions.updateUserPassword(lst.get(KEY_ID), lst.get(KEY_EMAIL), etNewPassword1.getText().toString());					
								Log.e("JSON Parser", json.toString());
								dialog.setMessage("Please wait...");
								dialog.show();
								try {
									if (json.getString("success") != null) {
										String res = json.getString("success");
										String errormsg = json.getString("error");
										if (Integer.parseInt(res) == 1 && Integer.parseInt(errormsg) != 1 ) {
											db.updatePhone(lst.get(KEY_ID), etNewPhone.getText().toString());
											Toast.makeText(getActivity(), "Password successfully updated.", Toast.LENGTH_LONG).show();
											dialog.dismiss();
											etCurrentPassword.setText("");
											etNewPassword1.setText("");
											etNewPassword2.setText("");
										}else{
											String error_msg = json.getString("error_msg");
											Toast.makeText(getActivity(), error_msg, Toast.LENGTH_LONG).show();
											dialog.dismiss();
										} 
									}
								}catch (Exception e) {
									dialog.dismiss();
									e.printStackTrace();
								}
							}else{
								Toast.makeText(getActivity(), "Internet baðlantýnýzý kontrol ediniz.", Toast.LENGTH_LONG).show();
							}
						}else
							Toast.makeText(getActivity(), "Password must has at least 8 digit!", Toast.LENGTH_LONG).show();
					}else
						Toast.makeText(getActivity(), "New passwords are not same!", Toast.LENGTH_LONG).show();
				}else
					Toast.makeText(getActivity(), "Current password is wrong!", Toast.LENGTH_LONG).show();
			}
		});

		return v;
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
}
