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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class AddGestureActivity extends Activity implements OnGesturePerformedListener {
	
    public String apptostart="App to start...";
       

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.addgesture);
              
//		// Create the adView
//        AdView adView = (AdView) findViewById(R.id.adView2);
//        // Initiate a generic request to load it with an ad
//        adView.loadAd(new AdRequest());
        
        //List<ApplicationInfo> packages = getPackageManager()
        //       .getInstalledApplications(PackageManager.GET_META_DATA);
        
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        List<ResolveInfo> resolveinfos = this.getPackageManager().queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        
        final List<String> apps = new ArrayList<String>();
        final List<String> packages = new ArrayList<String>();
        
        for (ResolveInfo rInfo : resolveinfos) {
        	   apps.add(rInfo.activityInfo.applicationInfo.loadLabel(this.getPackageManager()).toString());
        	   packages.add(rInfo.activityInfo.applicationInfo.packageName.toString());
        } 
        
        final String[] listofapps = apps.toArray(new String[apps.size()]);
        final String[] listofpackages = packages.toArray(new String[packages.size()]);
//        java.util.Arrays.sort(listofapps);
        
//        //find out all non system apps and get all package names
//        int nonsystemapps=0;
//        //for (int i = 0; i < packages.size(); i++)
//        for (int i = 0; i < listofapps.length; i++)
//        {
//        	//if ((packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM) != 1)
//              	nonsystemapps++;
//        	
//        }
//        final String[] apparray = new String[nonsystemapps];
//        final String[] packagearray = new String[nonsystemapps];
//        //for (int i = 0; i < packages.size(); i++)
//        for (int i = 0; i < listofapps.length; i++)
//        {
//        	//if ((packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM) != 1)
//        	//{
//        		for (int x = 0; x < nonsystemapps; x++)
//        		{
//        			if (apparray[x] == null)
//        			{
//        				//apparray[x]=packages.get(i).loadLabel(getPackageManager()).toString();
//        				apparray[x]=listofapps[i];
//        				//packagearray[x]=packages.get(i).packageName;
//        				packagearray[x]=listofpackages[i];
//        				x = nonsystemapps;
//        			}
//        		}
//        	//}
//        }
//        final String[] temparray = apparray.clone();
//        java.util.Arrays.sort(temparray);
        
        final Button appchooser = (Button) findViewById(R.id.button5);
        appchooser.setText(getIntent().getExtras().getString("APPTOSTART"));
      
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Which app would you like this gesture to start?");
        //final String[] listarray = new String[listofapps.length];
        final String[] listarray=listofapps;
        java.util.Arrays.sort(listarray);
        builder.setSingleChoiceItems(listarray, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	//apptostart = listofapps[item];
                dialog.cancel();
                appchooser.setText(listarray[item]);
            }
        });
        final AlertDialog appchooserdialog = builder.create();
               
        appchooser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               appchooserdialog.show();
           } 
         });
        
        GestureOverlayView gestureoverlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		SharedPreferences prefs = this.getSharedPreferences("GestartSettings", 0);
		gestureoverlay.setGestureColor(Color.parseColor(prefs.getString("GestureColor", "#3F48CC")));
		gestureoverlay.setUncertainGestureColor(Color.parseColor(prefs.getString("GestureColor", "#3F48CC")));
		gestureoverlay.addOnGesturePerformedListener(this);
		final GestureLibrary gesturelib = GestureLibraries.fromFile(this.getFilesDir() + File.separator + "GestureLibrary");

        Button cancel = (Button) findViewById(R.id.button4);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               finish();
               Intent i = new Intent(AddGestureActivity.this, ListGesturesActivity.class);
               startActivity(i);
            } 
         });
        
        final AlertDialog nogesturealert = new AlertDialog.Builder(this).create();
        nogesturealert.setTitle("No Gesture");
        nogesturealert.setMessage("Please draw a gesture...");
        nogesturealert.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
       
             //here you can add functions
       
          } });
        
        final AlertDialog noappalert = new AlertDialog.Builder(this).create();
        noappalert.setTitle("No App Chosen");
        noappalert.setMessage("Please choose an app to start when this gesture is drawn...");
        noappalert.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
       
             //here you can add functions
       
          } });
       
        
        Button save= (Button) findViewById(R.id.button3);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	GestureOverlayView gestureoverlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
                if (!(gestureoverlay.getGesture()==null) && !appchooser.getText().equals("App to start..."))
            	{
                	apptostart = appchooser.getText().toString();
                	String packagetostart="";
//                	for (int i=0; i < apparray.length; i++)
//                	{
//                		if (apptostart.equals(apparray[i]))
//                		{
//                			packagetostart=packagearray[i];
//                			i=apparray.length;	
//            			
//                		}
//                	}
                	//for (int i=0; i < listofapps.length; i++)
                	for (int i=0; i < apps.size(); i++)
                	{
                		//if (apptostart.equals(listofapps[i]))
                		if (apptostart.equals(apps.get(i)))
                		{
                			//packagetostart=listofpackages[i];
                			packagetostart = packages.get(i);
                			i=listofapps.length;	
            			}
                	}
                	gesturelib.load();
                	gesturelib.removeEntry(packagetostart);
                	gesturelib.addGesture(packagetostart, gestureoverlay.getGesture());
                	gesturelib.save();
                	finish();
                	Intent i = new Intent(AddGestureActivity.this, ListGesturesActivity.class);
                	startActivity(i);
            	}
            	else if (appchooser.getText().equals("App to start..."))
            	{
            		noappalert.show();
            	}
            	else if (gestureoverlay.getGesture()==null)
            	{
            		nogesturealert.show();
            	}
            		
            } 
         });
        
        
    }


	@Override
	public void onGesturePerformed(GestureOverlayView arg0, Gesture arg1) {
		// TODO Auto-generated method stub
	}



}