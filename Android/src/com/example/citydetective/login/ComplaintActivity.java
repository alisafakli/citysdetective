package com.example.citydetective.login;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.citydetective.MainActivity;
import com.example.citydetective.R;
import com.example.citydetective.location.LocationActivity;
import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.UserFunctions;

public class ComplaintActivity extends ActionBarActivity {
	Context context;
	Button btnSelect, btnSend,btnCapture,btnLocation;
	final static int SELECT = 1;
	File file;
	String uploadFilePath = "";
	String uploadFileName = "";
	String upLoadServerUri = "http://citydetective.safakli.com/upload2/UploadToServer.php";
	int serverResponseCode = 0;
	TextView tv;
	public static TextView tvLocation;
	ImageView iv;
	EditText et;
	Spinner spinner;
	public static String comp_Lat="";
	public static String comp_Lng="";
	static final int REQUEST_LOCATION = 5;
	private static final String IMAGE_UPLOAD_URL = "http://citydetective.safakli.com/upload/upload_image.php";;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaint);
		context = this;
		tvLocation = (TextView)findViewById(R.id.tvLocation);
		tv = (TextView)findViewById(R.id.tvUName);
		iv = (ImageView)findViewById(R.id.imageView1);
		et = (EditText)findViewById(R.id.etComplaint);
		btnLocation = (Button)findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ComplaintActivity.this,LocationActivity.class);
				startActivityForResult(i, REQUEST_LOCATION);
			}
		});
		btnCapture = (Button) findViewById(R.id.btnCapture);
		btnCapture.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dispatchTakePictureIntent();
			}
		});
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				Intent intent = new Intent(Intent.ACTION_PICK, uri);
				startActivityForResult(intent, SELECT);
			}
		});

		btnSend = (Button) findViewById(R.id.btnSend);
		
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(file != null){
					if(!et.getText().toString().equals("")){
						//new BackgroundUploader(url, file).execute();
						showYesNoDialog();
					}else{
						Toast.makeText(getApplicationContext(), "Please write your complaint!", Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Please add a photo!", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		spinner = (Spinner) findViewById(R.id.category_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.category_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	@SuppressLint("NewApi")
	void showYesNoDialog(){
		final String url = IMAGE_UPLOAD_URL;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	new BackgroundUploader(url, file).execute();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to send this complaint?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		//builder.setView(R.drawable.layoutborder1);
	}
	
	static final int REQUEST_IMAGE_CAPTURE = 2;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	String extStorageDirectory = "";
	Bitmap bitmap=null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == RESULT_OK && requestCode == SELECT) {
			Uri image = data.getData();
			try {
				bitmap = Images.Media.getBitmap(getContentResolver(), image);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/CityDetective/";
			File dir = new File(Environment.getExternalStorageDirectory() + "/CityDetective");
			if(!dir.exists())
				dir.mkdir();
			OutputStream outStream = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			String dateTime = dateFormat.format(date);
			//           file = new File(extStorageDirectory, "IMG_"+dateTime+".png");
			String fileName = dateTime+"_image.png";
			file = new File(extStorageDirectory, fileName);
			try {
				outStream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			iv.setImageBitmap(bitmap);
			uploadFilePath = extStorageDirectory;
			uploadFileName = fileName;
			tv.setText(uploadFilePath);
			//tv.setText(extStorageDirectory);
		}
		else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
	        Bundle extras = data.getExtras();
//	        bitmap = (Bitmap) extras.get("data");
//	        iv.setImageBitmap(imageBitmap);
			//Uri image = data.getData();
			try {
				bitmap = (Bitmap) extras.get("data");
		        iv.setImageBitmap(bitmap);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/CityDetective/";
			File dir = new File(Environment.getExternalStorageDirectory() + "/CityDetective");
			if(!dir.exists())
				dir.mkdir();
			OutputStream outStream = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			String dateTime = dateFormat.format(date);
			//           file = new File(extStorageDirectory, "IMG_"+dateTime+".png");
			String fileName = dateTime+"_image.png";
			file = new File(extStorageDirectory, fileName);
			try {
				outStream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			iv.setImageBitmap(bitmap);
			uploadFilePath = extStorageDirectory;
			uploadFileName = fileName;
			tv.setText(uploadFilePath);
		}
		
		//LOCATION
		else if(requestCode == REQUEST_LOCATION && resultCode == RESULT_OK){
			comp_Lat =data.getStringExtra("comp_Lat");
			comp_Lng =data.getStringExtra("comp_Lng");
			
			tvLocation.setText("Lat: " + comp_Lat +"\n"+
						"Lng: "+ comp_Lng);
		}
	}

	ProgressDialog dialog;
	class BackgroundUploader extends AsyncTask<Object, Integer,String> {

		private String url;
		private File file;

		public BackgroundUploader(String url, File file) {
			this.url = url;
			this.file = file; 
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMessage("Uploading...");
			dialog.setCancelable(false);
			dialog.setMax((int) file.length());
			dialog.show();
		}

		@Override
		protected String doInBackground(Object... arg0) {		
			String fileName = uploadFilePath+""+uploadFileName;
			HttpURLConnection conn = null;
			DataOutputStream dos = null;  
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024; 
			File sourceFile = new File(fileName); 

			if (!sourceFile.isFile()) {

				dialog.dismiss(); 

				Log.e("uploadFile", "Source File not exist :"
						+uploadFilePath + "" + uploadFileName);
				Log.e("File Path: ", "Source File not exist :"+uploadFilePath + "/" + uploadFileName);

				runOnUiThread(new Runnable() {
					public void run() {
						tv.setText("Source File not exist :"
								+uploadFilePath + "" + uploadFileName);
					}
				}); 

				return "0";

			}
			else
			{
				try { 

					// open a URL connection to the Servlet
					FileInputStream fileInputStream = new FileInputStream(sourceFile);
					
					URL url = new URL(upLoadServerUri);

					// Open a HTTP  connection to  the URL
					conn = (HttpURLConnection) url.openConnection(); 
					conn.setDoInput(true); // Allow Inputs
					conn.setDoOutput(true); // Allow Outputs
					conn.setUseCaches(false); // Don't use a Cached Copy
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("ENCTYPE", "multipart/form-data");
					conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
					conn.setRequestProperty("uploaded_file", fileName); 

					dos = new DataOutputStream(conn.getOutputStream());

					dos.writeBytes(twoHyphens + boundary + lineEnd); 
					dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
							+ fileName + "\"" + lineEnd);

					dos.writeBytes(lineEnd);

					// create a buffer of  maximum size
					bytesAvailable = fileInputStream.available(); 

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					// read file and write it into form...
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

					while (bytesRead > 0) {
						int bytesread2 = bytesRead;
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
						int bytesavail = bytesAvailable;
						
						//int process = (bytesread2 * 100)/bytesavail;
						//publishProgress(process);
						Log.e("bytesRead:" ,Integer.toString(bytesRead));
	
						Log.e("bytesAvailable:" ,Integer.toString(bytesAvailable));
						
						//Log.e("process:" ,Integer.toString(process));
//						int process = bytesRead/bufferSize * 100;
//						publishProgress(process);

					}

					//publishProgress(100);

					// send multipart form data necesssary after file data...
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

					// Responses from the server (code and message)
					serverResponseCode = conn.getResponseCode();
					String serverResponseMessage = conn.getResponseMessage();

					Log.i("uploadFile", "HTTP Response is : " 
							+ serverResponseMessage + ": " + serverResponseCode);

					if(serverResponseCode == 200){

						runOnUiThread(new Runnable() {
							public void run() {

								String msg = "File Upload Completed."+uploadFileName+"\n\n"+
								"Check http://citydetective.safakli.com/upload2/uploads/"+uploadFileName ;

								tv.setText(msg);
								Toast.makeText(getApplicationContext(), "File Upload Complete.", 
										Toast.LENGTH_LONG).show();
							}
						});                
					}    

					//close the streams //
					fileInputStream.close();
					dos.flush();
					dos.close();
					
//		            user.put(KEY_ID, cursor.getString(0));
//		            user.put(KEY_NAME, cursor.getString(1));
//		            user.put(KEY_SURNAME, cursor.getString(2));
//		            user.put(KEY_EMAIL, cursor.getString(3));
//		            user.put(KEY_TELEPHONE, cursor.getString(4));
					
					//send data to web services
					sendDataToWebService();
					

				} catch (MalformedURLException ex) {

					dialog.dismiss();  
					ex.printStackTrace();

					runOnUiThread(new Runnable() {
						public void run() {
							tv.setText("MalformedURLException Exception : check script url.");
							Toast.makeText(ComplaintActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
						}
					});

					Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
				} catch (Exception e) {

					dialog.dismiss();  
					e.printStackTrace();

					runOnUiThread(new Runnable() {
						public void run() {
							tv.setText("Got Exception : see logcat ");
							Toast.makeText(ComplaintActivity.this, "Got Exception : see logcat ", 
									Toast.LENGTH_SHORT).show();
						}
					});
					Log.e("Upload file to server Exception", "Exception : " 
							+ e.getMessage(), e);  
				}
				dialog.dismiss();       
				String str = Integer.toString(serverResponseCode);
				return str; 

			} // End else block 
		}
		String error_msg;
	    private static final String KEY_ID = "id";
	    private static final String KEY_EMAIL = "email";
		private void sendDataToWebService() {
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			String id = db.getUserDetails().get(KEY_ID).toString();
			String email = db.getUserDetails().get(KEY_EMAIL).toString();
			String category_id = Integer.toString(spinner.getSelectedItemPosition());
			UserFunctions userFunctions = new UserFunctions();
			JSONObject json2 = userFunctions.addComplaint(email, id, uploadFileName, et.getText().toString(), comp_Lat, comp_Lng, category_id, "waiting", "");
					
			Log.e("JSON Parser", json2.toString());
			try {
				if (json2.getString("success") != null) {
					String res = json2.getString("success");
					String errormsg = json2.getString("error");
					if (Integer.parseInt(res) == 1 && Integer.parseInt(errormsg) != 1 ) {
						
						runOnUiThread(new Runnable(){
						    public void run() {
						    	try {
									Toast.makeText(getApplicationContext(), "Thank your for your complaint." , Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}}});
						dialog.dismiss();
						

						finish();
					}else{
						error_msg = json2.getString("error_msg");
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
		
			
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			if (progress[0] < 20) {            
				dialog.setProgress(0);
			} else if (progress[0] < 40) {            
				dialog.setProgress(20);
			}
			else if (progress[0] < 60) {            
				dialog.setProgress(40);
			}
			else if (progress[0] < 80) {            
				dialog.setProgress(60);
			}
			else if (progress[0] < 100) {            
				dialog.setProgress(80);
			}
			else if (progress[0] == 100) {            
				dialog.setProgress(100);
			}
		}


	} 
}
