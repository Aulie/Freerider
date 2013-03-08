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
import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.protocol.NotificationResponse;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.RouteResponse;
import no.ntnu.idi.freerider.protocol.UserResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/** A parser which translates XML into Response objects. */
public class ResponseParser {
	private static Logger logger = LoggerFactory.getLogger(ResponseParser.class);
	
	/** Read and parse XML from this InputStream, returning a Response if found. */
	public static Response parse(InputStream stream){
		SAXReader reader = new SAXReader(false);
		Document document = null;
		try {
			document = reader.read(stream);
			if(logger.isDebugEnabled()){//No need to re-evaluate the XML we just parsed unless debugging.
				logger.debug("Parsing this Response:\n{}",document.asXML());
			}
		} catch (Exception e) {
			logger.warn("Error reading Response.",e);
			return null;
		}
		if(!ResponseValidator.validate(document)){
			return null;
		}
		Element root = document.getRootElement();
		Element header = (Element) root.elements().get(0);
		Element Data = (Element) root.elements().get(1);
		String typestr = header.attributeValue(ProtocolConstants.REQUEST_TYPE_ATTRIBUTE);
		RequestType type = RequestType.valueOf(typestr);

		String data = header.attributeValue(ProtocolConstants.RESPONSE_STATUS_ATTRIBUTE);
		ResponseStatus status = ResponseStatus.valueOf(data);
		String errorMessage = header.attributeValue(ProtocolConstants.ERROR_MESSAGE_ATTRIBUTE);
		if(type.getResponseClass() == JourneyResponse.class.asSubclass(Response.class)){
			List<Journey> journeys = new ArrayList<Journey>();
			@SuppressWarnings("unchecked")
			List<Element> journeyElements = Data.elements();
			for(Element element : journeyElements){
				if(element.getName().equals(ProtocolConstants.JOURNEY)){
					journeys.add(ParserUtils.parseJourney(element));					
				}
			}			
			return new JourneyResponse(type,status, errorMessage, journeys);
		}else if(type.getResponseClass() == RouteResponse.class.asSubclass(Response.class)){
			List<Route> routes = new ArrayList<Route>();
			@SuppressWarnings("unchecked")
			List<Element> journeyElements = Data.elements();
			for(Element element : journeyElements){
				if(element.getName().equals(ProtocolConstants.ROUTE)){
					routes.add(ParserUtils.parseRoute(element));					
				}
			}
			return new RouteResponse(type,status,errorMessage,routes);
		}else if(type.getResponseClass() == NotificationResponse.class.asSubclass(Response.class)){
			List<Notification> notes = new ArrayList<Notification>();
			@SuppressWarnings("unchecked")
			List<Element> noteList = Data.elements(); 
			for(Element element : noteList){
				if(element.getName().equals(ProtocolConstants.NOTIFICATION_ELEMENT)){
					notes.add(ParserUtils.parseNotification(element));
				}
			}
			return new NotificationResponse(type, status, notes);
		}else if(type.getResponseClass() == PreferenceResponse.class.asSubclass(Response.class)){
			@SuppressWarnings("unchecked")
			List<Element> prefList = Data.elements();
			for(Element element : prefList){
				if(element.getName().equals(ProtocolConstants.PREFERENCE)){
					return new PreferenceResponse(type,status,ParserUtils.parsePreference(element));
				}
			}
			return new PreferenceResponse(type, status, new TripPreferences());
		}

		return new UserResponse(type, status, errorMessage);

	}

}
