package com.Good.Geo;

public class Geotag {

	private double lattitude, longitude, altitude, degrees;
	
	public Geotag(double lattitude, double longitude, double altitude, double degrees)
	{
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.degrees = degrees;
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
	
	public double getDegrees()
	{
		return degrees;
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
	
	public void setDegrees(double degrees)
	{
		this.degrees = degrees;
	}
}
