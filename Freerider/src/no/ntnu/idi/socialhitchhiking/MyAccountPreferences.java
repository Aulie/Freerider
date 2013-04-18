package no.ntnu.idi.socialhitchhiking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;

import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * Class that contains functionality for the "Preferences" tab in "My Account".
 * @author Made Ziius
 *
 */
public class MyAccountPreferences extends SocialHitchhikingActivity {

	ArrayAdapter<String> prefAdap;
	ListView listPreferences;
	ArrayList<String> preferenceList;
	Request req;
	PreferenceResponse res;
	SparseBooleanArray 	checked;
	ArrayList<String>  selectedItems;
	TripPreferences pref2;
    /** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_preferences);
		
		listPreferences = (ListView) findViewById(R.id.listPreferences);
		TripPreferences pref = new TripPreferences(777, true, true, true, true, true);
		pref.setPrefId(1);
		req = new PreferenceRequest(RequestType.GET_PREFERENCE, getApp().getUser(), pref);
		res = null;
			try {
				res = (PreferenceResponse) RequestTask.sendRequest(req,getApp());
				
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

		/**Preferences fetched from the strigs.xml*/
		String[] preferences = getResources().getStringArray(R.array.preferences_array);
		prefAdap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, preferences);
		listPreferences.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listPreferences.setAdapter(prefAdap);
	    prefAdap.notifyDataSetChanged();
	   
	    selectedItems = new ArrayList<String>();
	    checked = listPreferences.getCheckedItemPositions();
	    pref2 =res.getPreferences();
		Log.e("PREFERANCE", pref2.getMusic().toString());
	    
	    /**Fetch preferences from the database and display them*/
	    for (int i = 0; i < 5; i++){
	            switch (i) {
	                case 0: if(pref2.getMusic()){
	               listPreferences.setItemChecked(i, true);
	                		}
	                         break;
	                case 1: if( pref2.getAnimals()){
	                	listPreferences.setItemChecked(i, true);
                		}
	                	break;
	                case 2: if( pref2.getBreaks()){
	                	listPreferences.setItemChecked(i, true);
                		}
	                	break;
	                case 3:  if( pref2.getTalking()){
	                	listPreferences.setItemChecked(i, true);
                		}
	                	break;
	                case 4:  if( pref2.getSmoking()){
	                	listPreferences.setItemChecked(i, true);
                		}
	                	break;
	            }
	    }
	    prefAdap.notifyDataSetChanged();
	}
	/**invoke getCheckedItemPositions() on list view object which returns the set of checked items (as SparseBooleanArray) in the list*/
	public void onClick(View v) {
	        for (int i = 0; i < checked.size(); i++) {
	            // Item position in adapter
	            int position = checked.keyAt(i);
	            // Add preference if it is checked i.e.) == TRUE!
	            if (checked.valueAt(i))
	                selectedItems.add(prefAdap.getItem(position));
	        }
	 } 
	/** Save the preferences on the database*/
	@Override
	public void onStop() {

        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add preference if it is checked i.e.) == TRUE and remove it if it is not checked
	            switch (position) {
	                case 0: pref2.setMusic(checked.valueAt(i));
	                         break;
	                case 1:  pref2.setAnimals(checked.valueAt(i));
	                         break;
	                case 2: pref2.setBreaks(checked.valueAt(i));
	                		break;
	                case 3: pref2.setTalking(checked.valueAt(i));
	                		break;
	                case 4: pref2.setSmoking(checked.valueAt(i));
	                		break;
	            }
        }
        Request req3 = new PreferenceRequest(RequestType.UPDATE_PREFERENCE,getApp().getUser(),pref2);
        try {
			PreferenceResponse res2 = (PreferenceResponse) RequestTask.sendRequest(req3,getApp());
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
		super.onStop();
	}
}


