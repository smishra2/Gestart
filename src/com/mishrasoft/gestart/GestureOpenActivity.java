package com.mishrasoft.gestart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GestureOpenActivity extends Activity implements OnGesturePerformedListener{

	private GestureLibrary gesturelib;
	private GestureAdapter adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.openinglayout);

		//set the sensor
		GestureOverlayView gesturesensor = (GestureOverlayView) findViewById(R.id.gestures_sensor);
		SharedPreferences prefs = this.getSharedPreferences("GestartSettings", 0);
		gesturesensor.setGestureColor(Color.parseColor(prefs.getString("GestureColor", "#3F48CC")));
		gesturesensor.setUncertainGestureColor(Color.parseColor(prefs.getString("GestureColor", "#3F48CC")));
		
		gesturelib = GestureLibraries.fromFile(this.getFilesDir() + File.separator + "GestureLibrary");
		//if (!gesturelib.load())
		//	finish();
		gesturesensor.addOnGesturePerformedListener(this);
		
//		// Create the adView
//		AdView adView = (AdView) findViewById(R.id.adView3);
//		// Initiate a generic request to load it with an ad
//		adView.loadAd(new AdRequest());

		// lets list the existing gestures
		LinearLayout scroller = (LinearLayout) findViewById(R.id.linearLayout4);
		adapter = new GestureAdapter(this, this.getFilesDir().toString(), prefs.getString("GestureColor", "#3F48CC"));
		if (!adapter.getItemName(0).equals("No gestures found")) {
			for (int i = 0; i < adapter.getCount(); i++) {
				// add the gesture bitmap
				ImageView gesturebitmap = new ImageView(this);
				gesturebitmap.setImageBitmap(adapter.getGesturePic(adapter.getItemName(i)));
				//gesturebitmap.setTop(1);
				//gesturebitmap.setLeft(1);
				//gesturebitmap.setBottom(1);
				//gesturebitmap.setRight(1);
				gesturebitmap.setPadding(5, 5, 5, 5);
				scroller.addView(gesturebitmap);

				// add the gesture name
				TextView gesturename = new TextView(this);
				gesturename.setText(adapter.getAppName(i));
				gesturename.setPadding(7, 25, 7, 0);
				scroller.addView(gesturename);

				// add the separator
				ImageView separator = new ImageView(this);
				separator.setImageResource(R.drawable.gesturelistseparator);
				separator.setPadding(7, 0, 7, 0);
				scroller.addView(separator);
			}
		} else {
			// add the gesture bitmap
			ImageView gesturebitmap = new ImageView(this);
			gesturebitmap.setImageResource(R.drawable.ic_launcher);
			//gesturebitmap.setTop(1);
			//gesturebitmap.setLeft(1);
			//gesturebitmap.setBottom(1);
			//gesturebitmap.setRight(1);
			gesturebitmap.setPadding(5, 5, 5, 5);
			scroller.addView(gesturebitmap);

			// add the gesture name
			TextView gesturename = new TextView(this);
			gesturename.setText("No gestures found");
			gesturename.setPadding(7, 25, 7, 0);
			scroller.addView(gesturename);
		}
		

		// link to gesturelistactivity
		TextView morebutton = (TextView) findViewById(R.id.morebutton);
		morebutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
				Intent i = new Intent(GestureOpenActivity.this,
						ListGesturesActivity.class);
				startActivity(i);
			}
		});
	}

	// backbutton go back to homescreen
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(startMain);

	}
	
 
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		// TODO Auto-generated method stub
		gesturelib.load();
		ArrayList<Prediction> predictions = gesturelib.recognize(gesture);
		//pull up the accuracy settings
		SharedPreferences prefs = this.getSharedPreferences("GestartSettings", 0);
		Double accuracy = (double) prefs.getInt("ReqAccuracy", 2);
		if (accuracy.equals(6.0))
			accuracy=2.5; //translate to default accuracy
		//get the possible predictions
		List<String> apps = new ArrayList<String>();
		List<String> apppackages = new ArrayList<String>();
		for (Prediction prediction : predictions) {
			if (prediction.score > accuracy) {
				apps.add(adapter.getAppNameFromPackageName(prediction.name));
				apppackages.add(prediction.name);
			}
		}
		
		//if there's more than one prediction, open a list to choose from
		final CharSequence[] items = apps
				.toArray(new CharSequence[apps.size()]);
		final CharSequence[] itempackages = apppackages
				.toArray(new CharSequence[apppackages.size()]);
		if (apps.size() > 1) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Which app would you like to start?");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Intent i = new Intent(Intent.ACTION_MAIN);
					PackageManager manager = getPackageManager();
					i = manager.getLaunchIntentForPackage(itempackages[item].toString());
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					// this.finish();
					Toast.makeText(getApplicationContext(), "Starting " + items[item] + "...", Toast.LENGTH_SHORT).show();
					// this.finish();
					startActivity(i);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		
		else if (apps.size() ==1)
		{
			Intent i = new Intent(Intent.ACTION_MAIN);
			PackageManager manager = getPackageManager();
			i = manager.getLaunchIntentForPackage(itempackages[0].toString());
			i.addCategory(Intent.CATEGORY_LAUNCHER);
			// this.finish();
			Toast.makeText(getApplicationContext(),
					"Starting " + items[0] + "...",Toast.LENGTH_SHORT).show();
			// this.finish();
			startActivity(i);
		}
		
		else
		{
			Toast.makeText(getApplicationContext(),"Gesture unrecognized...",Toast.LENGTH_SHORT).show();
		}
	}
}