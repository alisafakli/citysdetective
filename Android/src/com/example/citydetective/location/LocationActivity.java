package com.example.citydetective.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.citydetective.R;
import com.example.citydetective.login.ComplaintActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class LocationActivity extends FragmentActivity {

	GoogleMap map;
	ArrayList<LatLng> markerPoints;
	Button btnCurrentLoc,btnSelectedLoc;
	String s_lat="",s_lng="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		btnCurrentLoc = (Button)findViewById(R.id.btnCurrentLoc);
		btnCurrentLoc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		         Criteria criteria = new Criteria();

		         Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
		         if (location != null)
		         {
		        	 Intent returnIntent = new Intent();
		        	 returnIntent.putExtra("comp_Lat",Double.toString(location.getLatitude()));
		        	 returnIntent.putExtra("comp_Lng",Double.toString(location.getLongitude()));
		        	 setResult(RESULT_OK,returnIntent);
		        	 finish();
		         }
			}
		});
		btnSelectedLoc = (Button)findViewById(R.id.btnSelectedLoc);
		btnSelectedLoc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(s_lat!="" && s_lng!=""){
		        	 Intent returnIntent = new Intent();
		        	 returnIntent.putExtra("comp_Lat",s_lat);
		        	 returnIntent.putExtra("comp_Lng",s_lng);
		        	 setResult(RESULT_OK,returnIntent);
		        	 finish();
				}else{
					Toast.makeText(getApplicationContext(), "Please select a location!", Toast.LENGTH_LONG).show();
				}
			}
		});
		// Initializing 
		markerPoints = new ArrayList<LatLng>();
		
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		
		// Getting Map for the SupportMapFragment
		map = fm.getMap();		
		
		if(map!=null){
		
			// Enable MyLocation Button in the Map
			map.setMyLocationEnabled(true);		
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			//map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), 12.0f));
			// Setting onclick event listener for the map
			zoomCurrentLocation();
			map.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng point) {
					
					// Already two locations				
					if(markerPoints.size()>0){
						markerPoints.clear();
						map.clear();					
					}
					
					// Adding new item to the ArrayList
					markerPoints.add(point);				
					
					// Creating MarkerOptions
					MarkerOptions options = new MarkerOptions();
					
					// Setting the position of the marker
					options.position(point);
					
					/** 
					 * For the start location, the color of marker is GREEN and
					 * for the end location, the color of marker is RED.
					 */
					if(markerPoints.size()==1){
						options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//					}else if(markerPoints.size()==2){
//						options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					}
								
					
					// Add new marker to the Google Map Android API V2
					map.addMarker(options);
					
					// Checks, whether start and end locations are captured
					if(markerPoints.size() >= 1){					
						LatLng origin = markerPoints.get(0);
						s_lat = Double.toString(origin.latitude);
						s_lng = Double.toString(origin.longitude);
//						LatLng dest = markerPoints.get(1);
						
						// Getting URL to the Google Directions API
//						String url = getDirectionsUrl(origin, dest);				
//						
//						DownloadTask downloadTask = new DownloadTask();
//						
//						// Start downloading json data from Google Directions API
//						downloadTask.execute(url);
					}
					
				}
			});
		}		
	}
	Location location;
	private void zoomCurrentLocation() {
		// TODO Auto-generated method stub
		 LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         Criteria criteria = new Criteria();

         Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
         if (location != null)
         {
             map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                     new LatLng(location.getLatitude(), location.getLongitude()), 13));

             CameraPosition cameraPosition = new CameraPosition.Builder()
             .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
             .zoom(17)                   // Sets the zoom
             .bearing(90)                // Sets the orientation of the camera to east
             .tilt(40)                   // Sets the tilt of the camera to 30 degrees
             .build();                   // Creates a CameraPosition from the builder
         map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

         }

	}

	private String getDirectionsUrl(LatLng origin,LatLng dest){
					
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		String mode = "driving";
		mode = "walking";
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&mode="+mode;
		
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);					
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);	
				
			}
			
			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);							
		}			
    }   
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
}