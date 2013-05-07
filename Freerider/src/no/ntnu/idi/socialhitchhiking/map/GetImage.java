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
package no.ntnu.idi.socialhitchhiking.map;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import no.ntnu.idi.socialhitchhiking.myAccount.MyAccountMeActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Class that fetches images from URLs. Used here to get profile pictures from Facebook.
 * Call execute from an instance of this class with a URL as a parameter to display the image.
 * @author Kristoffer
 * 
 */
public class GetImage extends AsyncTask<String, Void, Bitmap> {
    
	private ImageView imageView;
	private Activity activity;
	private MyAccountMeActivity myAcc;
    /**
     * Constructor takes the ImageView used to display the image.
     * @param iv 
     */
    public GetImage(ImageView iv, Activity activity){
		this.imageView = iv;
		this.activity = activity;
	}
	@Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap map = null;
        for (String url : urls) {
            map = downloadImage(url);
        }
        return map;
    }

	/**
	 * Sets the Bitmap returned by doInBackground
	 */
    @Override
    protected void onPostExecute(Bitmap result) {
    	if(activity.getClass().getSimpleName().equals("MyAccountMeActivity")){
    		myAcc = (MyAccountMeActivity) activity;
    		myAcc.showProfile(result);
    	}else{
    		imageView.setImageBitmap(result);
    	}
    }

    /**
     * Creates Bitmap from InputStream and returns it
     * @param url
     * @return
     */
    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        URL urltest = null;
        try {
			urltest = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            bitmap = BitmapFactory.decodeStream(urltest.openConnection().getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

}
