package com.Good.Geo;

public class Geotag {

	private double lattitude, longitude, altitude, bearing;
	
	public Geotag(double lattitude, double longitude, double altitude, double bearing)
	{
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.bearing = bearing;
	}
	
	public double getLattitude()
	{
		return lattitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public double getAltitude()
	{
		return altitude;
	}
	
	public double getBearing()
	{
		return bearing;
	}
	
	public void setLattitude(double lattitude)
	{
		this.lattitude = lattitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setAltitude(double altitude)
	{
		this.altitude = altitude;
	}
	
	public void setBearing(double bearing)
	{
		this.bearing = bearing;
	}
}
