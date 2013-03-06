package no.ntnu.idi.socialhitchhiking.journey;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.Visibility;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.RouteRequest;
import no.ntnu.idi.freerider.protocol.RouteResponse;
import no.ntnu.idi.freerider.protocol.UserRequest;
import no.ntnu.idi.freerider.protocol.UserResponse;

import no.ntnu.idi.socialhitchhiking.Main;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;
import no.ntnu.idi.socialhitchhiking.utility.DateChooser;
import no.ntnu.idi.socialhitchhiking.utility.TripOptionAdapter;
import no.ntnu.idi.socialhitchhiking.utility.SocialHitchhikingActivity;

import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class TripOptions extends SocialHitchhikingActivity {
	
    private ListView listView1;
    private Calendar dateAndTime;
	private DateChooser dc;
	private NumberPicker seats;
	private Journey currentJourney;
	private TripPreferences tripPreferences;
	private Integer selectedPrivacy;
	private Visibility privacyPreference;
    private PropertyChangeListener propLis = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if(event.getPropertyName() == DateChooser.DATE_CHANGED){
				dateAndTime = (Calendar) event.getNewValue();
				if(dateAndTime != null){
					//sendJourneyRequest();
				}
				else{
				}
			}
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_options);
        
        //TODO
        //Options that will appear in the list. I have to fill the last parameter of trip option with the actual data
        //as it is now is only for showing how it would look like
        TripOption trip_options_data[] = new TripOption[]
        {
            new TripOption(R.drawable.trip_icon_calendar, "Date","12/12/13"),
            new TripOption(R.drawable.trip_icon_clock, "Time","12:30"),
            new TripOption(R.drawable.trip_icon_seats, "Seats Available", "1"),
            new TripOption(R.drawable.trip_icon_fb, "Privacy", "Friends of Friends"),
            new TripOption(R.drawable.trip_icon_plus, "Extras", "Breaks/Animals/Music/Talking/Smoking")
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
        //TODO Must change this to change only the date
    	
    	AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Set Date");
		b.setMessage("Do you want to set the date of the Trip?");
		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dc = new DateChooser(TripOptions.this, propLis);
				dc.setTitleDate("Set Date of Trip");
				dc.showDatePicker();
			}
		});
		b.setNegativeButton("Cancel", null);
		b.show();
    }
    
    void setTime(){
    	//TODO I must change this so it only changes the time

    	AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Set Date");
		b.setMessage("Do you want to set the date of the Trip?");
		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dc = new DateChooser(TripOptions.this, propLis);
				dc.setTitleDate("Set Date of Trip");
				dc.showDatePicker();
			}
		});
		b.setNegativeButton("Cancel", null);
		b.show();
    }
    void setSeats(){
//    	counter = 0;
//    	Button add = (Button) findViewById(R.id.bAdd);
//    	Button sub = (Button) findViewById(R.id.bSub);
//    	final EditText display = (EditText) findViewById(R.id.numberofseats);
    	
    	
    	//TODO Update seats in TripPreferences and show it on TripOptions Activity
    	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	    builder.setTitle("Seats");	
    	    builder.setMessage("Select seats available");
    	    // Get the layout inflater
    	    LayoutInflater inflater = this.getLayoutInflater();
    	    
//    	    add.setOnClickListener(new View.OnClickListener() {
//
//    	    	public void onClick(View v) {
//    	    		counter++;
//    	    		display.setText( "" + counter);
//    	    	}
//    	    });
//    	    sub.setOnClickListener(new View.OnClickListener() {
//
//    	    	public void onClick(View v) {
//    	    		counter--;
//    	    		display.setText( "" + counter);
//    	    	}
//    	    });
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    
    	    builder.setView(inflater.inflate(R.layout.number_picker, null));
    	    
    	    // Add action buttons
    	    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
				       // sign in the user ...
				}
    	    });
    	    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    	    	public void onClick(DialogInterface dialog, int id) {
    	    		//
    	    	}
    	    });      
    	    builder.show();
    }
    void setPrivacy(){
    	//TODO Change this to save visibility preferences in current Journey/trip.
    	selectedPrivacy=-1;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Set Privacy");
    	builder.setSingleChoiceItems(R.array.privacy_setting, -1, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
    	        selectedPrivacy = item;
    	    }
    	});
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //currentJourney.setVisibility(Visibility.FRIENDS_OF_FRIENDS);
            	switch (selectedPrivacy) {
				case 0:
					privacyPreference=Visibility.FRIENDS;
					break;
				case 1:
					privacyPreference=Visibility.FRIENDS_OF_FRIENDS;
					break;
				case 2:
					privacyPreference=Visibility.PUBLIC;
					break;
				default:
					break;
				}
            	Toast.makeText(getApplicationContext(), privacyPreference.toString(), Toast.LENGTH_SHORT).show();
            }
          
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
            }
        });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    void setExtras(){
    	//TODO This should record which are the Trip extras and write it in Journey/Ride in the attribute TripPreferences
    	final CharSequence[] items = {"Breaks", "Animals", "Music", "Talking", "Smoking"};
    	final boolean [] selectedExtras = {false, false, false, false, false};

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Pick extras");
    	builder.setMultiChoiceItems(R.array.preferences_array, null, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
				selectedExtras[which] = isChecked;
			}
    	});
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               //TODO Check which check boxes are marked and update the tripPreferences
//            	for(int i=0;i<items.length;i++){
//            		if
//            	}
//            	Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
            }
        });
    	 builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
             }
         });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
  //When next button is clicked it goes to RideInfo? or back to Main. Right now it goes back to the Main Activity
    public void onNextClick(View v){
    	Toast.makeText(getApplicationContext(), "Trip created", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(TripOptions.this, Main.class);
		startActivity(intent);
	}
}