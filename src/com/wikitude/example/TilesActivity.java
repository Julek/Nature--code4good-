package com.wikitude.example;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class TilesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tiles);
		
		findViewById(R.id.my_nature).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						load_my_nature();
					}
				});
		

		findViewById(R.id.their_nature).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						load_their_nature();
					}
				});
		

		findViewById(R.id.augumented_reality).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						load_aug_reality();
					}
				});
		
	}

	
	
	protected void load_aug_reality() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        onDestroy();
	}



	protected void load_their_nature() {
		// TODO Auto-generated method stub
	}



	protected void load_my_nature() {
		// TODO Auto-generated method stub
		
	}


	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tiles, menu);
		return true;
	}

}
