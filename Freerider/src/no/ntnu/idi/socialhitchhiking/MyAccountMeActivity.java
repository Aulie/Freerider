package no.ntnu.idi.socialhitchhiking;

import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.socialhitchhiking.map.GetImage;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAccountMeActivity extends SocialHitchhikingActivity {

	private ImageView picture;
	private ImageView gender;
	private EditText age;
	private EditText phone;
	private EditText aboutMe;
	private TextView name;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Getting the user
		user = getApp().getUser();
		// Setting the loading layout
		setContentView(R.layout.main_loading);
		
		// Adding image of the driver
		// Execute the Asynctask: Get image from url and add it to the ImageView
		new GetImage(picture, this).execute(user.getPictureURL());
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
		
		// Adding the picture of the user
		picture.setImageBitmap(result);
		
		// Adding the name of the user
		name.setText(user.getFullName());
		
		// Adding the age of the user
		age.setText(Integer.toString(user.getAge()));
		
		// Adding the phone number of the user
		phone.setText(user.getPhone());
		
		// Adding the About Me of the user
		aboutMe.setText(user.getAbout());
		
		//Adding Gender to the driver
	    if(user.getGender().equals("m")){
	    	Drawable male = getResources().getDrawable(R.drawable.male);
	    	gender.setImageDrawable(male);
	    }
	    else if(user.getGender().equals("f")){
	    	Drawable female = getResources().getDrawable(R.drawable.female);
	    	gender.setImageDrawable(female);
	    }
	}
	
}
