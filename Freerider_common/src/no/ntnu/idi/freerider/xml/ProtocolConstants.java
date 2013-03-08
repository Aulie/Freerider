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

import java.nio.charset.Charset;

import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.ResponseStatus;


/**
 * An interface defining constants of the XML-based client-server protocol.
 *
 */
interface ProtocolConstants {
	static final Charset PREFERRED_CHARSET = Charset.forName("ISO-8859-1");
	//Namespace stuff.
	/** The shared prefix of our XML documents' namespaces. */
	static final String XMLNS_TNS_PREFIX="http://code.google.com/a/eclipselabs.org/p/sintef-social-hitchhiking/";
	/** The namespace of a Request. */
	static final String XMLNS_REQUEST = XMLNS_TNS_PREFIX + "Request";
	/** The namespace of a Response. */
	static final String XMLNS_RESPONSE= XMLNS_TNS_PREFIX + "Response";
	static final String XMLNS_XSI="http://www.w3.org/2001/XMLSchema-instance";
	static final String XSI_SCHEMALOCATION_PREFIX = "http://svn.codespot.com/a/eclipselabs.org/sintef-social-hitchhiking/Social_hitchhiking_common/src/no/ntnu/idi/freerider/xml/";
	static final String XSI_SCHEMALOCATION_REQUEST = XMLNS_REQUEST + " " + XSI_SCHEMALOCATION_PREFIX + "/RequestSchema.xsd ";
	static final String XSI_SCHEMALOCATION_RESPONSE = XMLNS_RESPONSE + " " + XSI_SCHEMALOCATION_PREFIX + "/ResponseSchema.xsd ";
	
	//The root elements of the XML documents.
	/** The name of the root element of a request. */
	static final String REQUEST = "Request";
	/** The name of root element of a response. */
	static final String RESPONSE = "Response";
	
	//Headers and header attributes
	/** The name of the first child element of a {@link REQUEST}. */
	static final String REQUEST_HEADER = "RequestHeader";
	/** The name of the first child element of a {@link RESPONSE}. */
	static final String RESPONSE_HEADER = "ResponseHeader";
	/** The name of the protocol attribute of a {@link REQUEST_HEADER}. */
	static final String PROTOCOL_ATTRIBUTE = "protocol";
	/** The name of the protocol. */
	static final String PROTOCOL = "Social_hitchhiking";
	/** The name of the protocol version attribute of a  {@link REQUEST_HEADER}. */
	static final String PROTOCOL_VERSION_ATTRIBUTE = "protocol_version";
	/** Current protocol version. */
	static final String PROTOCOL_VERSION = "1.0.0";
	/** The name of the request type attribute of a {@link REQUEST_HEADER}. Valid values for this attribute are the names of a valid {@link RequestType}.*/ 
	static final String REQUEST_TYPE_ATTRIBUTE = "type";	
	/** The name of the response status attribute of a {@link RESPONSE_HEADER}. Valid values for this attribute are the names of a valid {@link ResponseStatus}.*/
	static final String RESPONSE_STATUS_ATTRIBUTE = "status";
	static final String USER_ATTRIBUTE = "user";
	static final String ERROR_MESSAGE_ATTRIBUTE = "error_message";

	
	//Data headers, children and attributes.
	/** The name of the second child element of a  {@link REQUEST} */
	static final String DATA = "Data";
	
	/** The name of a location/geodata element of a  {@link DATA} element.*/ 
	static final String LOCATION_DATA = "Location";
	/** The name of a  {@link LOCATION_DATA}'s latitude attribute. Valid values for this attribute are parseable doubles in the range[-90,90].*/ 
	static final String LATITUDE = "LAT";
	/** The name of a  {@link LOCATION_DATA}'s longitude attribute. Valid values for this attribute are parseable doubles in the range [-180,180]*/
	static final String LONGITUDE = "LONG";
	static final String MAPLOCATION_DATA = "MapLocation";
	static final String MAPLOCATION_ADDRESS = "address";
	
	/** The name of an element representing a {@link Route}.*/
	static final String ROUTE = "Route";
	/** The name of a {@link ROUTE}'s name field. */
	static final String ROUTE_NAME = "name";
	/**The name of a {@link ROUTE}'s serial field. */
	static final String ROUTE_SERIAL = "serial";
	static final String ROUTE_OWNER = "owner";
	static final String ROUTE_FREQUENCY = "frequency";
	
	/**The name of an element representing a Journey. */
	static final String JOURNEY = "JOURNEY";
	static final String JOURNEY_SERIAL = "serial";
	static final String JOURNEY_START = "starttime";
	static final String JOURNEY_VISIBILITY = "visibility";
	static final String JOURNEY_HITCHHIKER = "hiker";
	
	static final String SEARCH = "Search";
	static final String START_LOCATION = "Startlocation";
	static final String END_LOCATION = "Endlocation";
	static final String STARTTIME = "Starttime";
	static final String XML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	static final String USER_ELEMENT = "User";
	static final String USER_NAME = "name";
	static final String USER_SURNAME = "surname";
	static final String USER_ID = "id";
	static final String USER_RATING = "rating";
	
	static final String NOTIFICATION_ELEMENT = "Notification";
	static final String NOTIFICATION_TYPE = "type";
	static final String NOTIFICATION_RECIPIENT = "recipientID";
	static final String NOTIFICATION_SENDER = "senderID";
	static final String NOTIFICATION_TIME_SENT = "time_sent";
	static final String NOTIFICATION_JOURNEYSERIAL = "journey";
	static final String NOTIFICATION_STARTPOINT = "StartLocation";
	static final String NOTIFICATION_STOPPOINT = "StopLocation";
	static final String NOTIFICATION_NO_TIME = "NULL";
	static final String NOTIFICATION_COMMENT = "comment";
	static final String NOTIFICATION_SENDER_NAME = "sender_name";
	static final String NOTIFICATION_IS_READ = "is_read";
	
	static final String ACCESS_TOKEN_ELEMENT = "AccessToken";
	
	static final String PREFERENCE = "Preference";
	static final String PREFERENCE_ELEMENT = "preference_element";
	static final String PREFERENCE_ID = "preference_id";
	static final String PREFERENCE_SEATS = "preference_seats";
	static final String PREFERENCE_MUSIC = "preference_music";
	static final String PREFERENCE_ANIMALS = "preference_animals";
	static final String PREFERENCE_BREAKS = "preference_breaks";
	static final String PREFERENCE_TALKING = "preference_talking";
	static final String PREFERENCE_SMOKING = "preference_smoking";
	
	

}
