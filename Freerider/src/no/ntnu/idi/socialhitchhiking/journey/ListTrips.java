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
package no.ntnu.idi.socialhitchhiking.journey;

import no.ntnu.idi.socialhitchhiking.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
/**
 * Wrapper class for trip tabs
 * @author Thomas Gjerde
 *
 */
public class ListTrips extends TabActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_trips);
		TabHost tabHost = getTabHost();
		 

        TabSpec ownedSpec = tabHost.newTabSpec("Owned");
        ownedSpec.setIndicator("Created by me");
        Intent ownedIntent = new Intent(this, ListJourneys.class);
        ownedIntent.putExtra("owned", true);
        ownedSpec.setContent(ownedIntent);
 

        TabSpec hitchedSpec = tabHost.newTabSpec("Hitched");
        hitchedSpec.setIndicator("Hitched by me");
        Intent hitchedIntent = new Intent(this, ListJourneys.class);
        hitchedIntent.putExtra("owned", false);
        hitchedSpec.setContent(hitchedIntent);
 
        tabHost.addTab(ownedSpec);
        tabHost.addTab(hitchedSpec);
	}
	
}
