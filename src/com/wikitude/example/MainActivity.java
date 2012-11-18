package com.wikitude.example;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.Good.GeoNode;
import com.Good.ServerCommunication;
import com.Good.Geo.GeoLocation;
import com.Good.Geo.GeoLocation.NoBearing;
import com.wikitude.architect.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView;

/**
 * 
 * @author Wikitude
 * @date   JAN 2012
 * 
 * @class SimpleARBrowserActivity
 * 
 *	sample application to show how to use the ARchitect SDK
 * 	loads simple pois via javascript into the ARchitect world and displays them accordingly
 *  displays a bubble with information about the selected poi on the screen and displays a detail page when the bubble is clicked
 *  uses Android's LocationManager to get updates on the user's location
 * 
 *  important is that the methods of the activity lifecycle are forwarded to the ArchitectView
 *  Important methods:  	onPostCreate()
 * 							onResume()
 *							onPause()
 * 							onDestroy()
 * 							onLowMemory()	
 * 
 * 	Please also have a look at the application's Manifest and layout xml-file to see the permissions and requirements 
 * 	an activity using the SDK has to possess. (REF: ARchitect Documentation)		  	  
 */
public class MainActivity extends Activity implements ArchitectUrlListener, LocationListener{
	
	
	public static Context curr;
	
	public static SensorManager sensors;
	private static final String TAG = MainActivity.class.getSimpleName();

	
	private final static float  TEST_LATITUDE =  51.5025f;
	private final static float  TEST_LONGITUDE = -0.021f;
	private final static float 	TEST_ALTITUDE = 150;
	
	private String apiKey = "n+DtduXJkBa4hwW4Yhfhl6VjAbR0s8Bu+cLAvUYkENtRNfOIL96dDpAK1saHrVCG8D2IR2elw/AZda7r+Z9Gi9OhV/p+4qrNDctU0FRJipzBmGAC7A3Ro74mTk3uvPBv4RKF62H1e5bQbBpw669Jm+1ML9i1aEa9XBTtVrKtaNxTYWx0ZWRfXz3NUfI/Oou3sPI6XQQqnn8jxfaY39n7P/WT3wUj6AHLQa44pS5bVkk+YIUYiu5lrn2DFtG6wNQPk1KgOngpWihJH4IH3xstZl/CJHd6xPI279toJrakn5FWdL3LtDObTtWFI5qCuJttCRiWiZ/hd1lLx7BYyDTxhXCotN+ph5keUquN/cKNQjSJ/AnlvBcDV7NMmqBmBFzi2wJhte1WHnr80OjAw1oBPVT2+uUSCJxX5UyHygGx9qbvFgFVHclrXdalGqOwqQNauKiZF5QslSMfMYgFdWOvQgjDN1RbfTkUaaHJrW36nz2pz2JH2rVlQNN6P6EZZcOViF7H0L4MMQtm3+EqNE/4QEcW/Ir5e6hOzEeXZUx9LlRe8tIoxf50HhR8RfHKmjY0D9bDtVEDQyGD7NjPVJL+fddoEvTlrP5O5TaUSYC3BEd8uXTMxpUFVMfaEezbRQ/lcAF96gSmbkY1DHwgExsqiHs81Czbmfu+GOj6S2mnVDxnBsUF9ZXhg6+GM+0Uqfyk";
	
	
	private ArchitectView architectView;
//	private LocationManager locManager;
//	private Location loc;
	private List<PoiBean> poiBeanList;
	
	//server communication
	private static ServerCommunication scomm;
	Timer t;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
		curr = this;
		sensors = (SensorManager) curr.getSystemService(SENSOR_SERVICE);
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
    	
        //let the application be fullscreen
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
    	
        
        //check if the device fulfills the SDK'S minimum requirements
        if(!ArchitectView.isDeviceSupported(this))
        {
        	Toast.makeText(this, "minimum requirements not fulfilled", Toast.LENGTH_LONG).show();
        	this.finish();
        	return;
        }
        setContentView(R.layout.main);
       
        //set the devices' volume control to music to be able to change the volume of possible soundfiles to play
        this.setVolumeControlStream( AudioManager.STREAM_MUSIC );
        this.architectView = (ArchitectView) this.findViewById(R.id.architectView);
        //onCreate method for setting the license key for the SDK
        architectView.onCreate(apiKey);
        LinearLayout ll = (LinearLayout) findViewById(R.id.formLayout);
    	ll.setVisibility(View.INVISIBLE);
    	
    	GeoNode node = new GeoNode("","","",TEST_LONGITUDE,TEST_LATITUDE,TEST_ALTITUDE);
    	scomm = new ServerCommunication(node, ServerCommunication.CommunicationType.POST);
        //in order to inform the ARchitect framework about the user's location Androids LocationManager is used in this case
        //NOT USED IN THIS EXAMPLE
        //locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);

        final Button tagBtn = (Button) findViewById(R.id.tagBtn);
        tagBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String btnLabel = (String) tagBtn.getText();
            	if (btnLabel.compareTo("Tag") == 0){
         //   		callJavaScript("getInfoBox();");
                	tagBtn.setText("Submit Tag");
                	LinearLayout ll = (LinearLayout) findViewById(R.id.formLayout);
                	ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,300));
                	ll.setVisibility(View.VISIBLE);
            	} else {
            		double lat = TEST_LATITUDE + ((Math.random() - 0.5) / 500); 
            		double lon = TEST_LONGITUDE + ((Math.random() - 0.5) / 500); 
            		double alt = TEST_ALTITUDE + ((Math.random() - 0.5) * 10);
           // 		callJavaScript("submitTag("+lat+","+lon+","+alt+");");
            		String type = ((Spinner) findViewById(R.id.SpinnerType)).getSelectedItem().toString();
            		String description = ((EditText) findViewById(R.id.PlantDescription)).getText().toString();
            		String name = ((EditText) findViewById(R.id.PlantName)).getText().toString();
            		GeoNode node = new GeoNode(name,description,type,lon,lat,alt);
            		scomm = new ServerCommunication(node, ServerCommunication.CommunicationType.POST);
            		scomm.execute();
            		Toast.makeText(getApplicationContext(), "Tag submitted successfully", Toast.LENGTH_LONG).show();
            		tagBtn.setText("Tag");
                	LinearLayout ll = (LinearLayout) findViewById(R.id.formLayout);
                	ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0));
                	ll.setVisibility(View.INVISIBLE);
            	}
            }
        });

     }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	
    	//IMPORTANT: creates ARchitect core modules
    	if(this.architectView != null)
    		this.architectView.onPostCreate();
    	
    	//register this activity as handler of "architectsdk://" urls
    	this.architectView.registerUrlListener(this);
    	
    	t = new Timer();
		t.schedule(new LoadSample(),0,5000);

    }
    
	@Override
	protected void onResume() {
		super.onResume();

		this.architectView.onResume();
		this.architectView.setLocation(TEST_LATITUDE, TEST_LONGITUDE, TEST_ALTITUDE,1f);

	}
    @Override
    protected void onPause() {
    	super.onPause();
    	t.cancel();
    	if(this.architectView != null)
    		this.architectView.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	t.cancel();
    	if(this.architectView != null)
    		this.architectView.onDestroy();
    }
    
    @Override
    public void onLowMemory() {
    	super.onLowMemory();
    	
    	if(this.architectView != null)
    		this.architectView.onLowMemory();
    }

    /**
     * <p>
     * interface method of {@link ArchitectUrlListener} class
     * called when an url with host "architectsdk://" is discovered
     * 
     * can be parsed and allows to react to events triggered in the ARchitect World
     * </p>
     */
	@Override
	public boolean urlWasInvoked(String url) {
		//parsing the retrieved url string
		List<NameValuePair> queryParams = URLEncodedUtils.parse(URI.create(url), "UTF-8");
		
		String id = "";
		// getting the values of the contained GET-parameters
		for(NameValuePair pair : queryParams)
		{
			if(pair.getName().equals("id"))
			{
				id = pair.getValue();
			}
		}
		
		//get the corresponding poi bean for the given id
		PoiBean bean = poiBeanList.get(Integer.parseInt(id));
		//start a new intent for displaying the content of the bean
		Intent intent = new Intent(this, PoiDetailActivity.class);
		intent.putExtra("POI_NAME", bean.getName());
		intent.putExtra("POI_DESC", bean.getDescription());
		this.startActivity(intent);
		return true;
	}
	
	/**
	 * method for creating random locations in the vicinity of the user
	 * @return array with lat and lon values as doubles
	 */
	private double[] createRandLocation() {
		 
		return new double[]{ TEST_LATITUDE + ((Math.random() - 0.5) / 500), TEST_LONGITUDE + ((Math.random() - 0.5) / 500),  TEST_ALTITUDE + ((Math.random() - 0.5) * 10)};
	}

	/**
	 * loads a sample architect world and
	 * creates a definable amount of pois in beancontainers 
	 * and converts them into a jsonstring that can be sent to the framework
	 * @throws IOException exception thrown while loading an Architect world
	 */
	private class LoadSample extends TimerTask{ 
	public void run() {
		try {
			MainActivity.this.architectView.load("tutorial1.html");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		MainActivity.this.architectView.callJavascript("remData();");
		JSONArray array = new JSONArray();
		poiBeanList = new ArrayList<PoiBean>();
		try {
				try {

					scomm = new ServerCommunication(new GeoNode("","","",TEST_LONGITUDE,TEST_LATITUDE,TEST_ALTITUDE), ServerCommunication.CommunicationType.GET);		
					List<GeoNode> lst = scomm.execute().get();
				
					for(int i=0;i<lst.size();i++) {
						double[] location = new double[3];
						location[0] = lst.get(i).latitude;
						location[1] = lst.get(i).longitude;
						location[2] = lst.get(i).altitude;
						PoiBean bean = new PoiBean(
								""+i,
								lst.get(i).tagName,
								lst.get(i).tagDescr,
								lst.get(i).tagType, location[0], location[1], location[2]);
						array.put(bean.toJSONObject());
						poiBeanList.add(bean);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}	
		MainActivity.this.architectView.callJavascript("newData(" + array.toString() + ");");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	}

	/**
	 * listener method called when the location of the user has changed
	 * used for informing the ArchitectView about a new location of the user
	 */
	@Override
	public void onLocationChanged(Location loc) {
		// IMPORTANT: 
		// use this method for informing the SDK about a location change by the user
		// for simplicity not used in this example
		
		//inform ArchitectView about location changes
		//if(this.architectView != null)
		//	this.architectView.setLocation((float)(loc.getLatitude()), (float)(loc.getLongitude()), loc.getAccuracy());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void callJavaScript(String function){
		this.architectView.callJavascript(function);
	}
	
	
}
