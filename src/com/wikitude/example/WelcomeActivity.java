package com.wikitude.example;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class WelcomeActivity extends Activity {

	Bundle savedstates;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedstates = savedInstanceState;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		long delay = 3000;
		Timer welcomeTimer = new Timer();
		welcomeTimer.schedule(new WelcomeTimerTask(), delay);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}

    protected void onDestroy() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    	super.onDestroy();
        
    	
    }
    
    public class WelcomeTimerTask extends TimerTask{

    	public WelcomeTimerTask(){
    		super();
    	}
    	
		@Override
		public void run() {
			onDestroy();
		}
    	
    }
	
}
