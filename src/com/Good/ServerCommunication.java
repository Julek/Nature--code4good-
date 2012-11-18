package com.Good;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerCommunication {
	private String getHtml() throws ClientProtocolException, IOException
	{
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpGet httpGet = new HttpGet("http://code4good.nh2.me/list");
	    HttpResponse response = httpClient.execute(httpGet, localContext);
	    String result = "";

	    BufferedReader reader = new BufferedReader(
	        new InputStreamReader(
	          response.getEntity().getContent()
	        )
	      );

	    String line = null;
	    while ((line = reader.readLine()) != null){
	      result += line + "\n";
	    }
	    
	    return result;
	}
	
	public List<GeoNode> getLocations(double longitude, double latitude) throws JSONException {
		List<GeoNode> lst = new LinkedList<GeoNode>();
		try {
			String htmlpage = "mock:" + getHtml();
			JSONObject obj = new JSONObject(htmlpage);
			JSONArray arr = obj.getJSONArray("mock");
			for(int i = 0;i<arr.length();i++)
			{
				GeoNode n = new GeoNode();
				JSONObject o = arr.getJSONObject(i);
				n.tagName = o.getString("name");
				n.tagDescr = o.getString("descr");
				n.longitude = o.getDouble("long");
				n.latitude = o.getDouble("lat");
				n.altitude = o.getDouble("alt");
				
				int pos = 0;
				for(int j = 0;j<lst.size();j++) {
					if(n.getDistance(longitude, latitude)>lst.get(j).getDistance(longitude, latitude))
						break;
					pos++;
				}
				lst.add(pos, n);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lst;
	}
	
	
	public void postData(GeoNode n) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

		try {
		    // Add your data
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("name", n.tagName));
		    nameValuePairs.add(new BasicNameValuePair("decr", n.tagDescr));
		    nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(n.longitude)));
		    nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(n.latitude)));
		    nameValuePairs.add(new BasicNameValuePair("alt", String.valueOf(n.altitude)));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		    // Execute HTTP Post Request
		    httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}
	}
}