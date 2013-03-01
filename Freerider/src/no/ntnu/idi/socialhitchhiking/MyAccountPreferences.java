package no.ntnu.idi.socialhitchhiking;

import java.util.ArrayList;

import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;
import android.os.Bundle;
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


