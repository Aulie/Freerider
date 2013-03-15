package no.ntnu.idi.socialhitchhiking;

import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.socialhitchhiking.map.GetImage;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAccountMeActivity extends SocialHitchhikingActivity {

	private ImageView picture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_me_);
	

	// Adding image of the driver
	User user = getApp().getUser();
	
	picture = (ImageView)findViewById(R.id.meImage);
	
	// Create an object for subclass of AsyncTask
    GetImage task = new GetImage(picture);
    // Execute the task: Get image from url and add it to the ImageView
    task.execute(user.getPictureURL());
	
	// Adding the name of the driver
	((TextView)findViewById(R.id.meName)).setText(user.getFullName());

	//Adding Gender to the driver

			user.setGender("Female");
			ImageView iv_image;
		    iv_image = (ImageView) findViewById(R.id.gender);

			    if (user.getGender().equals("Female")){
			    
			    Drawable male = getResources().getDrawable(R.drawable.male);
			    iv_image.setImageDrawable(male);
			    }

			    else{
			    Drawable female = getResources().getDrawable(R.drawable.female);
			    iv_image.setImageDrawable(female);
			    }
	}
	
}
