package no.ntnu.idi.socialhitchhiking.map;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
	
    /**
     * Constructor takes the ImageView used to display the image.
     * @param iv 
     */
    public GetImage(ImageView iv){
		this.imageView = iv;
	}
	@Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap map = null;
        for (String url : urls) {
            map = downloadImage(url);
        }
        return map;
    }

    // Sets the Bitmap returned by doInBackground
    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    // Creates Bitmap from InputStream and returns it
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