package no.ntnu.idi.socialhitchhiking.journey;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.Visibility;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.PreferenceRequest;
import no.ntnu.idi.freerider.protocol.PreferenceResponse;
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
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
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
	private Route selectedRoute;
	private TripPreferences tripPreferences;
	private Integer selectedPrivacy = null;
	private Visibility privacyPreference;
	private TripOptionAdapter adapter;
	private List<TripOption> list_trip_options;
    
	private PropertyChangeListener propLis = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if(event.getPropertyName() == DateChooser.DATE_CHANGED){
				dateAndTime = (Calendar) event.getNewValue();
				
				String formatedDate = dateAndTime.get(Calendar.DAY_OF_MONTH)
						+"/"+(dateAndTime.get(Calendar.MONTH)+1)+"/"+dateAndTime.get(Calendar.YEAR);
				
				//This formats Calendar.MINUTE so minutes below 10 show a 0 before
				Integer min = dateAndTime.get(Calendar.MINUTE);
				String minutes=min.toString();
				if(min<10)
					minutes="0"+minutes;
				
				String formatedTime = dateAndTime.get(Calendar.HOUR_OF_DAY)+":"+minutes;
				
				list_trip_options.set(0, new TripOption(R.drawable.trip_icon_calendar, "Date", formatedDate));
				
				list_trip_options.set(1, new TripOption(R.drawable.trip_icon_clock, "Time", formatedTime));
				
				adapter.notifyDataSetChanged();
				
			}
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_options);
        privacyPreference=getApp().getSettings().getFacebookPrivacy();
        tripPreferences= new TripPreferences();
        tripPreferences.setPrefId(0);
        selectedRoute=getApp().getSelectedRoute();
        dateAndTime=Calendar.getInstance();
        list_trip_options = new ArrayList<TripOption>();
       
        list_trip_options.add(new TripOption(R.drawable.trip_icon_calendar, "Date",""));
        list_trip_options.add(new TripOption(R.drawable.trip_icon_clock, "Time",""));
        list_trip_options.add(new TripOption(R.drawable.trip_icon_seats, "Seats Available", ""));
        list_trip_options.add(new TripOption(R.drawable.trip_icon_fb, "Privacy", ""));
        list_trip_options.add(new TripOption(R.drawable.trip_icon_plus, "Extras", ""));
        
        adapter = new TripOptionAdapter(this, R.layout.list_row_trip_options, list_trip_options);       
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

//    	AlertDialog.Builder b = new AlertDialog.Builder(this);
//		b.setTitle("Set Date");
//		b.setMessage("Do you want to set the date of the Trip?");
//		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dc = new DateChooser(TripOptions.this, propLis);
//				dc.setTitleDate("Set Date of Trip");
//				dc.showTimePicker();
//			}
//		});
//		b.setNegativeButton("Cancel", null);
//		b.show();
    }
    void setSeats(){	
    	
    	//TODO Update seats in TripPreferences and show it on TripOptions Activity
    	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	    builder.setTitle("Seats");	
    	    builder.setMessage("Select seats available");

    	    LayoutInflater inflater = this.getLayoutInflater();

    	    final View textEntryView = inflater.inflate(R.layout.number_picker, null);
    	    final EditText editTextField = (EditText) textEntryView.findViewById(R.id.numberofseats);

    	    builder.setView(textEntryView);
    	    
    	    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
                    String editTextFieldValue = editTextField.getText().toString();
					tripPreferences.setSeatsAvailable(Integer.valueOf(editTextFieldValue));
					list_trip_options.set(2, new TripOption(R.drawable.trip_icon_seats, "Seats Availabe", editTextFieldValue));
					adapter.notifyDataSetChanged();				
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
    	selectedPrivacy=-1;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Set Privacy");
    	builder.setSingleChoiceItems(R.array.privacy_setting, -1, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	        selectedPrivacy = item;
    	    }
    	});
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	switch (selectedPrivacy) {
				case 0:
					privacyPreference=Visibility.FRIENDS;
					list_trip_options.set(3, new TripOption(R.drawable.trip_icon_fb, "Privacy", "Friends"));
					adapter.notifyDataSetChanged();
					break;
				case 1:
					privacyPreference=Visibility.FRIENDS_OF_FRIENDS;
					list_trip_options.set(3, new TripOption(R.drawable.trip_icon_fb, "Privacy", "Friends of Friends"));
					adapter.notifyDataSetChanged();
					break;
				case 2:
					privacyPreference=Visibility.PUBLIC;
					list_trip_options.set(3, new TripOption(R.drawable.trip_icon_fb, "Privacy", "Public"));
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
            }
          
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
            }
        });
    	AlertDialog alert = builder.create();
    	alert.show();
    	listView1.refreshDrawableState();
    }
    void setExtras(){
    	final String[] items = {"Music", "Animals", "Breaks", "Talking", "Smoking"};
    	final BitSet sExtras = new BitSet(5);

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Pick extras");
    	builder.setMultiChoiceItems(R.array.preferences_array, null, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				sExtras.set(which,isChecked);
			}
    	});
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               //TODO Check which check boxes are marked and update the tripPreferences
            	tripPreferences.setExtras(sExtras);
            	String ex = "";
            	for(int i=0 ; i<sExtras.length() ; i++){
            		if(sExtras.get(i)){
            			ex=ex+items[i]+" ";
            		}
            	}
            	list_trip_options.set(4, new TripOption(R.drawable.trip_icon_plus, "Extras", ex));
				adapter.notifyDataSetChanged();
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
    	sendJourneyRequest();
		Intent intent = new Intent(TripOptions.this, Main.class);
		startActivity(intent);		
	}
    
    private void sendJourneyRequest(){
    	Journey jour = new Journey(-1);
		jour.setRoute(selectedRoute);
		jour.setStart(dateAndTime);
		jour.setVisibility(privacyPreference);
		jour.setTripPreferences(tripPreferences);
		JourneyRequest req = new JourneyRequest(RequestType.CREATE_JOURNEY, getApp().getUser(), jour);
		
		Response res = null;
		try {
			res = RequestTask.sendRequest(req,getApp());
			if(res.getStatus() != ResponseStatus.OK){
//				createAlertDialog(this, false,  "Journey","created","");
				Toast.makeText(getApplicationContext(), "Trip created", Toast.LENGTH_SHORT).show();
			}
			else{
				if(getApp().getJourneys() != null)
					getApp().getJourneys().add(jour);
//				createAlertDialog(this, true, "Journey","created","");
				Toast.makeText(getApplicationContext(), "Trip created", Toast.LENGTH_SHORT).show();

			}
		} catch (ClientProtocolException e) {
//			createAlertDialog(this, false,  "Journey","created","");
			Toast.makeText(getApplicationContext(), "Trip created", Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
//			createAlertDialog(this, false,"Journey","created","");
			Toast.makeText(getApplicationContext(), "Trip created", Toast.LENGTH_SHORT).show();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Request req2 = new PreferenceRequest(RequestType.CREATE_PREFERENCE,getApp().getUser(),tripPreferences);
		try {
			PreferenceResponse res2 = (PreferenceResponse) RequestTask.sendRequest(req2,getApp());
			if(res2.getStatus() != ResponseStatus.OK){
//				createAlertDialog(this, false,  "Preferences","created","");
			}
			else{
//				if(getApp().g != null)
//					getApp().getJourneys().add(jour);
//				createAlertDialog(this, true, "Preferences","created","");
			}
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
    
}