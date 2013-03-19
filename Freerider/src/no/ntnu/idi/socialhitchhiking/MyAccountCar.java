package no.ntnu.idi.socialhitchhiking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import no.ntnu.idi.freerider.model.Car;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.CarRequest;
import no.ntnu.idi.freerider.protocol.CarResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;

import org.apache.http.client.ClientProtocolException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyAccountCar extends SocialHitchhikingActivity {

    private static final int CAMERA_REQUEST = 1337; 
    private ImageView imageView;
    private User user;
    private EditText carName;
    private RatingBar bar;
    private int id;
    private String s;
    private  Float comfort;
    private Car car;
    private int seats;
    private Bitmap btm;
    private byte[] byteArray;
    private byte[] byteArrayx;
    
    private int idOld;
    private String sOld;
    private  Float comfortOld;
    private int seatsOld;
    private byte[] byteArrayOld;
    private CarResponse res;
    
    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.my_account_car);
	        this.imageView = (ImageView)this.findViewById(R.id.cameraView);
	        Button photoButton = (Button) this.findViewById(R.id.cameraButton);
	        carName = (EditText) this.findViewById(R.id.carName);
	        bar = (RatingBar) this.findViewById(R.id.ratingBar1);
	        user = getApp().getUser();
	        
	        if(user.getCarId()!= 0){
	        	Car car = new Car(user.getCarId(), "", 0.0f);
	        	Request req = new CarRequest(RequestType.GET_CAR, getApp().getUser(), car);
	        	
	        	try {
					res = (CarResponse) RequestTask.sendRequest(req,getApp());
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
	        	
	        	/*Values of the car from database*/
		        sOld = res.getCar().getCarName();
		        idOld =res.getCar().getCarId();
		        comfortOld = (float)res.getCar().getComfort();
		        byteArrayOld =res.getCar().getPhoto(); 

		        
		        //Display these values to the user
		        carName.setText(sOld);
	        	bar.setRating(comfortOld);

	        	String empty = "null";
		        byteArrayx =  empty.getBytes();
	        	if(!(res.getCar().getPhotoAsBase64().equals(Base64.encode(byteArrayx, Base64.URL_SAFE)))){
	        		
	        		imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArrayOld, 0, byteArrayOld.length));
	        	}
	        	
	        }
	       
	        //if user does not yet have a car registated
	        else{
		        s = "";
		        id = -1;
		        comfort = 0.0f;
		        String empty = "null";
		        byteArray =  empty.getBytes();
	        }
		    
	        photoButton.setOnClickListener(new View.OnClickListener()

	        
	        {

	            @Override
	            public void onClick(View v) {
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, 1337); 
	  
	            }
	            
	        });
	        
	    }

	    @Override
		public void onBackPressed() {
	    	/** check if user has a car and update car*/
	    	user = getApp().getUser();
	    	
	    	
	    	if(user.getCarId()!= 0){
	    		id = user.getCarId();
	    		if((carName.getText().toString() != sOld)){
	    			s = carName.getText().toString();
	    		}
	    		else{
	    			s=sOld;
	    		}
	    			
	    		if((bar.getRating() != comfortOld)){
	    			comfort = bar.getRating();
	    		}
	    		else{
	    			comfort=comfortOld;
	    		}
	    	
	    		/*Fetch car picture and convert it to byte array*/
	    		btm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
	    		
	    		//if there is a picture of a car than 
	    		if(btm != null){
	    			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    			btm.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    			byteArray = stream.toByteArray();
	    		}
	    		else{
	    			byteArray = byteArrayOld;
	    		}
	    		car = new Car(id,s,comfort, byteArray);
	    		Request req = new CarRequest(RequestType.UPDATE_CAR, getApp().getUser(), car);
	    		
	    		try {
					CarResponse res = (CarResponse) RequestTask.sendRequest(req,getApp());
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
	    	/** if user does not yet have a car, make new car and update it*/
	    	
	    	if(user.getCarId() == 0){
	    		
	    		if((carName.getText().toString() != s)){
	    			s = carName.getText().toString();
	    		}
	    		
	    		if(bar.getRating() != comfort){
	    			comfort = bar.getRating();
	    		}
	    	
	    		/*Fetch car picture and convert it to byte array*/
	    		btm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
	    		
	    		//if there is a picture of a car than 
	    		if(btm != null){
	    			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    			btm.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    			byteArray = stream.toByteArray();
	    		}
	    		else{
	    			byteArray=byteArrayOld;
	    		}
	    		
	    		car = new Car(id,s,comfort, byteArray);
	    		Request req = new CarRequest(RequestType.CREATE_CAR, getApp().getUser(), car);
	    		try {
					CarResponse res = (CarResponse) RequestTask.sendRequest(req,getApp());
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

	    	
			// TODO Auto-generated method stub
		
	    	}
	    	super.onBackPressed();
	    }
	    
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (requestCode == 1337 && resultCode == RESULT_OK) {  
	            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            imageView.setImageBitmap(photo);
	        }  
	    } 
	}

