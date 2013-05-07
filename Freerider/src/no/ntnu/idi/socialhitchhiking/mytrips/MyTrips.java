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
package no.ntnu.idi.socialhitchhiking.mytrips;

import no.ntnu.idi.socialhitchhiking.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * This class displays the tabs in the MyTrips window
 *
 */
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
