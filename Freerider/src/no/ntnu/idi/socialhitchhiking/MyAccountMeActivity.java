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

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;

import no.ntnu.idi.freerider.model.Car;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.CarRequest;
import no.ntnu.idi.freerider.protocol.CarResponse;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.UserRequest;
import no.ntnu.idi.freerider.protocol.UserResponse;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.map.GetImage;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Class that contains functionality for the "Me" tab in "My Account".
 * @author Kristoffer Aulie
 *
 */
public class MyAccountMeActivity extends SocialHitchhikingActivity {

	private ImageView picture;
	private ImageView gender;
	private EditText age;
	private EditText phone;
	private EditText aboutMe;
	private TextView name;
	private TextView recommendations;
	private User user;
	
	private String ageString;
	private String aboutMeString;
	private String phoneString;
	
	private boolean ageChanged;
	private boolean aboutMeChanged;
	private boolean phoneChanged;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Setting the loading layout
			setContentView(R.layout.main_loading);
			// Getting the user from the database
			user = getApp().getUser();
			new UserLoader(this).execute();
			// Adding image of the driver
			// Execute the Asynctask: Get image from url and add it to the ImageView
			new GetImage(picture, this).execute(user.getPictureURL());
		}catch(NullPointerException e){
			Toast.makeText(this, "A server error occured.", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onStop(){
		ageChanged = false;
		aboutMeChanged = false;
		phoneChanged = false;
		
		if(phoneString == null){
			phoneString = "";
		}
		if(aboutMeString == null){
			aboutMeString = "";
		}
		
		// Checking if a new age is set
		if(!ageString.equals(age.getText().toString())){
			ageChanged = true;
		}
		// Checking if a new phone number is set
		if(!phoneString.equals(phone.getText().toString())){
			phoneChanged = true;
		}
		// Checking if a new "about me" is set
		if(!aboutMeString.equals(aboutMe.getText().toString())){
			aboutMeChanged = true;
		}
		// Setting the age
		boolean isEmpty = false;
		if(age.getText().toString().length() < 1){
			isEmpty = true;
		}
		
		if(isEmpty){
			ageString = "0";
		}else{
			ageString = age.getText().toString();
		}
		// Setting the phone number
		isEmpty = false;
		
		if(phone.getText().toString().length() < 1){
			isEmpty = true;
		}
		
		if(isEmpty){
			phoneString = "";
		}
		else{
			phoneString = phone.getText().toString();
		}
		// Setting the About me
		aboutMeString = aboutMe.getText().toString();
		
		// Make changes in the user object
		if(ageChanged){
			user.setAge(Integer.parseInt(ageString));
		}
		if(phoneChanged){
			user.setPhone(phoneString);
		}
		if(aboutMeChanged){
			user.setAbout(aboutMeString);
		}
		// Add the changes to the user object to the database
		Request userReq = new UserRequest(RequestType.UPDATE_USER, user);
		try {
			RequestTask.sendRequest(userReq, getApp());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStop();
	}
	/**
	 * Initializing the user from a {@link UserResponse}
	 * @param res
	 */
	public void initUser(UserResponse res){
		this.user = res.getUser();
	}
	/**
	 * Displays the user info in the layout.
	 * @param result
	 */
	public void showProfile(Bitmap result){
		setContentView(R.layout.my_account_me_);
		// Initializing views
		gender = (ImageView) findViewById(R.id.gender);
		picture = (ImageView)findViewById(R.id.meImage);
		name = (TextView)findViewById(R.id.meName);
		age = (EditText)findViewById(R.id.meAge);
		phone = (EditText)findViewById(R.id.mePhone);
		aboutMe = (EditText)findViewById(R.id.meAboutMe);
		recommendations = (TextView)findViewById(R.id.recommendations);
		
		// Adding the picture of the user
		picture.setImageBitmap(result);
		
		// Adding the name of the user
		name.setText(user.getFullName());
		
		// Adding the age of the user
		if(user.getAge() == 0){
			ageString = "";
		}else{
			ageString = Integer.toString(user.getAge());
		}
		age.setText(ageString);
		
		// Adding the phone number of the user
		phoneString = user.getPhone();
		phone.setText(phoneString);
		
		// Adding the About Me of the user
		aboutMeString = user.getAbout();
		aboutMe.setText(aboutMeString);
		// Adding Gender to the user
	    if(user.getGender().equals("m")){
	    	Drawable male = getResources().getDrawable(R.drawable.male);
	    	gender.setImageDrawable(male);
	    }
	    else if(user.getGender().equals("f")){
	    	Drawable female = getResources().getDrawable(R.drawable.female);
	    	gender.setImageDrawable(female);
	    }
	    // Adding recommendations to the user
	    recommendations.setText("Recommendations: " + (int)user.getRating());
	}
}
/**
 * Class that loads the user information from the server in the background.
 * @author Kristoffer Aulie
 *
 */
class UserLoader extends AsyncTask<Void, User, UserResponse>{
	MyAccountMeActivity activity;
	
	public UserLoader(Activity activity){
		this.activity = (MyAccountMeActivity) activity;
	}
	/**
	 * Getting the user information from the database.
	 * @param params
	 * @return
	 */
	protected UserResponse doInBackground(Void... params) {
		UserResponse res = null;
    	try {
			Request req = new UserRequest(RequestType.GET_USER, activity.getApp().getUser());
			res = (UserResponse) RequestTask.sendRequest(req,activity.getApp());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;
	}

	@Override
	protected void onPostExecute(UserResponse result) {
		activity.initUser(result);
	}
}
