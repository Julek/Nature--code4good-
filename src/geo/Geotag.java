package geo;

public class Geotag {

	private double lattitude, longitude, degrees;
	
	public Geotag(double lattitude, double longitude, double degrees)
	{
		this.lattitude = lattitude;
		this.longitude = longitude;
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
	
	public double getDegrees()
	{
		return degrees;
	}
	
}
