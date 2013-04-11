package no.ntnu.idi.socialhitchhiking;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import no.ntnu.idi.freerider.model.Car;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.CarRequest;
import no.ntnu.idi.freerider.protocol.CarResponse;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

public class MyAccountCar extends SocialHitchhikingActivity {

    private static final int CAMERA_REQUEST = 1337; 
    private ImageView imageView;
    private User user;
    private EditText carName;
    private EditText seatsText;
    private RatingBar bar;
    private int id;
    private String carNameString;
    private float comfort;
    private Car car;
    private Integer seatsAvailable;
    private Bitmap btm;
    private byte[] byteArray;
    private byte[] byteArrayx;
    private PreferenceResponse prefRes;
    
    boolean seatsChanged;
    boolean carChanged;
    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.my_account_car);
	        this.imageView = (ImageView)this.findViewById(R.id.cameraView);
	        carName = (EditText) this.findViewById(R.id.carName);
	        bar = (RatingBar) this.findViewById(R.id.ratingBar1);
	        seatsText = (EditText)this.findViewById(R.id.myAccountCarSeats);
	        
	        // Getting the user
	        user = getApp().getUser();
	        
	        // Getting and displaying the car info from the database
        	Car car = new Car(user.getCarId(), "", 0.0);
        	setContentView(R.layout.main_loading);
        	// Loading the car and seats info
	        new CarLoader(this, car).execute();
	    }

	    @Override
		public void onStop() {
	    	seatsChanged = false;
	    	carChanged = false;
	    	boolean isEmpty = false;
	    	// Checks to see what is changed
	    	if(!seatsAvailable.toString().equals(seatsText.getText().toString())){
	    		seatsChanged = true;
	    	}
	    	if(!carName.getText().toString().equals(carNameString) ||
	    			bar.getRating() != comfort){
	    		carChanged = true;
	    	}
	    	
	    	// If the user has entered a new number of available seats
	    	if(seatsText.getText().length() > 0){
		    	try{
	    			seatsAvailable = Integer.parseInt(seatsText.getText().toString());
		    	}catch(NumberFormatException e){
		    		Toast.makeText(this, "Please enter an integer value in Available seats", Toast.LENGTH_SHORT).show();
		    		return;
		    	}
	    	}else{
	    		seatsAvailable = 0;
	    	}
    		// Getting car ID
    		id = user.getCarId();
			// Setting new car name
    		if(carName.getText().toString().length() > 0){
    			carNameString = carName.getText().toString();
    		}else{
    			carNameString = "";
    		}
    		// Setting new comfort
    		comfort = bar.getRating();
    		
    		/*Fetch car picture and convert it to byte array*/
    		btm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
    		
    		// Adds the picture of the car if a picture exists
    		if(btm != null){
    			ByteArrayOutputStream stream = new ByteArrayOutputStream();
    			btm.compress(Bitmap.CompressFormat.PNG, 100, stream);
    			byteArray = stream.toByteArray();
    		}
    		
    		// Update seats in the database if it is changed
    		if(seatsChanged){
	    		TripPreferences preferences = prefRes.getPreferences();
	    		preferences.setSeatsAvailable(seatsAvailable);
	    		Request prefReq = new PreferenceRequest(RequestType.UPDATE_PREFERENCE, user, preferences);
	    		try {
					RequestTask.sendRequest(prefReq, getApp());
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
    		}
    		// If the user has a car, update the info if it has changed
    		if(user.getCarId()!= 0 && carChanged){
	    		// Updating the car info to the database
	    		car = new Car(id,carNameString,comfort, byteArray);
	    		Request req = new CarRequest(RequestType.UPDATE_CAR, getApp().getUser(), car);
	    		try {
					RequestTask.sendRequest(req, getApp());
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
    		}
	    	// If the user doesn't have a car, create one and add it to the database
	    	else if(user.getCarId() == 0 && carChanged){
	    		// Adding the new car to the database
	    		car = new Car(id,carNameString,comfort, byteArray);
	    		Request req = new CarRequest(RequestType.CREATE_CAR, getApp().getUser(), car);
	    		try {
					RequestTask.sendRequest(req, getApp());
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
	    	}
	    	super.onStop();
	    }
	    
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (requestCode == 1337 && resultCode == RESULT_OK) {  
	            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            imageView.setImageBitmap(photo);
	            imageView.invalidate();
	        }else if(requestCode == 1 && resultCode == RESULT_OK){
	        	Uri chosenImageUri = data.getData();
	            Bitmap mBitmap = null;
	            try {
					mBitmap = Media.getBitmap(this.getContentResolver(), chosenImageUri);
					imageView.setImageBitmap(mBitmap);
		            imageView.invalidate();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
		/**
		 * Displaying the car info in the layout
		 * @param res
		 */
		public void showMain(CarResponse res, PreferenceResponse prefResInit) {
			// Initializing the views
			setContentView(R.layout.my_account_car);
			this.imageView = (ImageView)this.findViewById(R.id.cameraView);
	        carName = (EditText) this.findViewById(R.id.carName);
	        bar = (RatingBar) this.findViewById(R.id.ratingBar1);
	        seatsText = (EditText)this.findViewById(R.id.myAccountCarSeats);
	        
	        // Setting the number of seats available
	        prefRes = prefResInit;
	        seatsAvailable = prefRes.getPreferences().getSeatsAvailable(); 
	        if(seatsAvailable > 0){
	        	seatsText.setText(seatsAvailable.toString());
	        }else{
	        	seatsText.setText("");
	        }
	        
	        
	        // If the user does have a car registered
	        if(user.getCarId() != 0){
		        // Setting the car name
				carNameString = res.getCar().getCarName();
				// Setting the car ID
		        id = res.getCar().getCarId();
		        // Setting the comfort
		        comfort = (float)res.getCar().getComfort();
		        // Setting the car image
		        byteArray = res.getCar().getPhoto();
		        
				/*Values of the car from database*/
		        Log.e("Inni:", carNameString + " " + Integer.toString(id));
		
		        // Display these values to the user
		        carName.setText(carNameString);
		    	bar.setRating(comfort);
		    	
		    	String empty = "No car type set";
		        byteArrayx =  empty.getBytes();
		        // If a new image is set, display it
		    	if(!(res.getCar().getPhotoAsBase64().equals(Base64.encode(byteArrayx, Base64.URL_SAFE)))){
		    		imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
		    	}
	        }
	        //if user does not yet have a car registated
	        else{
	        	Log.e("???", "ja/nei");
		        carNameString = "";
		        id = -1;
		        comfort = 0.0f;
		        String empty = "No car type set";
		        byteArray =  empty.getBytes();
	        }
		    
	        // Setting the button for taking a car picture
			Button photoButton = (Button) this.findViewById(R.id.cameraButton);
	        photoButton.setOnClickListener(new View.OnClickListener()
	        {
	            @Override
	            public void onClick(View v) {
	            	carChanged = true;
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, 1337); 
	            }
	        });
	        // Setting the button for getting a car picture from the phone
 			Button getimageButton = (Button) this.findViewById(R.id.getimageButton);
 			getimageButton.setOnClickListener(new View.OnClickListener()
 	        {
 	            @Override
 	            public void onClick(View v) {
 	            	carChanged = true;
 	            	Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
 	            	photoPickerIntent.setType("image/*");
 	            	startActivityForResult(photoPickerIntent, 1);
 	            }
 	        });
		} 
	}

class CarLoader extends AsyncTask<Void, User, CarResponse>{
	Car car;
	MyAccountCar activity;
	PreferenceResponse prefRes;
	
	public CarLoader(Activity activity, Car carIn){
		this.activity = (MyAccountCar) activity;
		car = carIn;
	}
	/**
	 * Getting the car info from the database
	 * @param params
	 * @return
	 */
	protected CarResponse doInBackground(Void... params) {
		CarResponse res = null;;
		prefRes = null;
    	try {
    		if(activity.getApp().getUser().getCarId() != 0){
    			// Getting the users car info if he has a car
    			Request req = new CarRequest(RequestType.GET_CAR, activity.getApp().getUser(), car);
    			res = (CarResponse) RequestTask.sendRequest(req,activity.getApp());
    		}
    		// Getting the users preferences
    		TripPreferences pref = new TripPreferences(777, true, true, true, true, true);
    		pref.setPrefId(1); //Dummy data
    		Request prefReq = new PreferenceRequest(RequestType.GET_PREFERENCE, activity.getApp().getUser(), pref);
			prefRes = (PreferenceResponse) RequestTask.sendRequest(prefReq,activity.getApp());
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
	protected void onPostExecute(CarResponse result) {
		activity.showMain(result, prefRes);
	}
}