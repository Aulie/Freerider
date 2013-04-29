/*******************************************************************************
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @contributor(s): Freerider Team 2 (Group 3, IT2901 Spring 2013, NTNU)
 * @version: 2.0
 * 
 * Copyright 2013 Freerider Team 2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package no.ntnu.idi.socialhitchhiking.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.MapLocation;
import no.ntnu.idi.socialhitchhiking.map.MapRoute;

/**
 * Class for parsing the XML-files from google maps
 * 
 * @author Thomas Gjerde
 * @author Jon-Robert
 *
 */
public class XMLParser 
{
	private static final String ns = null;
	private boolean drawable;
	public XMLParser(boolean drawable)
	{
		this.drawable = drawable;
	}
	
	/**
	 * The parser
	 * @param InputStream is
	 * @return MapRoute required from readFeed(parser, "DirectionsResponse")
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public MapRoute parse(InputStream is) throws XmlPullParserException, IOException{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(is,null);
			parser.nextTag();
			Log.e("Reached","Parse");
			return readFeed(parser, "DirectionsResponse");
		}
		finally
		{
			is.close();
		}
	}
	
	/**
	 * Method for extracting route and legs from the feed
	 * @param parser
	 * @param String type that specifies the action that should be performed
	 * @return MapRoute
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private MapRoute readFeed(XmlPullParser parser, String type) throws XmlPullParserException, IOException{
		
		MapRoute route = new MapRoute();
		parser.require(XmlPullParser.START_TAG, ns, type);
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equals("route")) {
				route = readFeed(parser, "route");
			}
			else if(name.equals("leg")) {
				route = readLeg(parser);
			}
			else {
				skip(parser);
			}
		}
		return route;
	}	
	
	/**
	 * Method for reading leg
	 * @param parser
	 * @return the MapRoute for the leg
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private MapRoute readLeg(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		Log.e("Reached","Leg");
		MapRoute route = new MapRoute();
		double duration = 0;
		parser.require(XmlPullParser.START_TAG, ns, "leg");
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equals("step")) {
				Step step = readStep(parser);
				try
				{
					duration = duration + (Double.parseDouble(step.minutesDuration)/60);
				}
				catch(Exception e)
				{
					
				}
				route.addCoordinate(new MapLocation(Double.parseDouble(step.getStartLatitude()), Double.parseDouble(step.getStartLongitude())));
				//Polyline
				if(drawable){
					for(int i = 0; i < step.mapPoints.size(); i++) {
						route.addCoordinate(new MapLocation(step.mapPoints.get(i).latitude, step.mapPoints.get(i).longitude));
						//route.addToDrivingThroughList(new MapLocation(step.mapPoints.get(i).latitude, step.mapPoints.get(i).longitude));
					}
				}
				//End polyline
				route.addCoordinate(new MapLocation(Double.parseDouble(step.getEndLatitude()), Double.parseDouble(step.getEndLongitude())));
				route.addToDrivingThroughList(new MapLocation(Double.parseDouble(step.getEndLatitude()), Double.parseDouble(step.getEndLongitude())));
				route.setDistanceInMinutes(duration);
			} else {
				skip(parser);
			}
		}
		return route;
	}
	
	/**
	 * Method for reading a step
	 * @param parser
	 * @return the Step step
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Step readStep(XmlPullParser parser) throws XmlPullParserException, IOException {
		Step step = new Step();
		parser.require(XmlPullParser.START_TAG, ns, "step");
		
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG){
				continue;
			}
			String name = parser.getName();
			if(name.equals("start_location")) {
				LowLocation ll = readLocation(parser, name);
				step.setStartLatitude(ll.getLatitude());
				step.setStartLongitude(ll.getLongitude());
				
			} else if(name.equals("end_location")) {
				LowLocation ll = readLocation(parser, name);
				step.setEndLatitude(ll.getLatitude());
				step.setEndLongitude(ll.getLongitude());
				
			} else if(name.equals("polyline")) {
				step.setMapPoints(decodePoly(readPolyline(parser)));
				
			} else if(name.equals("duration")) {
				step.setMinutesDuration(readData(parser, name, "value"));
				
			} else if(name.equals("html_instructions")) {
				step.setDescription(readString(parser,"html_instructions"));
			
			} else if(name.equals("distance")) {
				step.setKmDistance(readData(parser, name, "text"));
			
			} else {
				skip(parser);
			}
				
		}
		return step;
	}
	
	/**
	 * Method for creating a LowLocation
	 * @param parser
	 * @param type
	 * @return LowLocation lowLoc
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private LowLocation readLocation(XmlPullParser parser, String type) throws XmlPullParserException, IOException{
		
		LowLocation lowLoc = new LowLocation();
		
		parser.require(XmlPullParser.START_TAG, ns, type);
		
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equals("lat")) {
				lowLoc.setLatitude(readString(parser,"lat"));
			
			} else if(name.equals("lng")) {
				lowLoc.setLongitude(readString(parser,"lng"));
			
			} else {
				skip(parser);
			
			}
		}
		
		return lowLoc;
		
	}
	
	/**
	 * Method for reading data relevant for the journey
	 * @param parser
	 * @param typeName
	 * @param type
	 * @return a String containing the data fetched
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private String readData(XmlPullParser parser, String typeName, String type) throws XmlPullParserException, IOException{
		String ret = "";
		parser.require(XmlPullParser.START_TAG, ns, typeName);
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equals(type)) {
				ret = readString(parser,type);
			
			} else {
				skip(parser);
			
			}
		}
		return ret;
	}
	
	/**
	 * Method for reading a PolyLine
	 * @param parser
	 * @return a String containing a PolyLine
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private String readPolyline(XmlPullParser parser) throws XmlPullParserException, IOException {
		String ret = "";
		parser.require(XmlPullParser.START_TAG, ns, "polyline");
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equals("points")) {
				ret = readString(parser,"points");
			
			} else {
				skip(parser);
			
			}
		}
		
		return ret;
	}
	
	/**
	 * Method for reading a String
	 * @param parser
	 * @param name
	 * @return the processed String
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private String readString(XmlPullParser parser, String name) throws XmlPullParserException, IOException {
		
		parser.require(XmlPullParser.START_TAG, ns, name);
		String result = "";
		if(parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		parser.require(XmlPullParser.END_TAG, ns, name);
		return result;
	}
	
	/**
	 * Skip-method
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    	if(parser.getEventType() != XmlPullParser.START_TAG) {
    		throw new IllegalStateException();
    	}
    	int depth = 1;
    	while(depth != 0) {
    		switch(parser.next()) {
    		case XmlPullParser.END_TAG:
    			depth--;
    			break;
    		case XmlPullParser.START_TAG:
    			depth++;
    			break;
    		}
    	}
    }
	
	/**
	 * Method for getting detailed line
	 * @param encoded
	 * @return ArrayList<Location>
	 */
	public static ArrayList<Location> decodePoly(String encoded) {
		ArrayList<Location> poly = new ArrayList<Location>();
		int index = 0, len = encoded.length();
		int lat = 0; 
		int lng = 0;
		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
		    b = encoded.charAt(index++) - 63;
		    result |= (b & 0x1f) << shift;
		    shift += 5;
		   } while (b >= 0x20);
		   int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		   lat += dlat;
		   shift = 0;
		   result = 0;
		   do {
		    b = encoded.charAt(index++) - 63;
		    result |= (b & 0x1f) << shift;
		    shift += 5;
		   } while (b >= 0x20);
		   int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		   lng += dlng;
		   Location p = new Location((((double) lat / 1E5)),
		     (((double) lng / 1E5)));
		   poly.add(p);
		  }
		  return poly;
		 }
	
}
