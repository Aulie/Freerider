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
package no.ntnu.idi.freerider.protocol;


/** An enum describing the different kinds of requests a client can make of the server.*/
public enum RequestType {
	/** Place a Route on the database. */
	CREATE_ROUTE,
	/** Place a Route in the database, marking it as ad-hoc and attached to a single Journey.*/
	CREATE_AD_HOC_ROUTE,
	/** Edit a Route on the database. */
	UPDATE_ROUTE,
	/** Delete a Route from the database. */
	DELETE_ROUTE,
	/** Create a Journey in the database. */
	CREATE_JOURNEY,
	/** Edit a Journey in the database. */
	UPDATE_JOURNEY,
	/** Delete a Journey from the database. NOTE: Only applicable with no hiker. SEND_NOTIFICATION is used to cancel Journeys that already have hikers.*/
	DELETE_JOURNEY,
	/** Perform a search for applicable Journeys. */
	SEARCH,
	/** Create a User record in the database. */
	CREATE_USER,
	/** Edit a User record in the database. */
	UPDATE_USER,
	
	LOGIN,
	START_JOURNEY,
	RATE_JOURNEY,
	/** Send a Notification to another User.*/
	SEND_NOTIFICATION,
	/** Retrieve Notifications intended for this User. */
	PULL_NOTIFICATIONS,
	/** Mark a given Notification as read (displayed to the user) in the database.*/
	MARK_NOTIFICATION_READ,
	
	/** Get the Journeys involving this user (either as a driver or hitchhiker). Journeys are in short form (include MapLocations but no other route data) */
	GET_JOURNEYS,
	/** Get the full details of a specific Journey. */
	GET_JOURNEY,
	/** Get Routes owned by this user. Routes returned are in short form, including MapLocations but no other route data. */
	GET_ROUTES,
	/** Get the full details of a specific Route. */
	GET_ROUTE,
	/**Get specified preference */
	GET_PREFERENCE,
	/** Add preference */
	CREATE_PREFERENCE,
	/** Update specified preference */
	UPDATE_PREFERENCE,
	/** Get specified car */
	GET_CAR,
	/** Create new car */
	CREATE_CAR,
	/** Update specified car */
	UPDATE_CAR
	;

	/**
	 * This method maps a RequestType to the appropriate subclass of Request for requests of that type.
	 * @return The Request subclass using this RequestType.
	 */
	@SuppressWarnings("unchecked")
	public Class<Request> getRequestClass(){
		switch(this){
		case CREATE_ROUTE: case UPDATE_ROUTE: case DELETE_ROUTE: case GET_ROUTE: case CREATE_AD_HOC_ROUTE:
			return (Class<Request>) RouteRequest.class.asSubclass(Request.class);
		case CREATE_JOURNEY: case UPDATE_JOURNEY: case DELETE_JOURNEY: case GET_JOURNEY:
			return (Class<Request>) JourneyRequest.class.asSubclass(Request.class);
		case SEARCH:
			return (Class<Request>) SearchRequest.class.asSubclass(Request.class);
		case SEND_NOTIFICATION: case MARK_NOTIFICATION_READ:
			return (Class<Request>) NotificationRequest.class.asSubclass(Request.class);
		case LOGIN:
			return (Class<Request>) LoginRequest.class.asSubclass(Request.class);
		case GET_PREFERENCE:
			return (Class<Request>) PreferenceRequest.class.asSubclass(Request.class);
		case CREATE_PREFERENCE:
			return (Class<Request>) PreferenceRequest.class.asSubclass(Request.class);
		case UPDATE_PREFERENCE:
			return (Class<Request>) PreferenceRequest.class.asSubclass(Request.class);
		case CREATE_CAR:
			return (Class<Request>) CarRequest.class.asSubclass(Request.class);
		case UPDATE_CAR:
			return (Class<Request>) CarRequest.class.asSubclass(Request.class);
		case GET_CAR:
			return (Class<Request>) CarRequest.class.asSubclass(Request.class);
		default: 
			return (Class<Request>) UserRequest.class.asSubclass(Request.class);
		}
	}

	/**
	 * This method maps a RequestType to the appropriate subclass of Response for that type.
	 * @return The Response subclass using this RequestType.
	 */
	@SuppressWarnings("unchecked")
	public Class<Response> getResponseClass(){
		switch(this){
		case CREATE_JOURNEY: case UPDATE_JOURNEY: case GET_JOURNEYS: case SEARCH: case GET_JOURNEY:
			return (Class<Response>) JourneyResponse.class.asSubclass(Response.class);
		case CREATE_ROUTE: case UPDATE_ROUTE: case GET_ROUTES: case GET_ROUTE: case CREATE_AD_HOC_ROUTE:
			return (Class<Response>) RouteResponse.class.asSubclass(Response.class);
		case PULL_NOTIFICATIONS:
			return (Class<Response>) NotificationResponse.class.asSubclass(Response.class);
		case GET_PREFERENCE:
			return (Class<Response>) PreferenceResponse.class.asSubclass(Response.class);
		case CREATE_PREFERENCE:
			return (Class<Response>) PreferenceResponse.class.asSubclass(Response.class);
		case UPDATE_PREFERENCE:
			return (Class<Response>) PreferenceResponse.class.asSubclass(Response.class);
		case CREATE_CAR:
			return (Class<Response>) CarResponse.class.asSubclass(Response.class);
		case UPDATE_CAR:
			return (Class<Response>) CarResponse.class.asSubclass(Response.class);
		case GET_CAR:
			return (Class<Response>) CarResponse.class.asSubclass(Response.class);
		default:
			return (Class<Response>) UserResponse.class.asSubclass(Response.class);
		}
	}
}
