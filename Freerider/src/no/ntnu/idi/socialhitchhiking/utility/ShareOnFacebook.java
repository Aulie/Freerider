package no.ntnu.idi.socialhitchhiking.utility;

import java.util.BitSet;
import java.util.Calendar;

import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.socialhitchhiking.Main;
import no.ntnu.idi.socialhitchhiking.R;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	
	private String date,time,seats,extras;
	private Route currentRoute;

	@SuppressWarnings("deprecation")
	public boolean saveCredentials(Facebook facebook) {
        	Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        	editor.putString(TOKEN, facebook.getAccessToken());
        	editor.putLong(EXPIRES, facebook.getAccessExpires());
        	return editor.commit();
    	}

    	@SuppressWarnings("deprecation")
		public boolean restoreCredentials(Facebook facebook) {
        	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        	facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
        	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
        	return facebook.isSessionValid();
    	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentRoute = getApp().getSelectedRoute();
		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.facebook_dialog);
		
		//Intitialize TripOption string values
		date = "Date: "+ getApp().getSelectedJourney().getStart().get(Calendar.DAY_OF_MONTH)+
				"/"+getApp().getSelectedJourney().getStart().get(Calendar.MONTH)+
				"/"+getApp().getSelectedJourney().getStart().get(Calendar.YEAR);
		time = "Start time: "+ getApp().getSelectedJourney().getStart().get(Calendar.HOUR)+":"+getApp().getSelectedJourney().getStart().get(Calendar.MINUTE);
		seats = "Seats available: "+ getApp().getSelectedJourney().getTripPreferences().getSeatsAvailable();
//		String extras = "Extras: "+ getApp().getSelectedJourney().getTripPreferences().toString();
		BitSet sExtras = getApp().getSelectedJourney().getTripPreferences().getExtras();
		extras = "Extras: ";
		String[] items = {"Music", "Animals", "Breaks", "Talking", "Smoking"};
    	for(int i=0 ; i<sExtras.length() ; i++){
    		if(sExtras.get(i)){
    			if(i==sExtras.length()-1)
    				extras=extras+items[i]+".";
    			extras=extras+items[i]+",";
    		}
    	}

		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		if (facebookMessage == null){
			facebookMessage = "I have created a new ride on FreeRider\n"+date+"\n"+time+"\n"+seats+"\n"+extras;
		}
		messageToPost = facebookMessage;
	}

	public void doNotShare(View button){
		Intent intent = new Intent(ShareOnFacebook.this, Main.class);
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}
	@SuppressWarnings("deprecation")
	public void share(View button){
		if (! facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			postToWall(messageToPost);
			Intent intent = new Intent(ShareOnFacebook.this, Main.class);
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
	}

	@SuppressWarnings("deprecation")
	public void loginAndPostToWall(){
		 facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
	}

	@SuppressWarnings("deprecation")
	public void postToWall(String message){
	    
		Bundle postParams = new Bundle();

		postParams.putString("message", message);
		postParams.putString("caption", "https://maps.google.com/maps?saddr="+currentRoute.getStartAddress()
				+"&daddr="+currentRoute.getEndAddress());
		postParams.putString("description", "Click to see the route");
//		postParams.putString("privacy", "EVERYONE");
//		postParams.putString("actions", "[{'name':'Test a simple Graph API call!','link':'https://developers.facebook.com/tools/explorer?method=GET&path=me'}]");
		postParams.putString("type", "photo");
		postParams.putString("link", "https://maps.google.com/maps?saddr="+currentRoute.getStartAddress()
				+"&daddr="+currentRoute.getEndAddress());
		postParams.putString("picture", "http://www.veryicon.com/icon/png/Business/Business/Cars.png");

                try {
        	        facebook.request("me");
			String response = facebook.request("me/feed", postParams, "POST");
		
//		Request request = new Request(Session.getActiveSession(), "me/feed", postParams, HttpMethod.POST);
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") || response.equals("false")) {
				showToast("Blank response.");
			}
			else {
				showToast("Trip posted to your facebook wall!");
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
