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
package no.ntnu.idi.socialhitchhiking.utility;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import no.ntnu.idi.freerider.protocol.LoginRequest;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.socialhitchhiking.Main;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Custom {@link Activity} class. Implements the proper menu behavior
 * needed by the application.
 * 
 * @extends Activity
 * 
 * @author Christian Thurmann-Nielsen
 *
 */
public abstract class SocialHitchhikingActivity extends Activity{

	private SocialHitchhikingApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (SocialHitchhikingApplication) getApplication();
	}
	@Override
	/**
	 * Creates a menu from the xml_menu.xml file.
	 * 
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		if(app.getUser() == null){
			return false;
		}
		else{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.xml_menu, menu);
			return super.onCreateOptionsMenu(menu);
		}
		
	}
	@Override
	/**
	 * Defines what happens when you click a {@link MenuItem}
	 */
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.menu_setting){
			startSettingsActvity();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}

	}
	private void startSettingsActvity() {
		initActivity(no.ntnu.idi.socialhitchhiking.utility.SettingsActivity.class);
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	public SocialHitchhikingApplication getApp(){
		return app;
	}
	/**
	 * Starts an activity via a class. This method prevents the application from starting
	 * activities that already exists. It checks whether the {@param cl} is the current
	 * activity. If it is, the activity should not be started, because we will then have 
	 * two running activities of the same class.
	 * 
	 * @param cl - The activity class that should be started.
	 */
	@SuppressWarnings("rawtypes")
	protected void initActivity(Class cl){
		if(cl != this.getClass()){
			Intent intent = new Intent(this, cl);
			startActivity(intent);
			finish();
		}
		
	}
	protected boolean sendLoginRequest(){
		LoginRequest req = new LoginRequest(getApp().getUser(), getApp().getSettings().getAccessToken());
		Response res;
		try {
			Log.d("FACEBOOK", "sendLoginRequest()");
			res = RequestTask.sendRequest(req,getApp());
			if(res.getStatus() == ResponseStatus.OK) 
				return true;
		} catch (ClientProtocolException e) {
			createAlertDialog(this, false, "Login", "accepted", "Server is probably down, or you're not connected to the internet.\nExiting");
			e.printStackTrace();
		} catch (IOException e) {
			createAlertDialog(this, false, "Login", "accepted", "Server is probably down, or you're not connected to the internet.\nExiting");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
/*	public void onBackPressed() {
		if(this.getClass() != Main.class){
			Intent main = new Intent(this, no.ntnu.idi.socialhitchhiking.Main.class);
			main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(main);
		}
		finish();
		
		super.onBackPressed();
	}
	*/
	protected void createAlertDialog(SocialHitchhikingActivity activity,boolean flag,final String type,String action,String msg){
		if(flag){
			new AlertDialog.Builder(this)
			.setTitle("Confirmed").setMessage(type+" "+action+"!")
			.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
		else{
			new AlertDialog.Builder(activity)
			.setTitle("ERROR").setMessage(type+ " not "+action+"!\n"+msg)
			.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					if(type.equals("Login")){
						finish();
					}
				}

			}).show();
		}
	}
}
