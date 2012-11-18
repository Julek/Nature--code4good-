package com.wikitude.example;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.Good.GeoNode;
import com.Good.ServerCommunication;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class HelloGoogleMaps extends MapActivity{

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_maps);
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    GeoNode node = new GeoNode("","","",0,0,0);
	    ServerCommunication scomm = new ServerCommunication(node, ServerCommunication.CommunicationType.POST);
	    List<GeoNode> geoList = null;
	    try {
			geoList = scomm.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    HelloItemizedOverlay itemizedoverlay = null;
	    for (GeoNode gNode : geoList){
	    	String type = gNode.tagType;
	    	Drawable drawable = this.getResources().getDrawable(getDrawable(type));
		    itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		    
		    GeoPoint point = new GeoPoint((int) (gNode.latitude * Math.pow(10, 6)), (int) (gNode.longitude * Math.pow(10, 6)));
		    OverlayItem overlayitem = new OverlayItem(point, gNode.tagName, gNode.tagDescr);
		    
		    itemizedoverlay.addOverlay(overlayitem);
		
	    }
	    mapOverlays.add(itemizedoverlay);
	}
	

	private int getDrawable(String type) {
		if (type.toLowerCase().compareTo("river") == 0){
			return R.drawable.river;
		} else if (type.toLowerCase().compareTo("caves") == 0) {
			return R.drawable.cave;
		} else if (type.toLowerCase().compareTo("tree") == 0) {
			return R.drawable.tree;
		} else if (type.toLowerCase().compareTo("rock") == 0) {
			return R.drawable.rock;
		} else if (type.toLowerCase().compareTo("scenary") == 0) {
			return R.drawable.scenary;
		} else {
			return R.drawable.flower;
		}
		
	}

	protected void onDestroy(){
		super.onDestroy();
	}
}
