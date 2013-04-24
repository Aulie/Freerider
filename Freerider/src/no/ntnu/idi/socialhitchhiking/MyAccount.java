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
package no.ntnu.idi.socialhitchhiking;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.socialhitchhiking.journey.ListJourneys;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * This class makes the tab view for my account and adds three tabs on it.
 * @author Made Ziius
 *
 */
@SuppressWarnings("deprecation")
public class MyAccount extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account);
		TabHost tabHost = getTabHost();
		   
	    // setting Title and Icon for the Me Tab
	    TabSpec mespec = tabHost.newTabSpec("Me");
	    mespec.setIndicator("ME");
	    Intent meIntent = new Intent(this, MyAccountMeActivity.class);
	    mespec.setContent(meIntent);

	    // Tab for My car
	    TabSpec mycarspec = tabHost.newTabSpec("MyCar");
	    mycarspec.setIndicator("MY CAR");
	    Intent mycarIntent = new Intent(this, MyAccountCar.class);
	    mycarspec.setContent(mycarIntent);

	    // Tab for Preferences
	    TabSpec preferencesspec = tabHost.newTabSpec("Preferances");
	    preferencesspec.setIndicator("PREFERENCES");
	    Intent preferancesIntent = new Intent(this, MyAccountPreferences.class);
	    preferencesspec.setContent(preferancesIntent);

	    // Adding all TabSpec to TabHost
	    tabHost.addTab(mespec); 
	    tabHost.addTab(preferencesspec); 
	    tabHost.addTab(mycarspec);
	    
	    //custom size to the preference tab
	    tabHost.getTabWidget().getChildAt(1).getLayoutParams().width =45;
	    
	    // If from newUser dialog, go to the Me-tab
	    if(getIntent().getBooleanExtra("fromDialog", false)){
	    	tabHost.setCurrentTab(0);
	    }
	}
}
