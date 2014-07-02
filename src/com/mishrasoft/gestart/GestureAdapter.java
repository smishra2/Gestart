package com.mishrasoft.gestart;

import java.io.File;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GestureAdapter extends BaseAdapter{
	
	private Context context;
	private GestureLibrary gesturelib;
	private String[] listofgestures =  new String[]{"No gestures found"};
	private String color;
	
	public GestureAdapter(Context context, String filesdir, String color) {
		this.color = color;
		this.context = context;
		File gesfile = new File(filesdir + File.separator + "GestureLibrary");
		this.gesturelib = GestureLibraries.fromFile(gesfile);
		this.gesturelib.load();
		if (this.gesturelib.getGestureEntries().size() > 0)
		{
			this.listofgestures = this.gesturelib.getGestureEntries().toArray(new String[this.gesturelib.getGestureEntries().size()]);
			java.util.Arrays.sort(this.listofgestures);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count=0;
		count = this.listofgestures.length;
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.listofgestures[position];
		//return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public String getItemName(int position)
	{
		//return "";
		return (String) this.listofgestures[position];
	}
	
	public String getAppName(int position)
	{
		String appname="";
		final PackageManager pm = context.getPackageManager();
		ApplicationInfo ai;
		try {
		    ai = pm.getApplicationInfo(this.listofgestures[position], 0);
		} catch (final NameNotFoundException e) {
		    ai = null;
		}
		appname = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
		return appname;
	}
	
	public String getAppNameFromPackageName(String packagename)
	{
		String appname="";
		final PackageManager pm = context.getPackageManager();
		ApplicationInfo ai;
		try {
		    ai = pm.getApplicationInfo(packagename, 0);
		} catch (final NameNotFoundException e) {
		    ai = null;
		}
		appname = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
		return appname;
	}
	
	public Bitmap getGesturePic(String gestureName)
	{
		//this.gesturelib.getGestures(gestureName).get(0).t
		return (this.gesturelib.getGestures(gestureName).get(0).toBitmap(75, 75, 10, Color.parseColor(this.color)));
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View gridView;
		 
		//if (convertView == null) {
 
			gridView = new View(context);
			//reverse the position for the listview
			//int position = this.listofgestures.length - pos - 1;
 
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.eachgesturelayout, null);
			
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.gestureimage);
				imageView.setImageResource(R.drawable.ic_launcher);
			
			TextView textView = (TextView) gridView
					.findViewById(R.id.gesturelabel);
				textView.setText(this.listofgestures[position].toString());
 
			if (this.gesturelib.getGestureEntries().size()!=0)
			{

				imageView.setImageBitmap(this.getGesturePic(this.listofgestures[position]));;
				//textView.setText((String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
				textView.setText(this.getAppName(position));
				
			}
			
			 
		//} else {
		//	gridView = (View) convertView;

		//}
 
		return gridView;
	}



}

