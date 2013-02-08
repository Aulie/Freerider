/**
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @version: 		1.0
 *
 * Copyright (C) 2012 Freerider Team.
 *
 * Licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package no.ntnu.idi.socialhitchhiking.map;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import no.ntnu.idi.freerider.model.MapLocation;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/**
 * This class retrieves a {@link MapRoute} using the Google Maps API.
 * <br><br>
 * Use the static method {@link #getRoute(double, double, double, double)}.
 */
public class RouteProvider {
	
	/**
	 * Returning a {@link MapRoute}, containing data that is retrieved from Google Maps.
	 * 
	 * @param fromLat The latitude where the route starts.
	 * @param fromLon The longitude where the route starts.
	 * @param toLat The latitude where the route ends.
	 * @param toLon The latitude where the route ends.
	 * @return Returns a {@link MapRoute} containing all the map data needed for showing a route in a map view.
	 * @throws MalformedURLException 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static MapRoute getRoute(double fromLat, double fromLon, double toLat, double toLon) throws MalformedURLException, IOException
	{
		String url = RouteProvider.getUrl(fromLat, fromLon, toLat, toLon);
		Log.e("TestLL","FL " + fromLat + " FL " + fromLon + " TL " + toLat + " TL " + toLon);
		Log.e("URL",url);
		InputStream is = RouteProvider.getConnectionInputStream(url);
		
		MapRoute temp = new MapRoute();

			try {
				temp = RouteProvider.getRoute(is);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				Log.e("XMLError", e.getMessage());
				Log.e("XMLCause",e.getCause().toString());
			} catch (IOException e)
			{
				Log.e("IOError", e.getMessage());
				Log.e("IOCause",e.getCause().toString());
			}

		Log.e("Reached","A MapRoute");
		return temp;
	}

	
	private static MapRoute getRoute(InputStream is) throws IOException, XmlPullParserException {
		XMLParser parser = new XMLParser();
		return parser.parse(is);
	}

	/**
	 * Creates a url that should be used to retrieve a route from google maps.
	 * 
	 * @param fromLat
	 * @param fromLon
	 * @param toLat
	 * @param toLon
	 * @return
	 */
	private static String getUrl(double fromLat, double fromLon, double toLat, double toLon) {// connect to map web service
		StringBuffer urlString = new StringBuffer();
			urlString.append("http://maps.googleapis.com/maps/api/directions/xml?");
			urlString.append("origin=");// from
			urlString.append(Double.toString(fromLat));
			urlString.append(",");
			urlString.append(Double.toString(fromLon));
			urlString.append("&destination=");// to
			urlString.append(Double.toString(toLat));
			urlString.append(",");
			urlString.append(Double.toString(toLon));
			urlString.append("&sensor=false");
			return urlString.toString();
	}
	/**
	 * Creates a connection from the given URL, and returns an {@link InputStream} for reading data.
	 * 
	 * @param url The URL that the connection refer to.
	 * @return Returns an {@link InputStream} for reading data.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private static InputStream getConnectionInputStream(String url) throws MalformedURLException, IOException {
		URLConnection conn = new URL(url).openConnection();
		return conn.getInputStream();
	} 
}

class KMLHandler extends DefaultHandler {
	MapRoute route;
	boolean isPlacemark;
	boolean isRoute;
	boolean isItemIcon;
	private Stack<String> mCurrentElement = new Stack<String>();
	private String mString;

	public KMLHandler() {
		route = new MapRoute();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		mCurrentElement.push(localName);
		if (localName.equalsIgnoreCase("Placemark")) {
			isPlacemark = true;
		} else if (localName.equalsIgnoreCase("ItemIcon")) {
			if (isPlacemark){
				isItemIcon = true;
			}
		}
		mString = new String();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = new String(ch, start, length).trim();
		mString = mString.concat(chars);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (mString.length() > 0) {
			if (localName.equalsIgnoreCase("name")) {
				if (isPlacemark) {
					isRoute = mString.equalsIgnoreCase("Route");
					if (!isRoute) {
						
					}
				} else {
					route.setRouteName(mString);
				}
			} else if (localName.equalsIgnoreCase("color") && !isPlacemark) {

			} else if (localName.equalsIgnoreCase("width") && !isPlacemark) {
				
			} else if (localName.equalsIgnoreCase("description")) {
				if (isPlacemark) {
					String description = cleanup(mString);
					if (!isRoute){
						
					}else{
						route.setDistanceDescription(description);
					}
				}
			} else if (localName.equalsIgnoreCase("href")) {
				if (isItemIcon) {
					
				}
			} else if (localName.equalsIgnoreCase("coordinates")) {
				if (isPlacemark) {
					if (!isRoute) {

					} else {
						String[] coordinatesParsed = split(mString, " ");
						for (int i = 0; i < coordinatesParsed.length; i++) {
							String[] xyParsed = split(coordinatesParsed[i], ",");
							double lat = Double.parseDouble(xyParsed[1]);
							double lon = Double.parseDouble(xyParsed[0]);
							route.addCoordinate(new MapLocation(lat, lon));
							if(i == 0) {
								route.addToDrivingThroughList(GeoHelper.getMapLocation(lat, lon));
							}else if(i == coordinatesParsed.length-1) {
								route.addToDrivingThroughList(GeoHelper.getMapLocation(lat, lon));
							}
						}
					}
				}
			}
		}
		mCurrentElement.pop();
		if (localName.equalsIgnoreCase("Placemark")) {
			isPlacemark = false;
			if (isRoute)
				isRoute = false;
		} else if (localName.equalsIgnoreCase("ItemIcon")) {
			if (isItemIcon)
				isItemIcon = false;
		}
	}

	private String cleanup(String value) {
		String remove = "<br/>";
		int index = value.indexOf(remove);
		if (index != -1)
			value = value.substring(0, index);
		remove = "&#160;";
		index = value.indexOf(remove);
		int len = remove.length();
		while (index != -1) {
			value = value.substring(0, index).concat(
					value.substring(index + len, value.length()));
			index = value.indexOf(remove);
		}
		return value;
	}

	private static String[] split(String strString, String strDelimiter) {
		String[] strArray;
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;
		if (strString == null) {
			throw new IllegalArgumentException("Input string cannot be null.");
		}
		if (strDelimiter.length() <= 0 || strDelimiter == null) {
			throw new IllegalArgumentException(
					"Delimeter cannot be null or empty.");
		}
		if (strString.startsWith(strDelimiter)) {
			strString = strString.substring(strDelimiter.length());
		}
		if (!strString.endsWith(strDelimiter)) {
			strString += strDelimiter;
		}
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
				iIndexOfInnerString)) != -1) {
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}
		strArray = new String[iOccurrences];
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
				iIndexOfInnerString)) != -1) {
			strArray[iCounter] = strString.substring(iIndexOfInnerString,
					iIndexOfDelimiter);
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
			iCounter += 1;
		}

		return strArray;
	}
}