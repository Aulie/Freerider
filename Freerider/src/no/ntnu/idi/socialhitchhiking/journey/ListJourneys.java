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

package no.ntnu.idi.socialhitchhiking.journey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.NotificationType;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.NotificationResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.UserRequest;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.map.MapRoute;
import no.ntnu.idi.socialhitchhiking.utility.JourneyAdapter;
import no.ntnu.idi.socialhitchhiking.utility.SectionedListViewAdapter;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListJourneys extends SocialHitchhikingActivity{
	private ListView listview;
	private List<Journey> journeys;
	private Calendar now, hr24, hr72,in14Days,nextMonth;
	private boolean owned;
	public Dialog optionsDialog;
	
	public void showMain(List<Journey> journeys){
		setContentView(R.layout.my_rides);
		listview = (ListView)findViewById(R.id.journey_view_list);

		if(journeys.size()==0){
			Toast.makeText(this, "You have no active rides", Toast.LENGTH_LONG).show();
		}
		List<Journey> tempJourneys = new ArrayList<Journey>();
		for(int i = 0; i < journeys.size(); i++) {
			if(journeys.get(i).getHitchhikers().size() > 0){
				boolean added = false;
				for(int j = 0; j<journeys.get(i).getHitchhikers().size(); j++)
				{
					if(journeys.get(i).getHitchhikers().get(j).getID().equals(getApp().getUser().getID())){
						if(!owned) {
							tempJourneys.add(journeys.get(i));
						}
					}
					else if(journeys.get(i).getDriver().getID().equals(getApp().getUser().getID())){
						if(owned && !added){
							tempJourneys.add(journeys.get(i));
							added = true;
						}
					}
				}
				
			}
			else
			{
				if(owned) {
					tempJourneys.add(journeys.get(i));
				}
			}
			
		}
		this.journeys = tempJourneys;
		initCalendars();
		
		initAdapter(adapter, this.journeys);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parentView, View childView, final int pos, long id) {
				Journey j = (Journey) adapter.getItem(pos);
				cancelJourney(j);
				return false;
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, final int pos, long id) {
				Journey j = (Journey) adapter.getItem(pos);
				cancelJourney(j);
			}
			
		});
		listview.setAdapter(adapter);
	}
	
	private SectionedListViewAdapter adapter = new SectionedListViewAdapter() {
		@Override
		protected View getHeaderView(String caption, int index, View convertView,
				ViewGroup parent) {
			TextView result = (TextView) convertView;

			if (convertView == null) {
				result = (TextView) getLayoutInflater().inflate(
						R.layout.listview_sectioned, null);
			}

			result.setText(caption);
			result.setTextColor(Color.BLACK);
			result.setBackgroundColor(Color.rgb(170, 170, 170));

			return (result);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		owned = intent.getBooleanExtra("owned", true);
		setContentView(R.layout.main_loading);
		new Loader(this).execute();
		
		
	}
	private void initCalendars(){
		now = Calendar.getInstance();

		hr24 = Calendar.getInstance();
		hr24.add(Calendar.DATE, 1);

		hr72 = Calendar.getInstance();
		hr72.add(Calendar.DATE, 3);

		in14Days = Calendar.getInstance();
		in14Days.add(Calendar.WEEK_OF_YEAR, 2);

		nextMonth = Calendar.getInstance();
		nextMonth.add(Calendar.MONTH, 1);
	}
	private void initAdapter(SectionedListViewAdapter adp,List<Journey> list){
		if(adapter != null){
			adapter.reset();
		}
		List<Journey> temp = new ArrayList<Journey>(list);
		Collections.copy(temp, list);
		List<Journey> result = new ArrayList<Journey>();
		result = getOngoing(temp);
		if(result.size() != 0)adp.addSection("Ongoing", new JourneyAdapter(this, 0, result));
		
		result = getNext24(temp);
		if(result.size() != 0)adp.addSection("Next 24 hours", new JourneyAdapter(this, 0, result));

		result = getNext72(temp);
		if(result.size() != 0)adp.addSection("Next 72 hours", new JourneyAdapter(this, 0, result));

		result = getNext14Days(temp);
		if(result.size() != 0)adp.addSection("Next 14 Days", new JourneyAdapter(this, 0, result));

		result = getThisMonth(temp);
		if(result.size() != 0)adp.addSection("Next month", new JourneyAdapter(this, 0, result));
		
		if(temp.size() != 0 && list.size() != 0){
			adp.addSection("Later", new JourneyAdapter(this, 0, temp));
			adp.addSection("", new JourneyAdapter(this, 0,new ArrayList<Journey>()));
		}
		else if(list.size() != 0)adp.addSection("", new JourneyAdapter(this, 0,new ArrayList<Journey>()));
		
	}
	private List<Journey> getNext24(List<Journey> list){
		List<Journey> no = new ArrayList<Journey>();

		for(Journey j : list){
			if(j.getStart().after(now) && j.getStart().before(hr24)){
				no.add(j);
			}
		}
		list.removeAll(no);

		return no;
	}
	
	
	private List<Journey> getNext72(List<Journey> list){
		List<Journey> no = new ArrayList<Journey>();

		for(Journey j : list){
			if(j.getStart().after(now) && j.getStart().before(hr72)){
				no.add(j);
			}
		}
		list.removeAll(no);

		return no;
	}
	private List<Journey> getOngoing(List<Journey> list){
		List<Journey> no = new ArrayList<Journey>();
		for(Journey j : list){
			if(j.getStart().before(now)){
				no.add(j);
			}
		}
		list.removeAll(no);
		return no;
	}
	private List<Journey> getNext14Days(List<Journey> list){
		List<Journey> no = new ArrayList<Journey>();


		for(Journey j : list){
			if(j.getStart().after(now) && j.getStart().before(in14Days)){
				no.add(j);
			}
		}
		list.removeAll(no);

		return no;
	}
	private List<Journey> getThisMonth(List<Journey> list){
		List<Journey> no = new ArrayList<Journey>();


		for(Journey j : list){
			if(j.getStart().after(now) && j.getStart().before(nextMonth)){
				no.add(j);
			}
		}
		list.removeAll(no);

		return no;
	}

	private void cancelJourney(final Journey j){
		optionsDialog = new Dialog(ListJourneys.this);
		optionsDialog.setTitle("Ride Options");
		
		optionsDialog.setContentView(R.layout.options_layout);
		
		ImageView leaveBtn = (ImageView)optionsDialog.findViewById(R.id.leaveBtn);
		ImageView showInMapBtn = (ImageView)optionsDialog.findViewById(R.id.showBtn);
		
		leaveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder ad = new AlertDialog.Builder(ListJourneys.this);  
				if(j.getDriver().getFullName().equals(getApp().getUser().getFullName())){
					ad.setMessage("Do you want to cancel this ride?");
				} else{
					ad.setMessage("Do you want to leave this ride?");
				}
				ad.setTitle("Confirm");
				
				ad.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						handleJourney(j);
						optionsDialog.dismiss();
					}
				  });
				ad.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
					}
				  });
				ad.show();
			}
		});
		
		showInMapBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInMap(j);
			}
		});
		
		optionsDialog.show();
	}
	
	private List<Notification> getNotifications(User user){
		List<Notification> notifications = null;
		UserRequest req = new UserRequest(RequestType.PULL_NOTIFICATIONS, user);
		Response response = null;

		try {
			response = RequestTask.sendRequest(req, getApp());
			NotificationResponse notif = null;
			if(response instanceof NotificationResponse && response.getStatus() == ResponseStatus.OK){
				notif = (NotificationResponse) response;
				notifications = notif.getNotifications();
			}
		} catch (ClientProtocolException e) {
			
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notifications;
	}
	private void showInMap(Journey j) {
		List<Notification> notifDriver = getNotifications(j.getDriver());
		Location drop = null;
		Location pick = null;
		boolean accepted = false;
		boolean rejected = false;
		if(notifDriver != null){
			for (Notification not : notifDriver) {
				if(not.getJourneySerial() == j.getSerial()){
					if(not.getStopPoint() != null) drop = not.getStopPoint();
					if(not.getStartPoint() != null) pick = not.getStartPoint();

					if(not.getType().equals(NotificationType.REQUEST_ACCEPT)){
						accepted = true;
					}
					if(not.getType().equals(NotificationType.REQUEST_REJECT)){
						rejected = true;
					}
				}
			}
		}
		
		Route sr = j.getRoute();
		Intent intent = new Intent(this, no.ntnu.idi.socialhitchhiking.map.MapActivityJourney.class);
		intent.putExtra("journey", true);
		intent.putExtra("journeyAccepted", accepted);
		intent.putExtra("journeyRejected", rejected);
		MapRoute mr = new MapRoute(sr.getOwner(), sr.getName(), sr.getSerial(), sr.getMapPoints());
		
		getApp().setSelectedMapRoute(mr);
		getApp().setSelectedJourney(j);
		getApp().setJourneyDropoffPoint(drop);
		getApp().setJourneyPickupPoint(pick);
		
		startActivity(intent);
	}
	private void handleJourney(Journey j){
		if(j.getHitchhikers() != null){
			sendCancelJourney(j);
		}
		else sendDeleteJourney(j);
	}
	@Override
	protected void onResume() {
		super.onResume();
		adapter.updateDataSet();
		if(optionsDialog != null){
			optionsDialog.dismiss();
		}
		if(journeys != null){
			journeys.clear();
			setContentView(R.layout.main_loading);
			new Loader(this).execute();
		}
		
		
	}
	
	private void sendCancelJourney(Journey j) {
		NotificationType type;
		String id = getApp().getUser().getID();
		Notification notif;
		if(!j.getDriver().getID().equals(id) ){
			type = NotificationType.HITCHHIKER_CANCEL;
			notif = new Notification(id, j.getRoute().getOwner().getID(),"", "", j.getSerial(), type);
		}else{ 
			type = NotificationType.HITCHHIKER_ACCEPTS_DRIVER_CANCEL;
			notif = new Notification(id, id, "","", j.getSerial(), type);
		}
		NotificationRequest req = new NotificationRequest(getApp().getUser(), notif);
		boolean succeded = sendJourneyRequest(req);
		if(succeded){
			deleteJourneyFromList(j);
			createAlertDialog(this, succeded, "Ride", "cancelled", "");
		}
	}
	private boolean sendJourneyRequest(Request req){
		Response res;
		try {
			res = RequestTask.sendRequest(req,getApp());
			boolean succeded = res.getStatus() == ResponseStatus.OK;
			return succeded;
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private void deleteJourneyFromList(Journey j){
		adapter.removeObject(j);
		getApp().getJourneys().remove(j);
	}
	private void sendDeleteJourney(Journey j){
		JourneyRequest req = new JourneyRequest(RequestType.DELETE_JOURNEY, getApp().getUser(), j);

		boolean succeded = sendJourneyRequest(req);
		if(succeded){
			deleteJourneyFromList(j);
			createAlertDialog(this, succeded, "Ride", "cancelled", "");
		}
	}
	
}

class Loader extends AsyncTask<Void, Integer, List<Journey>>{
	
	ListJourneys activity;
	public Loader(ListJourneys activity){
		this.activity = (ListJourneys) activity;
	}
	
	protected List<Journey> doInBackground(Void... params) {
		List<Journey> journeys;
		try {
			journeys = activity.getApp().sendJourneysRequest();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			journeys = new ArrayList<Journey>();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			journeys = new ArrayList<Journey>();
		}
		return journeys;
	}

	@Override
	protected void onPostExecute(List<Journey> result) {
		activity.showMain(result);
		
	}

}
