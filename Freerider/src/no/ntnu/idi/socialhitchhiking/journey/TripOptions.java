package no.ntnu.idi.socialhitchhiking.journey;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.DateChooser;
import no.ntnu.idi.socialhitchhiking.utility.TripOptionAdapter;
import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;


public class TripOptions extends Activity {

    private ListView listView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_options);
        
        TripOption trip_options_data[] = new TripOption[]
        {
            new TripOption(R.drawable.trip_icon_calendar, "Date"),
            new TripOption(R.drawable.trip_icon_clock, "Time"),
            new TripOption(R.drawable.trip_icon_seats, "Seats"),
            new TripOption(R.drawable.trip_icon_fb, "Privacy"),
            new TripOption(R.drawable.trip_icon_plus, "Extras")
        };
        
        TripOptionAdapter adapter = new TripOptionAdapter(this, 
                R.layout.list_row_trip_options, trip_options_data);
        
        
        listView1 = (ListView)findViewById(R.id.list);
         
        View header = (View)getLayoutInflater().inflate(R.layout.trip_options_header, null);
        listView1.addHeaderView(header);
        
        listView1.setAdapter(adapter);
        
        listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				listView1.setItemChecked(position, true);
				
				switch (position) {
				case 1:
					setDate();
					break;
				case 2:
					setTime();
					break;
				case 3:
					setSeats();
					break;
				case 4:
					setPrivacy();
					break;
				case 5:
					setExtras();
					break;
				default:
					break;
				}
			}
		});
        
    }
    void setDate(){
    	// TODO
	    AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Choose Date");
		b.setMessage("Change date");
//		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dc = new DateChooser(TripOptions3.this, propLis);
//				dc.setTitle("Set Date of Journey", "Set Time of Journey");
//				dc.show();
//			}
//		});
		b.setNegativeButton("Cancel", null);
		b.show();
    }
    
    void setTime(){
    	// TODO
	    AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Choose Date");
		b.setMessage("Change date");
//		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dc = new DateChooser(TripOptions3.this, propLis);
//				dc.setTitle("Set Date of Journey", "Set Time of Journey");
//				dc.show();
//			}
//		});
		b.setNegativeButton("Cancel", null);
		b.show();
    }
    void setSeats(){
    	
	    AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Seats");
		b.setMessage("Seats available");
//		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dc = new DateChooser(TripOptions3.this, propLis);
//				dc.setTitle("Set Date of Journey", "Set Time of Journey");
//				dc.show();
//			}
//		});
		b.setNegativeButton("Cancel", null);
		b.show();
    }
    void setPrivacy(){
    	
	    AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Select Privacy");
		b.setMessage("Friends, FoF, Public");
		
		b.setNegativeButton("Cancel", null);
		b.show();
    }
    void setExtras(){
    	// TODO It doesn't work now
	    DialogFragment d = new ExtrasDialogFragment();
	    //d.show(getDialogFragmentManager(), "Extras");
    }
    public void onNextClick(View v){
		Intent intent = new Intent(TripOptions.this, CreateOrLoadRide.class);
		startActivity(intent);
	}

    public class ExtrasDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
	        final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        // Set the dialog title
	        builder.setTitle("Extras")
	        // Specify the list array, the items to be selected by default (null for none),
	        // and the listener through which to receive callbacks when items are selected
	               .setMultiChoiceItems(R.array.privacy_values, null,
	                          new DialogInterface.OnMultiChoiceClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int which,
	                           boolean isChecked) {
	                       if (isChecked) {
	                           // If the user checked the item, add it to the selected items
	                           mSelectedItems.add(which);
	                       } else if (mSelectedItems.contains(which)) {
	                           // Else, if the item is already in the array, remove it 
	                           mSelectedItems.remove(Integer.valueOf(which));
	                       }
	                   }
	               })
	        // Set the action buttons
	               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User clicked OK, so save the mSelectedItems results somewhere
	                       // or return them to the component that opened the dialog
	                       
	                   }
	               })
	               
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int id) {
	                       
	                   }
	               });
	
	        return builder.create();
        }
    }
}