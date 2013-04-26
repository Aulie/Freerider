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

package no.ntnu.idi.socialhitchhiking;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.UserRequest;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.map.MapRoute;
import no.ntnu.idi.socialhitchhiking.service.AlarmService;
import no.ntnu.idi.socialhitchhiking.service.JourneyReminder;
import no.ntnu.idi.socialhitchhiking.utility.SettingsManager;

import org.apache.http.client.ClientProtocolException;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

public class SocialHitchhikingApplication extends Application{ 
	private User user;
	private SettingsManager settings;
	private List<Route> routes;
	private List<Journey> journeys;
	private Journey selectedJourney;
	private Location journeyPickupPoint;
	private Location journeyDropoffPoint;
	private Route oldEditRoute;
	private Route selectedRoute;
	private MapRoute selectedMapRoute;
	private AlarmManager am;
	private PendingIntent service,journeyReminder;
	private Main m;
	private HashMap<String, Boolean> keyMap;
	private List<Notification> notifications;
	private Notification selectedNotification;
	private PropertyChangeSupport props;
	private int notifs;
	private boolean newNotifications;
	public static final String ACCESS_TOKEN = "access_token3253623261";
	public static final String NOTIFICATION = "notification123412515";

	@Override
	public void onCreate() {
		super.onCreate();  
		notifs = 0;
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		keyMap = new HashMap<String, Boolean>();
		keyMap.put("alarmService", false);
		keyMap.put("main",false);
		keyMap.put("inbox",false);
		props = new PropertyChangeSupport(this);
		newNotifications = false;
		settings = new SettingsManager(this);
		startJourneyReminder();
	}
	public void reset(){
		user = null;
		routes = null;
		journeyDropoffPoint = null;
		journeyPickupPoint = null;
		journeys = null;
		selectedJourney = null;
		selectedMapRoute = null;
		selectedNotification = null;
		selectedRoute = null;
		killService();
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notif) {
		if(notif != null){
			notifications = notif;
		}
	}
	public boolean isThereNewNotifications(){
		return newNotifications;
	}
	public void setIsThereNewNotifications(boolean state){
		newNotifications = state;
	}
	public int getNotifs(){
		return notifs;
	}
	public int getNewNotif(){
		int old = notifs;
		if(notifications != null){
			notifs = 0;
			for(Notification n : notifications){
				if(!n.isRead())notifs++;
			}
			if(notifs > old)newNotifications = true;
			return notifs;
		}
		return 0;
	}
	/**
	 * Checks if the {@link HashMap} contains the given key.
	 * returns false if the key is not defined in the hashmap,
	 * else it returns the keys value
	 * 
	 * @param key - The key String
	 */
	public boolean isKey(String key){
		try {
			return keyMap.get(key);
		} catch (NullPointerException e) {
			return false;
		}
	}
	public void setKeyState(String key,boolean state){
		keyMap.put(key, state);
	}
	public Journey getSelectedJourney() {
		return selectedJourney;
	}
	public void setSelectedJourney(Journey selectedJourney) {
		this.selectedJourney = selectedJourney;
	}
	public void setJourneyPickupPoint(Location loc){
		journeyPickupPoint = loc;
	}
	public Location getJourneyPickupPoint(){
		return journeyPickupPoint;
	}
	public void setJourneyDropoffPoint(Location loc){
		journeyDropoffPoint = loc;
	}
	public Location getJourneyDropoffPoint(){
		return journeyDropoffPoint;
	}
	public MapRoute getSelectedMapRoute() {
		return selectedMapRoute;
	}
	public void setSelectedMapRoute(MapRoute selectedMapRoute) {
		this.selectedMapRoute = selectedMapRoute;
	}
	public Route getSelectedRoute() {
		return selectedRoute;
	}
	public void setSelectedRoute(Route selectedRoute) {
		this.selectedRoute = selectedRoute;
	}
	public void setMain(Main m){
		this.m = m;
	}
	public Main getMain(){
		return m;
	}
	public void setService(PendingIntent p){
		service = p;
	}
	public List<Route> getRoutes(){
		return routes;

	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public List<Journey> getJourneys(){
		return journeys;
	}

	public User getUser(){
		return user;
	}
	public void setUser(User user){
		this.user = user;
	}
	public SettingsManager getSettings(){
		return settings;
	}
	public void addPropertyListener(PropertyChangeListener l){
		props.addPropertyChangeListener(l);
	}
	public void removePropertyListener(PropertyChangeListener l){
		props.removePropertyChangeListener(l);
	}
	public void fireAccesTokenChanged(){
		PropertyChangeEvent ev = new PropertyChangeEvent(this, ACCESS_TOKEN, null, null);
		props.firePropertyChange(ev);
	}
	public void fireNotificationChanged(Notification n){
		PropertyChangeEvent ev = new PropertyChangeEvent(this, NOTIFICATION, null, n);
		props.firePropertyChange(ev);
	}
	/**
	 * Starts a service that will poll the server for updates at a regular time interval.
	 * It will start 10 sec after the app is started, then poll according to the update interval 
	 * chosen in settings. 
	 */
	public void startService(){		
		Intent intent = new Intent(this, AlarmService.class);
		service = PendingIntent.getBroadcast(this, 0 , intent, 0);
		long firstTime = SystemClock.elapsedRealtime();
		firstTime += 10*1000;
		am.cancel(service);
		
		int interval = Integer.valueOf(getSettings().getUpdateInterval());
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, interval *1000, service);
		keyMap.put("alarmService", true);
	}
	/**
	 * Starts a service that checks if there is a scheduled Journey in less than one hour.
	 * It will start one minute after the app is started, then poll every ten minutes.
	 */
	public void startJourneyReminder(){		
		Intent intent = new Intent(this, JourneyReminder.class);
		journeyReminder = PendingIntent.getBroadcast(this, 0 , intent, 0);
		am.cancel(journeyReminder);
	
		int tenMinutes = 600000;
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, tenMinutes/10, tenMinutes, journeyReminder);
	}
	public List<Journey> sendJourneysRequest() throws InterruptedException, ExecutionException{
		if(user != null && user.getID() != ""){
			Request req = new UserRequest(RequestType.GET_JOURNEYS, getUser());
			JourneyResponse res = null;
			try {
				res = (JourneyResponse) RequestTask.sendRequest(req,this);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (res == null){
				return null;
			}
			journeys = res.getJourneys();
			if(journeys != null && journeys.size() != 0){
				Calendar now = Calendar.getInstance();
				now.add(Calendar.DAY_OF_MONTH, -1);
				List<Journey> temp = new ArrayList<Journey>();
				for (Journey j : journeys) {
					if(j.getStart().before(now))temp.add(j);
				}
				journeys.removeAll(temp);
			}
			return journeys;
		}
		return null;
	}

	public void killService() {
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(service);
	}

	public void setSelectedNotification(Notification selectedNotification) {
		this.selectedNotification = selectedNotification;
	}

	public Notification getSelectedNotification() {
		return selectedNotification;
	}
	public void setOldEditRoute(Route oldEditRoute) {
		this.oldEditRoute = oldEditRoute;
	}
	public Route getOldEditRoute() {
		return oldEditRoute;
	}

}
