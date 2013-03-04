package no.ntnu.idi.socialhitchhiking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MyAccountCar extends Activity {

    private static final int CAMERA_REQUEST = 1337; 
    private ImageView imageView;
    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.my_account_car);
	        this.imageView = (ImageView)this.findViewById(R.id.cameraView);
	        Button photoButton = (Button) this.findViewById(R.id.cameraButton);
	        photoButton.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, 1337); 
	  
	            }
	        });
	    }

	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (requestCode == 1337 && resultCode == RESULT_OK) {  
	            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            imageView.setImageBitmap(photo);
	        }  
	    } 
	}

