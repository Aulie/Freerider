package no.ntnu.idi.socialhitchhiking.utility;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;
import java.util.Calendar;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.socialhitchhiking.Main;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.journey.TripOptions;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class ShareOnFacebook extends SocialHitchhikingActivity{

	private static final String APP_ID = "321654017885450";
	private static final String[] PERMISSIONS = new String[] {"read_stream","publish_stream"};

	private static final String TOKEN = "access_token";
        private static final String EXPIRES = "expires_in";
        private static final String KEY = "facebook-credentials";

	private Facebook facebook;
	private String messageToPost;

	public boolean saveCredentials(Facebook facebook) {
        	Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        	editor.putString(TOKEN, facebook.getAccessToken());
        	editor.putLong(EXPIRES, facebook.getAccessExpires());
        	return editor.commit();
    	}

    	public boolean restoreCredentials(Facebook facebook) {
        	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        	facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
        	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
        	return facebook.isSessionValid();
    	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Route route = getApp().getSelectedRoute();
		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.facebook_dialog);

		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		if (facebookMessage == null){
			facebookMessage = "I have created a new ride on FreeRider";
		}
		messageToPost = facebookMessage;
	}

	public void doNotShare(View button){
		finish();
	}
	public void share(View button){
		if (! facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			postToWall(messageToPost);
		}
	}

	public void loginAndPostToWall(){
		 facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
	}

	public void postToWall(String message){
	    
		Bundle postParams = new Bundle();
//				parameters.putString("caption", "FreeRider!" + "\n\n"+ "https://www.facebook.com");
//				parameters.putString("picture", "http://www.veryicon.com/icon/png/Business/Business/Cars.png");
//                parameters.putString("message", message);
//                parameters.putString("description", "topic share");
//                parameters.putString("link","www.google.com");
////                Intent inte = getIntent();
//                parameters.putString("privacy", "EVERYONE");
		postParams.putString("message", message);
		postParams.putString("caption", "https://maps.google.com/maps?saddr="+getApp().getSelectedRoute().getStartAddress()+"&daddr="+getApp().getSelectedRoute().getEndAddress());
		String date = "Date: "+ getApp().getSelectedJourney().getStart().get(Calendar.DAY_OF_MONTH)+
				"/"+getApp().getSelectedJourney().getStart().get(Calendar.MONTH)+
				"/"+getApp().getSelectedJourney().getStart().get(Calendar.YEAR);
		String time = "Start time: "+ getApp().getSelectedJourney().getStart().get(Calendar.HOUR)+":"+getApp().getSelectedJourney().getStart().get(Calendar.MINUTE);
		String seats = "Seats available: "+ getApp().getSelectedJourney().getTripPreferences().getSeatsAvailable();
//		String extras = "Extras: "+ getApp().getSelectedJourney().getTripPreferences().toString();
		BitSet sExtras = getApp().getSelectedJourney().getTripPreferences().getExtras();
		String extras = "Extras: ";
		String[] items = {"Music", "Animals", "Breaks", "Talking", "Smoking"};
    	for(int i=0 ; i<sExtras.length() ; i++){
    		if(sExtras.get(i)){
    			extras=extras+items[i]+" ";
    		}
    	}
		
		postParams.putString("description", date+"\n"+time+"\n"+seats+"\n"+extras);
//		postParams.putString("actions", "[{'name':'Test a simple Graph API call!','link':'https://developers.facebook.com/tools/explorer?method=GET&path=me'}]");
		postParams.putString("type", "photo");
		postParams.putString("link", "https://maps.google.com/maps?saddr="+getApp().getSelectedRoute().getStartAddress()+"&daddr="+getApp().getSelectedRoute().getEndAddress());
		postParams.putString("picture", "http://www.veryicon.com/icon/png/Business/Business/Cars.png");

                try {
        	        facebook.request("me");
			String response = facebook.request("me/feed", postParams, "POST");
		
//		Request request = new Request(Session.getActiveSession(), "me/feed", postParams, HttpMethod.POST);
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") ||
			        response.equals("false")) {
				showToast("Blank response.");
			}
			else {
				showToast("Trip posted to your facebook wall!");
				Intent intent = new Intent(ShareOnFacebook.this, Main.class);
				startActivity(intent);
			}
			finish();
		} catch (Exception e) {
			showToast("Failed to post to wall!");
			e.printStackTrace();
			finish();
		}
	}

	class LoginDialogListener implements DialogListener {
	    public void onComplete(Bundle values) {
	    	saveCredentials(facebook);
	    	if (messageToPost != null){
			postToWall(messageToPost);
		}
	    }
	    public void onFacebookError(FacebookError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onError(DialogError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onCancel() {
	    	showToast("Authentication with Facebook cancelled!");
	        finish();
	        Intent intent = new Intent(ShareOnFacebook.this, Main.class);
			startActivity(intent);
	    }
	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
