/*
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
package no.ntnu.idi.freerider.xml;


import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import no.ntnu.idi.freerider.model.Car;
import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.CarRequest;
import no.ntnu.idi.freerider.protocol.CarResponse;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.LoginRequest;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.RouteRequest;
import no.ntnu.idi.freerider.protocol.SearchRequest;
import no.ntnu.idi.freerider.protocol.UserRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A parser which translates XML requests into Request objects. */
public class RequestParser {
	private static Logger logger = LoggerFactory.getLogger(RequestParser.class);



	/** Read and parse XML from this InputStream, returning a Request if found. */
	public static Request parse(InputStream stream){
		//Create and use reader to make Document.
		SAXReader reader = new SAXReader(false);
		Document document = null;
		try {
			document = reader.read(stream);
			if(logger.isDebugEnabled()){
				logger.debug("Parsing request:\n{}",document.asXML());
			}
		} catch (Exception e) {
			logger.warn("Error parsing Request.", e);
		}
		if(!RequestValidator.validate(document)){
			return null;
		}

		//Find the major elements of the Request.
		Element root = document.getRootElement();
		Element header = (Element) root.elements().get(0);
		Element Data = (Element) root.elements().get(1);
		String typestr = header.attributeValue(ProtocolConstants.REQUEST_TYPE_ATTRIBUTE);
		RequestType type = RequestType.valueOf(typestr);
		User user = new User("",header.attributeValue(ProtocolConstants.USER_ATTRIBUTE));

		//Parse out data unique to different request types.
		if(type.getRequestClass() == UserRequest.class.asSubclass(Request.class)){
			user = ParserUtils.parseUser(Data.element(ProtocolConstants.USER_ELEMENT));
			return new UserRequest(type, user);
		}else if(type.getRequestClass() == RouteRequest.class.asSubclass(Request.class)){
			Route route = ParserUtils.parseRoute(Data.element(ProtocolConstants.ROUTE));
			user = route.getOwner();
			return new RouteRequest(type, user, route);
		}else if(type.getRequestClass() == JourneyRequest.class.asSubclass(Request.class)){
			Journey journey = ParserUtils.parseJourney(Data.element(ProtocolConstants.JOURNEY));
			return new JourneyRequest(type, user, journey);
		}else if(type.getRequestClass() == SearchRequest.class.asSubclass(Request.class)){
			Element searchElement = Data.element(ProtocolConstants.SEARCH);
			Calendar starttime = null;
			try {
				Date startDate = new SimpleDateFormat(ProtocolConstants.XML_DATE_FORMAT).parse(searchElement.attributeValue(ProtocolConstants.STARTTIME));
				starttime = new GregorianCalendar();
				starttime.setTime(startDate);
			} catch (ParseException e) {
				logger.error("Error parsing time in SEARCH.", e);
			}
			Location startPoint = ParserUtils.parseLocation(searchElement.element(ProtocolConstants.START_LOCATION));
			Location endPoint = ParserUtils.parseLocation(searchElement.element(ProtocolConstants.END_LOCATION));
			return new SearchRequest(user, startPoint, endPoint, starttime,Integer.parseInt(searchElement.attributeValue(ProtocolConstants.NUMBER_OF_DAYS)));
		}else if(type.getRequestClass() == NotificationRequest.class.asSubclass(Request.class)){
			Notification note = ParserUtils.parseNotification(Data.element(ProtocolConstants.NOTIFICATION_ELEMENT));
			return new NotificationRequest( type,user, note);
		}else if(type.getRequestClass() == LoginRequest.class.asSubclass(Request.class)){
			Element tokenElement = Data.element(ProtocolConstants.ACCESS_TOKEN_ELEMENT);
			String accessToken = tokenElement.getTextTrim();
			return new LoginRequest(user,accessToken);
		}else if(type.getRequestClass() == PreferenceRequest.class.asSubclass(Request.class)){
			TripPreferences preference = ParserUtils.parsePreference(Data.element(ProtocolConstants.PREFERENCE));
			return new PreferenceRequest(type,user,preference);
		}else if(type.getRequestClass() == CarRequest.class.asSubclass(Request.class)){
			Car car = ParserUtils.parseCar(Data.element(ProtocolConstants.CAR));
			return new CarRequest(type,user,car);
		}
		else return null;	
	}

}
