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
package no.ntnu.idi.freerider.xml;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.protocol.CarResponse;
import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.NotificationResponse;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.RouteResponse;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

/** A serializer translating Responses into XML. */
public class ResponseSerializer {

	public static String serialize(Response responseObject){
		//Initialize empty Response document.
		Document xmlResponse = DocumentFactory.getInstance().createDocument();
		xmlResponse.setXMLEncoding(ProtocolConstants.PREFERRED_CHARSET.displayName()); //Possible alternative: try ISO-8859-1
		Element responseRoot = xmlResponse.addElement(ProtocolConstants.RESPONSE);
		Element responseHeader = responseRoot.addElement(ProtocolConstants.RESPONSE_HEADER);
		responseHeader.addAttribute(ProtocolConstants.PROTOCOL_ATTRIBUTE, ProtocolConstants.PROTOCOL);
//		responseHeader.addAttribute("xmlns:tns", ProtocolConstants.XMLNS_RESPONSE);
//		responseHeader.addAttribute("xmlns:xsi", ProtocolConstants.XMLNS_XSI);
//		responseHeader.addAttribute("xsi:schemalocation", ProtocolConstants.XSI_SCHEMALOCATION_RESPONSE);
		responseHeader.addAttribute(ProtocolConstants.PROTOCOL_VERSION_ATTRIBUTE, ProtocolConstants.PROTOCOL_VERSION);
		
		//Handle null request errors.
		if(responseObject==null){
			responseHeader.addAttribute(ProtocolConstants.RESPONSE_STATUS_ATTRIBUTE, ResponseStatus.FAILED.toString());
			return xmlResponse.asXML();
		}
		
		//Complete initialization.
		responseHeader.addAttribute(ProtocolConstants.REQUEST_TYPE_ATTRIBUTE, responseObject.getType().toString());
		if(responseObject.getErrorMessage()!=null){
			responseHeader.addAttribute(ProtocolConstants.ERROR_MESSAGE_ATTRIBUTE, responseObject.getErrorMessage());
		}
		Element responseData = responseRoot.addElement(ProtocolConstants.DATA);
		
		//Add header data.
		responseHeader.addAttribute(ProtocolConstants.RESPONSE_STATUS_ATTRIBUTE, responseObject.getStatus().toString());
		//Insert other data.
		if(responseObject instanceof JourneyResponse && ((JourneyResponse) responseObject).getJourneys()!=null){
			for (Journey journey : ((JourneyResponse) responseObject).getJourneys()) {
				Element journeyElement = SerializerUtils.serializeJourney(journey); 
				if(journeyElement!=null){
					responseData.add(journeyElement);
				}
			}
		}
		if(responseObject instanceof RouteResponse && ((RouteResponse) responseObject).getRoutes()!=null){
			for (Route route : ((RouteResponse) responseObject).getRoutes()) {
				Element routeElement = SerializerUtils.serializeRoute(route); 
				if(routeElement!=null){
					responseData.add(routeElement);
				}
			}
		}
		if(responseObject instanceof NotificationResponse && ((NotificationResponse) responseObject).getNotifications() != null)
		for(Notification note : ((NotificationResponse) responseObject).getNotifications()){
			Element noteElement = SerializerUtils.serializeNotification(note);
			if(noteElement != null) responseData.add(noteElement);
		}
		if(responseObject instanceof PreferenceResponse && ((PreferenceResponse)responseObject).getPreferences() != null){
			Element prefElement = SerializerUtils.serializePreference(((PreferenceResponse)responseObject).getPreferences());
			if(prefElement != null) 
			{
				responseData.add(prefElement);
			}
		}
		if(responseObject instanceof CarResponse && ((CarResponse)responseObject).getCar() != null){
			Element carElement = SerializerUtils.serializeCar(((CarResponse)responseObject).getCar());
			if(carElement != null) 
			{
				responseData.add(carElement);
			}
		}
		return xmlResponse.asXML();
	}

	
}
