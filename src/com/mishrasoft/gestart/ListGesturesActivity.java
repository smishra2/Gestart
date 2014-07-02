package com.mishrasoft.gestart;

import java.io.File;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class ListGesturesActivity extends Activity {
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);

//		// Create the adView
//		AdView adView = (AdView) findViewById(R.id.adView);
//		// Initiate a generic request to load it with an ad
//		adView.loadAd(new AdRequest());
		
		//create preferences where there are none
		SharedPreferences prefs = this.getSharedPreferences("GestartSettings", 0);
		if (!prefs.contains("GestureColor"))
		{
		      // We need an Editor object to make preference changes.
		      SharedPreferences.Editor editor = prefs.edit();
		      editor.putString("GestureColor", "#3F48CC");
		      // Commit the edits!
		      editor.commit();
		}
		if (!prefs.contains("ReqAccuracy"))
		{
		      // We need an Editor object to make preference changes.
		      SharedPreferences.Editor editor = prefs.edit();
		      editor.putInt("ReqAccuracy", 2);
		      // Commit the edits!
		      editor.commit();
		}
		
		//about button
		ImageView aboutbutton = (ImageView) findViewById(R.id.imageView1);
		final AlertDialog alertDialog =new AlertDialog.Builder(this).create();
        aboutbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alertDialog.setTitle("About");
				alertDialog.setMessage("Welcome to Gestart\u00a9\n\nCreate gestures with the button below and edit existing gestures by holding down a specific gesture. Add the widget to your home screen and click it to open a screen from which you can draw these gestures to start the corresponding app.\n\nAs a tip, if drawing a gesture yields too many results, try increasing the required accuracy in Settings.");
				Message listener = null;
				alertDialog.setButton("OK", listener);
				alertDialog.show();
			}
		});	

		Button addgesture = (Button) findViewById(R.id.button1);
		addgesture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
				Intent i = new Intent(ListGesturesActivity.this,
						AddGestureActivity.class);
				Bundle paramets = new Bundle();
				paramets.putString("APPTOSTART","App to start...");
				i.putExtras(paramets);
				startActivity(i);
			}
		});
		
		//settings
		Button settings = (Button) findViewById(R.id.settingsbutton);
		settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
				Intent i = new Intent(ListGesturesActivity.this,
						SettingsActivity.class);
				startActivity(i);
				
				
			}
		});
		
		// set the listview
		final ListView gesturelist = (ListView) findViewById(R.id.listView1);
		GestureAdapter adapter = new GestureAdapter(this, this.getFilesDir().toString(), prefs.getString("GestureColor", "#3F48CC"));	
		gesturelist.setAdapter(adapter);
		
		registerForContextMenu(gesturelist);


	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.listView1) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle("Choose an option");
	      menu.add(Menu.NONE, 0, 0, "Change Gesture");
	      menu.add(Menu.NONE, 1, 1, "Delete");
	      menu.add(Menu.NONE, 2, 2, "Cancel");
	  }
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  
	  //change gesture option 
	  if (item.getItemId() == 0)
	  {
			
			Intent i = new Intent(ListGesturesActivity.this, AddGestureActivity.class);
			Bundle paramets = new Bundle();
			paramets.putString("APPTOSTART",(new GestureAdapter(this, this.getFilesDir().toString(), "#3F48CC")).getAppName(info.position));
			i.putExtras(paramets);
			finish();
			startActivity(i);
	  }
	  //delete option
	  else if (item.getItemId() == 1)
	  {
		  GestureLibrary gesturelib = GestureLibraries.fromFile(this.getFilesDir() + File.separator + "GestureLibrary");
		  gesturelib.load();
		  gesturelib.removeEntry((new GestureAdapter(this, this.getFilesDir().toString(), "#3F48CC")).getItemName(info.position));
		  gesturelib.save();
		  //this.recreate();
		  SharedPreferences prefs = this.getSharedPreferences("GestartSettings", 0);
		  GestureAdapter adapter = new GestureAdapter(this, this.getFilesDir().toString(), prefs.getString("GestureColor", "#3F48CC"));	
		  ListView gesturelist = (ListView) findViewById(R.id.listView1);
		  gesturelist.setAdapter(adapter);
	  }
	  //cancel option
	  else if (item.getItemId() == 2)
	  {
		  
	  }

	  return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(startMain);
		
	}
}