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

import no.ntnu.idi.freerider.protocol.CarRequest;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.LoginRequest;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RouteRequest;
import no.ntnu.idi.freerider.protocol.SearchRequest;
import no.ntnu.idi.freerider.protocol.SingleJourneyRequest;
import no.ntnu.idi.freerider.protocol.UserRequest;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
/** A serializer which translates Requests into XML. */
public class RequestSerializer {

	/** Serialize this Request. */
	public static String serialize(Request request){
		if(request== null) return null;
		//Create and initialize the request's major structure.
		Document xmldocument = DocumentFactory.getInstance().createDocument();
		xmldocument.setXMLEncoding(ProtocolConstants.PREFERRED_CHARSET.displayName()); //Possible alternative: try ISO-8859-1
		Element root = xmldocument.addElement(ProtocolConstants.REQUEST);
		Element header = root.addElement(ProtocolConstants.REQUEST_HEADER);
		header.addAttribute(ProtocolConstants.REQUEST_TYPE_ATTRIBUTE, request.getType().toString());
		header.addAttribute(ProtocolConstants.PROTOCOL_VERSION_ATTRIBUTE, ProtocolConstants.PROTOCOL_VERSION);
		header.addAttribute(ProtocolConstants.PROTOCOL_ATTRIBUTE, ProtocolConstants.PROTOCOL);
		Element data = root.addElement(ProtocolConstants.DATA);
		
		//Add data unique to this request.
		header.addAttribute(ProtocolConstants.USER_ATTRIBUTE, request.getUser().getID());
		if(request instanceof RouteRequest){
			data.add(SerializerUtils.serializeRoute(((RouteRequest) request).getRoute()));
		}
		else if(request instanceof SearchRequest){
			SearchRequest req = (SearchRequest) request;
			data.add(SerializerUtils.serializeSearch(req.getStartPoint(), req.getEndPoint(), req.getStartTime(),req.getNumDays()));
		}else if(request instanceof UserRequest){
			data.add(SerializerUtils.serializeUser(request.getUser()));
		}else if(request instanceof JourneyRequest){
			data.add(SerializerUtils.serializeJourney(((JourneyRequest) request).getJourney()));
		}else if(request instanceof NotificationRequest){
			data.add(SerializerUtils.serializeNotification(((NotificationRequest) request).getNotification()));
		}else if(request instanceof LoginRequest){
			Element token = new DefaultElement(ProtocolConstants.ACCESS_TOKEN_ELEMENT);
			token.setText(((LoginRequest) request).getAccessToken());
			data.add(token);
		}else if(request instanceof PreferenceRequest) {
			data.add(SerializerUtils.serializePreference(((PreferenceRequest)request).getPreference()));
		}else if(request instanceof CarRequest) {
			data.add(SerializerUtils.serializeCar(((CarRequest)request).getCar()));
		}else if(request instanceof SingleJourneyRequest) {
			data.addAttribute(ProtocolConstants.SINGLE_JOURNEY_ID, Integer.toString(((SingleJourneyRequest)request).getJourneySerial()));
		}
		
		
		return xmldocument.asXML();
	}

	
	
	
}
