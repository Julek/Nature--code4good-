package com.Good;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

import com.Good.Geo.GeoLocation;
import com.Good.Geo.GeoLocation.NoBearing;

public class MainActivity extends Activity {

	public static Context curr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
			GeoLocation.setup_GeoLocation();
		}
		catch(NoBearing e){
			AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.curr).create();

			alertDialog.setTitle("Error");
			alertDialog.setMessage("This application cannot function on a device without an internal compass");
			alertDialog.setIcon(R.drawable.ic_launcher);

			alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});

			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
