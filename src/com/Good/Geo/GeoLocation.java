package com.Good.Geo;

import java.util.concurrent.locks.*;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.*;
import android.os.Bundle;

import com.Good.Geo.Geotag;
import com.wikitude.example.*;

public class GeoLocation {

	static private Geotag tag = null;
	static private LocationManager locationManager;
	static private Sensor magnet;
	static private LocListener listener = new LocListener();
	private static Lock lock = new ReentrantLock();
	static float degrees = 0;
	
	public static void setup_GeoLocation() throws NoBearing
	{
		locationManager = (LocationManager) MainActivity.curr.getSystemService(Context.LOCATION_SERVICE);
		magnet = MainActivity.sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		MainActivity.sensors.registerListener(new MagnetSensor(), magnet, SensorManager.SENSOR_DELAY_NORMAL);
		if(!locationManager.getProvider(LocationManager.GPS_PROVIDER).supportsBearing())
			throw new NoBearing();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
	}

	
	public static Geotag getGeotag() throws NoClue
	{
		lock.lock();
		Geotag ret = tag;
		lock.unlock();
		
		if(ret == null)
			throw new NoClue();
		
		return ret;
	}
	
	public static double getBearing()
	{
		return degrees;
	}
	
	private static class LocListener implements LocationListener
	{

		public void onLocationChanged(Location location) {
			lock.lock();
			if(tag == null)
			{
				tag = new Geotag(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getBearing());
			}
			else
			{	
				tag.setLattitude(location.getLatitude());
				tag.setLongitude(location.getLongitude());
				tag.setAltitude(location.getAltitude());
				tag.setBearing(location.getBearing());
			}
			lock.unlock();
			return;
		}

		public void onProviderDisabled(String arg0) {
			if(arg0 == LocationManager.GPS_PROVIDER)
			{
				lock.lock();
				tag = null;
				lock.unlock();
			}
			return;
		}

		public void onProviderEnabled(String arg0) {
			return;
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			return;
		}
		
	}
	
	private static class MagnetSensor implements SensorEventListener
	{
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}

		public void onSensorChanged(SensorEvent event) {
			float x = event.values[0];
			float z = event.values[2];
			degrees = (float) Math.acos(z/Math.sqrt(x*x + z*z));
		}
	}
	
	public static class NoClue extends Exception
	{
		private static final long serialVersionUID = 773107550741108174L;
	}
	
	public static class NoBearing extends Exception
	{
		private static final long serialVersionUID = -754690391962814625L;
	}
	
}
