package com.example.citydetective;



import static com.example.citydetective.utils.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.example.citydetective.utils.CommonUtilities.EXTRA_MESSAGE;
import static com.example.citydetective.utils.CommonUtilities.SENDER_ID;
import static com.example.citydetective.utils.CommonUtilities.SERVER_URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.citydetective.login.AccountFragment;
import com.example.citydetective.login.CategoriesFragment;
import com.example.citydetective.login.ComplaintActivity;
import com.example.citydetective.login.HomeFragment;
import com.example.citydetective.login.MyComplaintsFragment;
import com.example.citydetective.login.ServerMessages;
import com.example.citydetective.login.slidingmenu.NavDrawerItem;
import com.example.citydetective.login.slidingmenu.NavDrawerListAdapter;
import com.example.citydetective.utils.ServerUtilities;
import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.UserFunctions;
import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
    public static String name;
    public static String email;
    UserFunctions userFunctions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainlogon);
		//db.resetTables();
		userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			mTitle = mDrawerTitle = getTitle();
	
			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
	
			// nav drawer icons from resources
			navMenuIcons = getResources()
					.obtainTypedArray(R.array.nav_drawer_icons);
	
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	
			navDrawerItems = new ArrayList<NavDrawerItem>();
	
			// adding nav drawer items to array
			// Home
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
			// Find People
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
			// Photos
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
			// Communities, Will add a counter here
	//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
			// Pages
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
			// What's hot, We  will add a counter here
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
			
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
			
	
			// Recycle the typed array
			navMenuIcons.recycle();
	
			mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	
			// setting the nav drawer list adapter
			adapter = new NavDrawerListAdapter(getApplicationContext(),
					navDrawerItems);
			mDrawerList.setAdapter(adapter);
	
			// enabling action bar app icon and behaving it as toggle button
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
	
			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, //nav menu toggle icon
					R.string.app_name, // nav drawer open - description for accessibility
					R.string.app_name // nav drawer close - description for accessibility
			) {
				public void onDrawerClosed(View view) {
					getSupportActionBar().setTitle(mTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					supportInvalidateOptionsMenu();
				}
	
				public void onDrawerOpened(View drawerView) {
					getSupportActionBar().setTitle(mDrawerTitle);
					// calling onPrepareOptionsMenu() to hide action bar icons
					supportInvalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);
	
			if (savedInstanceState == null) {
				// on first time display view for first nav item
				displayView(0);
			}
			//PUSH NOTIFICATION
			initialisePushNotification();
		}else{
			Intent login = new Intent(getApplicationContext(),
					com.example.citydetective.MainActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			finish();	
		}
	}
	
	
    AlertDialogManager alert = new AlertDialogManager();  
    // Internet detector
    DatabaseHandler db;
    ConnectionDetector cd;
    HashMap<String, String> lst;
    String KEY_NAME = "name";
    String KEY_EMAIL = "email";
    String lblMessage = "";
	private void initialisePushNotification() {
		// TODO Auto-generated method stub
	    // alert dialog manage
	    
	    cd = new ConnectionDetector(getApplicationContext());
	    
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(LoginActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
 
        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(LoginActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
             return;
        }
        DatabaseHandler db = new DatabaseHandler(getBaseContext());		
		lst = db.getUserDetails();
        //get name and email from database
		 name = lst.get(KEY_NAME);
		 email = lst.get(KEY_EMAIL);
        //

         
        // Check if user filled the form
        if(name.trim().length() > 0 && email.trim().length() > 0){
            // Launch Main Activity

        }else{
            // user doen't filled that data
            // ask him to fill the form
            alert.showAlertDialog(LoginActivity.this, "Registration Error!", "Please enter your details", false);
        }
        
        cd = new ConnectionDetector(getApplicationContext());
        
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(LoginActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
           
         
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
 
//        lblMessage = (TextView) findViewById(R.id.lblMessage);
       
         
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
         
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
 
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM           
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.              
                //Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
        }
	    
	}
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	 private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
	        @SuppressLint("SimpleDateFormat")
			@Override
	        public void onReceive(Context context, Intent intent) {
	            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	            // Waking up mobile if it is sleeping
	            WakeLocker.acquire(getApplicationContext());
	             
	            /**
	             * Take appropriate action on this message
	             * depending upon your app requirement
	             * For now i am just displaying it on the screen
	             * */
	            // Showing received message
	            lblMessage += newMessage + "\n";      
	            db = new DatabaseHandler(getApplicationContext());
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	            SimpleDateFormat dateFormatID = new SimpleDateFormat("yyyyMMddHHmmss");
	            Date date = new Date();
	            String did = dateFormatID.format(date);
	            String datef = dateFormat.format(date);
	            db.addMessage(did,newMessage,datef );
	            
	            
	            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
	            // Releasing wake lock
	            WakeLocker.release();
	        }
	    };
	     
	    @Override
	    protected void onDestroy() {
	        if (mRegisterTask != null) {
	            mRegisterTask.cancel(true);
	        }
	        try {
	            unregisterReceiver(mHandleMessageReceiver);
	            GCMRegistrar.onDestroy(this);
	        } catch (Exception e) {
	            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
	        }
	        super.onDestroy();
	    }

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			//fragment = new CategoriesFragment();
			Intent i = new Intent(LoginActivity.this,ComplaintActivity.class);
			startActivity(i);
			break;
		case 2:
			fragment = new MyComplaintsFragment();

			break;
		case 3:
			fragment = new CategoriesFragment();
			break;
		case 4:
			fragment = new AccountFragment();
			break;
		case 5:
			fragment = new ServerMessages();
			break;
			
		case 6:
			com.example.citydetective.webservice.UserFunctions userFunctions = 
			new com.example.citydetective.webservice.UserFunctions();
			//db.resetTablesPN();
			userFunctions.logoutUser(getApplicationContext());
			Intent login = new Intent(getApplicationContext(),
					com.example.citydetective.MainActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			
			ServerUtilities.unregister(getApplicationContext());
			this.finish();
			
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("LoginActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
