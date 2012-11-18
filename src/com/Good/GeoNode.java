package com.Good;

public class GeoNode {

	public double longitude;
	public double latitude;
	public double altitude;
	
	public String tagName;
	public String tagDescr;
	public String tagType;
	
	public GeoNode() {
		//
	}
	
	public GeoNode(String tagName, String tagDescr, String tagType, double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.tagDescr = tagDescr;
		this.tagName = tagName;
		this.tagType = tagType;
	}
	
	
	public double getDistance(double lon,double lat) {
		double lat2 = lat;
		double lat1 = latitude;
		double lon2 = lon;
		double lon1 = longitude;
	
		int R = 6371; // km
		double dLat = (lat2-lat1) * 0.0174532925;
		double dLon = (lon2-lon1) * 0.0174532925;
		lat1 = lat1 * 0.0174532925;
		lat2 = lat2 * 0.0174532925; //to radians

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c;
		return d;
	}
	
	public String toString(){
		return this.tagType + " " +
				this.tagDescr + " " +
				this.tagName + " " +
				this.latitude + " " +
				this.longitude + " " +
				this.altitude;
	}
}
