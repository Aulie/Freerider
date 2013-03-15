package no.ntnu.idi.socialhitchhiking.mytrips;

import no.ntnu.idi.socialhitchhiking.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MyTrips extends TabActivity {
	
@SuppressWarnings("deprecation")
@Override
protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.my_trips_view);
			TabHost tabHost = getTabHost();
			   
		    // Tab for My Rides
		    TabSpec myRidesspec = tabHost.newTabSpec("MyRides");
		    myRidesspec.setIndicator("MY RIDES");
		    Intent mycarIntent = new Intent(this, MyRides.class);
		    myRidesspec.setContent(mycarIntent);

		    // Tab for Preferences
		    TabSpec hichedRidesspec = tabHost.newTabSpec("HichedRides");
		    hichedRidesspec.setIndicator("HICHED RIDES");
		    Intent preferancesIntent = new Intent(this, HitchedRides.class);
		    hichedRidesspec.setContent(preferancesIntent);

		    // Adding all TabSpec to TabHost
		    tabHost.addTab(hichedRidesspec); 
		    tabHost.addTab(myRidesspec);
		    
		    //custom size to the preference tab
		    tabHost.getTabWidget().getChildAt(0).getLayoutParams().width =45;
		}

}