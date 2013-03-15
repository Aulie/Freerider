package no.ntnu.idi.socialhitchhiking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;

import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.SearchRequest;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * This class creates the preferences checkbox list on MyAccount and on MyAccountPreferences activity.
 * @author Made Ziius
 *
 */
public class MyAccountPreferences extends SocialHitchhikingActivity {

	ArrayAdapter<String> prefAdap;
	ListView listPreferences;
	ArrayList<String> preferenceList;
	
    /** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_preferences);
		
		listPreferences = (ListView) findViewById(R.id.listPreferences);
		TripPreferences pref = new TripPreferences(777, true, true, true, true, true);
		pref.setPrefId(1);
		Request req = new PreferenceRequest(RequestType.GET_PREFERENCE, getApp().getUser(), pref);
		PreferenceResponse res = null;
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
	    
	}
	/**invoke getCheckedItemPositions() on list view object which returns the set of checked items (as SparseBooleanArray) in the list*/
	 public void onClick(View v) {
	        SparseBooleanArray checked = listPreferences.getCheckedItemPositions();
	        ArrayList<String> selectedItems = new ArrayList<String>();
	        for (int i = 0; i < checked.size(); i++) {
	            // Item position in adapter
	            int position = checked.keyAt(i);
	            // Add preference if it is checked i.e.) == TRUE!
	            if (checked.valueAt(i))
	                selectedItems.add(prefAdap.getItem(position));
	        }
	 }
}


