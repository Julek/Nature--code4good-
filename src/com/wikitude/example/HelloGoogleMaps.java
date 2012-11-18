package com.wikitude.example;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	    HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
	    
	    GeoPoint point = new GeoPoint(19240000,-99120000);
	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	}
	

	protected void onDestroy(){
		super.onDestroy();
	}
}
