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
package no.ntnu.idi.socialhitchhiking.service;

import java.io.IOException;
import java.net.MalformedURLException;

import no.ntnu.idi.freerider.protocol.NotificationResponse;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.UserRequest;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.SendNotification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * The code in onReceive() will run at regular intervals, as set in startService() in the SocialHitchhikingApplication class.
 * This is used to poll the server.
 * @author Pål
 * @author Christian Thurmann-Nielsen
 */
public class AlarmService extends BroadcastReceiver{
	SocialHitchhikingApplication app;

 
	@Override
	public void onReceive(Context context, Intent intent) {
		try{
		app = (SocialHitchhikingApplication) context.getApplicationContext();
		}catch(ClassCastException e){
			return;
		}
		if(app.getUser() == null)return;
		UserRequest req = new UserRequest(RequestType.PULL_NOTIFICATIONS,app.getUser());
		Response response = null;
		
		try {
			response = RequestTask.sendRequest(req,app);
			NotificationResponse notif = null;
			if(response instanceof NotificationResponse && response.getStatus() == ResponseStatus.OK){
				notif = (NotificationResponse) response;
				app.setNotifications(notif.getNotifications());
				int notifs = app.getNewNotif();
				if(notifs > 0 && app.isThereNewNotifications()){
					SendNotification.create(app, SendNotification.INBOX, "New Notifications", 
							"You have "+ notifs +" unread notifications in your inbox!", "Inbox");
					app.setIsThereNewNotifications(false);
				}
			}
			
			System.out.println(notif.getNotifications().size());
			//response.get?
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


