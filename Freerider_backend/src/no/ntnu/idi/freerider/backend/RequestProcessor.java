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
package no.ntnu.idi.freerider.backend;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.ntnu.idi.freerider.backend.facebook.FacebookFilter;
import no.ntnu.idi.freerider.model.Car;
import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.NotificationType;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.CarRequest;
import no.ntnu.idi.freerider.protocol.CarResponse;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.NotificationResponse;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.RouteRequest;
import no.ntnu.idi.freerider.protocol.RouteResponse;
import no.ntnu.idi.freerider.protocol.SearchRequest;
import no.ntnu.idi.freerider.protocol.UserResponse;
import no.ntnu.idi.freerider.protocol.LoginRequest;

/** The server's primary business logic class,
 *  which operates the database using DBConnector,
 *  processes incoming Requests and returns Responses. */
public class RequestProcessor {
	private DBConnector db;
	private static Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
	/** The social network filtering our searches etc. */
	private SocialNetwork filterService;

	public RequestProcessor(){
		db = new DBConnector();
		try {
			db.init();
		} catch (SQLException e) {
			logger.error("Error connecting to database.",e);
			throw new RuntimeException("Error connecting to database",e);
		}
		filterService = new FacebookFilter();
	}

	public Response process(Request request){
		if(request == null){
			return new UserResponse(RequestType.CREATE_USER, ResponseStatus.CLIENT_ERROR,"Could not parse request.");
		}
		ServerLogger.write(request.toString());
		ResponseStatus status = ResponseStatus.OK;
		RequestType type = request.getType();
		List<Journey> journeys = null;
		logger.error("Test");
		switch(type){
		case CREATE_USER:
			User newUser = request.getUser();
			try {
				db.addUser(newUser);
			} catch (SQLException e) {
				logger.error("Error creating new user.",e);
				/*
				try {
					db.updateUser(newUser);
				} catch (SQLException e1) {
					
				}
				*/
				return new UserResponse(type,ResponseStatus.FAILED,e.getMessage());
			}
			return new UserResponse(type, status,newUser);

		case UPDATE_USER:
			User tempUser = request.getUser();
			try {
				db.updateUser(tempUser);
			} catch (SQLException e) {
				return new UserResponse(type, ResponseStatus.FAILED,e.getMessage());
			}
			return new UserResponse(type,status,tempUser);
		case LOGIN:
			try {
				db.setAccessToken(request.getUser().getID(), ((LoginRequest) request).getAccessToken());
				return new UserResponse(type,ResponseStatus.OK);
			} catch (SQLException e) {
				logger.error("Error setting access token.",e);
				return new UserResponse(type,ResponseStatus.FAILED,e.getMessage());
			}
		case GET_USER:
			try
			{
				User ret = db.getUser(request.getUser().getID());
				ServerLogger.write("About:" + ret.getAbout());
				return new UserResponse(type, status, ret);
			} catch (SQLException e3)
			{
				ServerLogger.write("SQLERROR: " + e3.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e3.getMessage());
			}
		case CREATE_ROUTE:
			List<Route> routes = null;
			try {
				Route tobesaved = ((RouteRequest) request).getRoute();
				Route savedRoute = db.addRoute(tobesaved);
				routes = new ArrayList<Route>();
				routes.add(savedRoute);
				return new RouteResponse(type,ResponseStatus.OK,routes);
			} catch (SQLException e) {
				ServerLogger.write("Error saving route" + e.getMessage());
				return new RouteResponse(type,ResponseStatus.FAILED,e.getMessage(),routes);
			}
		case CREATE_AD_HOC_ROUTE:
			routes = null;
			try {
				Route tobesaved = ((RouteRequest) request).getRoute();
				Route savedRoute = db.addAdhocRoute(tobesaved);
				routes = new ArrayList<Route>();
				routes.add(savedRoute);
				return new RouteResponse(type,ResponseStatus.OK,routes);
			} catch (SQLException e) {
				logger.error("Error saving route.",e);
				return new RouteResponse(type,ResponseStatus.FAILED,e.getMessage(),routes);
			}
		case UPDATE_ROUTE:
			routes = null;
			try {
				Route savedRoute = db.updateRoute(((RouteRequest) request).getRoute());
				routes = new ArrayList<Route>();
				routes.add(savedRoute);
				return new RouteResponse(type,ResponseStatus.OK,routes);
			} catch (SQLException e) {
				logger.error("Error updating route.",e);
				return new RouteResponse(type,ResponseStatus.FAILED,e.getMessage(),routes);
			}
		case GET_ROUTES:
			routes = null;
			ServerLogger.write("GET_ROUTES");
			String id = request.getUser().getID();
			try {
				ServerLogger.write("Route start");
				routes = db.getRoutes(id);
				ServerLogger.write("Route returned");
				return new RouteResponse(type,ResponseStatus.OK,routes);
			} catch (SQLException e1) {
				ServerLogger.write("Error retrieving routes: " + e1.getMessage());
				return new RouteResponse(type,ResponseStatus.FAILED,e1.getMessage(),routes);
			}
		case GET_ROUTE:
			routes = new ArrayList<Route>();
			int serial = ((RouteRequest) request).getRoute().getSerial();
			try {
				routes.add(db.getRoute(serial));
			} catch (SQLException e2) {
				ServerLogger.write("Error retrieving route: " + e2.getMessage());
				return new RouteResponse(type,ResponseStatus.FAILED,e2.getMessage(),routes);
			}
		case DELETE_ROUTE:
			try {
				db.deleteRoute(((RouteRequest) request).getRoute());
				return new UserResponse(type,ResponseStatus.OK);
			} catch (SQLException e1) {
				logger.error("Error deleting route.",e1);
				return new UserResponse(type,ResponseStatus.FAILED,e1.getMessage());
			}
		case CREATE_JOURNEY:
			try{
				Journey savedJourney = db.addJourney(((JourneyRequest) request).getJourney());
				journeys = new ArrayList<Journey>();
				journeys.add(savedJourney);
				ServerLogger.write("Before Journey Return");
				return new JourneyResponse(type, ResponseStatus.OK, journeys);
			}catch (SQLException e) {
				ServerLogger.write("Error saving journey: " + e.getMessage());
				return new JourneyResponse(type, ResponseStatus.FAILED,e.getMessage(),journeys);
			}
		case UPDATE_JOURNEY:
			Journey journey = ((JourneyRequest) request).getJourney();
			try {
				Journey savedJourney = db.updateJourney(journey);
				journeys = new ArrayList<Journey>();
				journeys.add(savedJourney);
				return new JourneyResponse(type, ResponseStatus.OK, journeys);
			} catch (SQLException e) {
				logger.error("Error updating Journey " + journey.getSerial(),e);
				return new JourneyResponse(type,ResponseStatus.FAILED,e.getMessage(),journeys);
			}
		case DELETE_JOURNEY:
			try {
				db.deleteJourney(((JourneyRequest) request).getJourney());
				return new UserResponse(type,ResponseStatus.OK);
			} catch (SQLException e1) {
				logger.error("Error deleting journey.",e1);
				return new UserResponse(type,ResponseStatus.FAILED,e1.getMessage());
			}
		case SEARCH:
			try {
				SearchRequest req = (SearchRequest) request;
				journeys = db.search(req.getStartPoint(), req.getEndPoint(), req.getStartTime(),req.getUser());
				Calendar c = Calendar.getInstance();
				for(int i = 1; i < req.getNumDays(); i++) {
					
					c.add(Calendar.DAY_OF_MONTH, 2); //1 or 2?
					List<Journey> tempJourneys = db.search(req.getStartPoint(), req.getEndPoint(), c, req.getUser());
					for(int j = 0; j < tempJourneys.size(); j++) {
						journeys.add(tempJourneys.get(j));
					}
				}
				Collections.sort(journeys, new Comparator<Journey>(){

					@Override
					public int compare(Journey arg0, Journey arg1) {
						if (arg0.getStart() == null || arg1.getStart() == null)
					        return 0;
					      return arg0.getStart().compareTo(arg1.getStart());
					}
					
				});
				String accessToken = db.getAccessToken(req.getUser().getID());
				filterService.filterSearch(journeys, req.getUser().getID(),accessToken);
			} catch (SQLException e) {
				ServerLogger.write("Error performing search:" +e.getMessage());
				return new JourneyResponse(type,ResponseStatus.FAILED,e.getMessage(),journeys);
			}
			return new JourneyResponse(type,status,journeys);
		case GET_JOURNEYS:
			try{
				journeys = db.getJourneys(request.getUser());
				ServerLogger.write("Before Journey Req");
				//ServerLogger.write(journeys.get(0).getDriver().getFullName());
				ServerLogger.write("After serverlogger");
				return new JourneyResponse(type, ResponseStatus.OK,journeys);
			}catch(SQLException e){
				logger.error("Error finding user's journeys for user " + request.getUser().getID(), e);
				return new JourneyResponse(type,ResponseStatus.FAILED,e.getMessage(),journeys);
			}
		case GET_JOURNEY:
			try {
				journeys = new ArrayList<Journey>();
				journeys.add(db.getJourney(((JourneyRequest) request).getJourney().getSerial()));
				return new JourneyResponse(type,ResponseStatus.OK,journeys);
			} catch (SQLException e1) {
				logger.error("Error retrieving journey.",e1);
				return new JourneyResponse(type,ResponseStatus.FAILED,e1.getMessage(),null);
			}
		case SEND_NOTIFICATION:
			Notification note = ((NotificationRequest) request).getNotification(); 
			try{
				handleNotification(request.getUser(),note);
				if(note.getType() != NotificationType.HITCHHIKER_ACCEPTS_DRIVER_CANCEL){
					db.addNotification(note);
				}
				return new UserResponse(type, ResponseStatus.OK);
			}catch(SQLException e){
				logger.error("Error processing notification.",e);
				return new UserResponse(type,ResponseStatus.FAILED,e.getMessage());
			}
		case PULL_NOTIFICATIONS:
			try {
				List<Notification> notes = db.getNotifications(request.getUser());
				return new NotificationResponse(RequestType.PULL_NOTIFICATIONS,ResponseStatus.OK,notes);
			} catch (SQLException e) {
				logger.error("Error retrieving notifications.",e);
				return new NotificationResponse(type,ResponseStatus.FAILED,e.getMessage(),null);
			}
		case MARK_NOTIFICATION_READ:
			try{
				note = ((NotificationRequest) request).getNotification();
				db.setNotificationRead(note);
				return new UserResponse(type,ResponseStatus.OK);
			}catch(SQLException e){
				logger.error("Error marking notification read.", e);
				return new UserResponse(type,ResponseStatus.FAILED,e.getMessage());
			}
		case GET_PREFERENCE:
			ServerLogger.write("Before prefReq");
			//TripPreferences prefReq = ((PreferenceRequest)request).getPreference();
			
			//db.getPreference(prefReq.getPrefId());
			
			try {
				TripPreferences preference;
				preference = db.getPreference(request.getUser().getID());
				return new PreferenceResponse(type, status, preference);
			} catch (SQLException e) {
				ServerLogger.write("SQLERROR: " + e.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e.getMessage());
			}
		case CREATE_PREFERENCE:
			TripPreferences prefReq2 = ((PreferenceRequest)request).getPreference();
			//ServerLogger.write(prefReq2.getSeatsAvailable().toString());
			try {
				db.createPreference(prefReq2);
				return new PreferenceResponse(type, status, prefReq2);
			} catch (SQLException e) {
				ServerLogger.write("SQLERROR: " + e.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e.getMessage());
			}
		case UPDATE_PREFERENCE:
			TripPreferences prefReq3 = ((PreferenceRequest)request).getPreference();
			try {
				db.updatePreference(prefReq3);
				return new PreferenceResponse(type, status, prefReq3);
			} catch (SQLException e) {
				ServerLogger.write("SQLERROR: " + e.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e.getMessage());
			}
		case GET_CAR:
			Car carReq = ((CarRequest)request).getCar();
			try
			{
				Car car = db.getCar(carReq.getCarId());
				return new CarResponse(type, status, car);
			} catch (SQLException e)
			{
				ServerLogger.write("SQLERROR: " + e.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e.getMessage());
			}
		case CREATE_CAR:
			Car carReq2 = ((CarRequest)request).getCar();
			User carUserReq = ((CarRequest)request).getUser();
			try
			{
				int carid = db.createCar(carReq2,carUserReq);
				carReq2.setCarId(carid);
				return new CarResponse(type, status, carReq2);
			} catch (SQLException e)
			{
				ServerLogger.write("SQLERROR: " + e.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e.getMessage());
			}
		case UPDATE_CAR:
			Car carReq3 = ((CarRequest)request).getCar();
			try
			{
				db.updateCar(carReq3);
				return new CarResponse(type, status, carReq3);
			} catch (SQLException e)
			{
				ServerLogger.write("SQLERROR: " + e.getMessage());
				return new UserResponse(type, ResponseStatus.FAILED, e.getMessage());
			}
			
		default:
			status = ResponseStatus.UNKNOWN;
			return new UserResponse(type, status);
		}

	}

	/** Attempt to handle the business logic of a given Notification.  */
	private void handleNotification(User sender, Notification notification) throws SQLException{
		int serial = notification.getJourneySerial();
		switch(notification.getType()){
		case REQUEST_ACCEPT:
			if(!sender.getID().equals(db.getJourneyDriverID(serial))){
				throw new SQLException("Unauthorized acceptance in journey " + serial + " from user " + sender.getID());
			}
			if(!checkForRequest(notification)) {
				throw new SQLException("Attempt to accept nonexistent hitchhiker request or accept previously rejected request.");
			}
			ServerLogger.write("Before addHitchhiker");
			db.addHitchhiker(notification.getRecipientID(),serial);
			ServerLogger.write("Before incrementSeats");
			db.incrementSeats(serial, -1);
			ServerLogger.write("After incrementSeats");
			break;
		case REQUEST_REJECT:
			if(checkForRequest(notification)) throw new SQLException("Attempted to reject previously accepted request.");
			break;
		case HITCHHIKER_CANCEL:
			//if(!sender.getID().equals(db.getHitchhikerID(serial))) return;
			db.removeHitchhiker(sender.getID(),serial);
			db.incrementSeats(serial, 1);
			break;
		case HITCHHIKER_ACCEPTS_DRIVER_CANCEL:
			//if(!sender.getID().equals(db.getHitchhikerID(serial))) throw new SQLException("Unauthorized acceptance in journey " + serial + " from user " + sender.getID());
			//if(!checkForRequest(notification)) {
				//throw new SQLException("Attempt to accept nonexistent driver cancel.");
			//}
			db.deleteJourneyWithoutCheck(new Journey(serial));
			break;
		case MESSAGE:
			break;
		default:
			return;
		}
	}

	/** Checks that a given acceptance notification corresponds 
	 * to an existing request notification, to prevent exploits from clients.*/
	private boolean checkForRequest(Notification note) throws SQLException {
		NotificationType expectedType = null;
		switch(note.getType()){
		case REQUEST_ACCEPT:
			if(db.checkForNotifications(note.getRecipientID(),note.getSenderID(),note.getJourneySerial(),NotificationType.REQUEST_REJECT)) return false;
			expectedType = NotificationType.HITCHHIKER_REQUEST;
			break;
		case HITCHHIKER_ACCEPTS_DRIVER_CANCEL:
			expectedType = NotificationType.DRIVER_CANCEL;
			break;
		case REQUEST_REJECT:
			return db.checkForNotifications(note.getRecipientID(),note.getSenderID(),note.getJourneySerial(),NotificationType.REQUEST_ACCEPT);
		default:
			return true;
		}
		return db.checkForNotifications(note.getRecipientID(),note.getSenderID(),note.getJourneySerial(),expectedType);
	}

}
