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
package no.ntnu.idi.socialhitchhiking.inbox;

import java.io.IOException;

import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.NotificationType;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.map.MapActivityAbstract;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class NotificationHandler{
	private static Inbox in;
	private static Notification n;
	private static SocialHitchhikingApplication app;
	
	/**
	 * Static method to be called if the calling Activity is a {@link MapActivityAbstract}.
	 * 
	 * 
	 * @param type - {@link NotificationType} 
	 * @param com
	 * @return
	 */
	public static boolean handleMap(NotificationType type,String com){
		return createRequest(true, type, com);
	}
	/**
	 * Static method to handle a {@link Notification} when it's clicked. 
	 * Does nothing if the Notification is already read. Uses a
	 * switch based on the {@link NotificationType), to handle unread 
	 * Notifications.
	 * 
	 * @param nf - Notification to be handled
	 * @param ap - Pointer to the Application, which will be used to get the current user etc.
	 * @param i - The calling Inbox-activity which dialogs will be created upon
	 */
	public static void handleNotification(Notification nf,SocialHitchhikingApplication ap,Inbox i){
		app = ap;
		n = nf;
		in = i;
		if(n.isRead()){
			createConfirmDialog("Inactive", "Notification is inactive");
		}
		else{
			switch (n.getType()) {
			case DRIVER_CANCEL:
				createMessageDialog(false,"Driver cancelled Journey", n.getSenderName()+" cancelled the Journey");
				break;
			case HITCHHIKER_ACCEPTS_DRIVER_CANCEL:
				createMessageDialog(true,"Hitchhiker acknowledged", n.getSenderName()+" accepts cancel");
				break;
			case HITCHHIKER_CANCEL:
				createMessageDialog(false,"Hitchhiker cancelled request", n.getSenderName()+" cancelled the request");
				break;
			case HITCHHIKER_REQUEST:
				createNotificationDialog();
				break;
			case REQUEST_ACCEPT:
				createMessageDialog(false,"Request accepted by driver", "Your request was accepted by "+n.getSenderName());
				break;
			case REQUEST_REJECT:
				createMessageDialog(false,"Request rejected by driver", "Your request was rejected by "+n.getSenderName());
				break;
			default:
				createMessageDialog(false,"Unknown", "Status unknown");
				break;
			}
		}
		
	}
	
	private static void createCommentForRequest(final NotificationType nt){
		final EditText input = new EditText(in);
		new AlertDialog.Builder(in).
		setTitle("Comment").
		setMessage("Write a comment").
		setView(input).
		setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				createRequest(false, nt, input.getText().toString());
			}
		}).show();
	}
	
	/**
	 * Creates a dialog, which asks the user whether they want to accept or reject
	 * the Hitchhiker. Creates an accept notification or a reject notification
	 * depending on the answer.
	 */
	private static void createNotificationDialog(){
		new AlertDialog.Builder(in).
		setTitle("Accept hitchhiker?").
		setMessage("Do you want to pick up "+n.getSenderName()).
		setPositiveButton("Accept", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				createCommentForRequest(NotificationType.REQUEST_ACCEPT);
				
			}
		}).
		setNeutralButton("Reject", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				createCommentForRequest(NotificationType.REQUEST_REJECT);
			}
		}).
		setNegativeButton("Show in map", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				in.showInMap(n);
			}
		}).
		show();
	}
	/**
	 * Creates a simple dialog to show a message when a Notification is clicked.
	 * 
	 * @param title - The title of the dialog that will be created.
	 * @param msg - The message of the dialog that will be created.
	 */
	private static void createMessageDialog(final boolean hitchhiker_cancel,String title,String msg){
		new AlertDialog.Builder(in).
		setTitle(title).
		setMessage(msg).
		setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(hitchhiker_cancel){
					createCommentForRequest(NotificationType.HITCHHIKER_ACCEPTS_DRIVER_CANCEL);
				}
				createMarkedAsReadRequest();
			}
		}).
		setNegativeButton("Cancel", null).
		show();
	}
	/**
	 * Creates a simple dialog to show a message when a Notification is clicked.
	 * 
	 * @param title - The title of the dialog that will be created.
	 * @param msg - The message of the dialog that will be created.
	 */
	private static void createConfirmDialog(String title,String msg){
		new AlertDialog.Builder(in).
		setTitle(title).
		setMessage(msg).
		setNeutralButton("OK", null).
		show();
	}
	/**
	 * Creates a {@link NotificationRequest} to mark a Notification as read,
	 * and sends it to the server. Creates a dialog that show if the request
	 * was successfully sent.
	 * 
	 */
	private static boolean createMarkedAsReadRequest() {
		NotificationRequest req = new NotificationRequest(RequestType.MARK_NOTIFICATION_READ,app.getUser(), n);
		if(sendNotificationRequest(req)){
			in.setNotificationRead(n);
			return true;
		}
		return false;
	}
	/**
	 * Creates a {@link NotificationRequest} and sends it to the server.
	 * Creates a dialog that show if the request was successfully sent.
	 * 
	 * @param accept
	 */
	private static boolean createRequest(boolean mapmode,NotificationType type,String com) {
		Notification notif = new Notification(app.getUser().getID(), n.getSenderID(), "",com, n.getJourneySerial(), type);
		NotificationRequest req = new NotificationRequest(app.getUser(), notif);
		if(sendNotificationRequest(req)){
			if(!mapmode)createConfirmDialog("Confirmed", "Successfully sent reply");
			return createMarkedAsReadRequest();
		}
		else {
			if(!mapmode)createConfirmDialog("ERROR", "Reply was not sent");
			return false;
		}
	}
	/**
	 * Sends a notification request.
	 * 
	 * @param req - The request to be sent. 
	 * @return false if something went wrong, true if everything went well.
	 */
	private static boolean sendNotificationRequest(Request req){
		Response res;
		try {
			res = RequestTask.sendRequest(req,app);
			boolean succeded = res.getStatus() == ResponseStatus.OK;
			return succeded;
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return false;
	}
}
