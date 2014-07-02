package com.mishrasoft.gestart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	int accuracy = 2;
	String gesturecolor = "#3F48CC";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.settings);
		
		
		//get accuracy from settings
		SharedPreferences prefs = this.getSharedPreferences("GestartSettings", 0);
		accuracy = prefs.getInt("ReqAccuracy", 2);
		
		//set accuracy bar
		final SeekBar accuracybar = (SeekBar) findViewById(R.id.accuracyBar);
		accuracybar.setProgress(accuracy);
		accuracybar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				accuracy = progress;
				//Toast.makeText(getApplicationContext(), Integer.toString(progress), Toast.LENGTH_LONG).show();
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//set default accuracy and disable sensitivity bar when checkbox is checked
		final CheckBox check = (CheckBox) findViewById(R.id.checkBox1);
		
		if (prefs.getInt("ReqAccuracy", 2) == 6)
		{
			accuracybar.setEnabled(false);
			check.setChecked(true);
		}
		check.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (check.isChecked())
				{
					accuracybar.setEnabled(false);
					accuracy = 6; //where 6 will be translated to 2.5 later
				}
				else if (!check.isChecked())
				{
					accuracybar.setEnabled(true);
					accuracy = accuracybar.getProgress();
				}
			}
			
		});
		
		//set up the color drop down
		final Spinner colorspinner = (Spinner) findViewById(R.id.colorspinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.colors_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		colorspinner.setAdapter(adapter);
		//set to currently used color
		for (int i =0; i<10;i++)
		{
			if (colorspinner.getItemAtPosition(i).equals(prefs.getString("GestureColor", "Gestart Blue")))
				colorspinner.setSelection(i);
		}
		
		
		colorspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				//Toast.makeText(getApplicationContext(), colorspinner.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG);
				if (!colorspinner.getItemAtPosition(pos).toString().equals("Gestart Blue"))
					gesturecolor=colorspinner.getItemAtPosition(pos).toString();
				else
					gesturecolor="#3F48CC";
				
			}
     		public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

	    });
		
		//save button
		Button save = (Button) findViewById(R.id.Savesettings);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
			      // We need an Editor object to make preference changes.
			      // All objects are from android.context.Context
			      SharedPreferences settings = getSharedPreferences("GestartSettings", 0);
			      SharedPreferences.Editor editor = settings.edit();
			      editor.putInt("ReqAccuracy", accuracy);
			      editor.putString("GestureColor", gesturecolor);

			      // Commit the edits!
			      editor.commit();
			      Intent i = new Intent(SettingsActivity.this,
							ListGesturesActivity.class);
			      startActivity(i);
			}
		});
		
		Button cancel = (Button) findViewById(R.id.Cancelsettings);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				   finish();
	               Intent i = new Intent(SettingsActivity.this, ListGesturesActivity.class);
	               startActivity(i);
			}
		});
	}
}
