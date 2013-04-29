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
package no.ntnu.idi.socialhitchhiking.inbox;

import no.ntnu.idi.socialhitchhiking.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
/**
 * Wrapper class for notification tabs
 * @author Thomas Gjerde
 *
 */
public class ListNotifications extends TabActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_trips);
		TabHost tabHost = getTabHost();
		
		TabSpec unReadSpec = tabHost.newTabSpec("Unread");
		unReadSpec.setIndicator("Messages");
        Intent unReadIntent = new Intent(this, Inbox.class);
        unReadIntent.putExtra("history", false);
        unReadSpec.setContent(unReadIntent);
 

        TabSpec readSpec = tabHost.newTabSpec("Read");
        readSpec.setIndicator("Read");
        Intent readIntent = new Intent(this, Inbox.class);
        readIntent.putExtra("history", true);
        readSpec.setContent(readIntent);
 
        TabSpec requestSpec = tabHost.newTabSpec("Requests");
        requestSpec.setIndicator("Requests");
        Intent requestIntent = new Intent(this,Inbox.class);
        requestIntent.putExtra("request", true);
        requestSpec.setContent(requestIntent);
        
        tabHost.addTab(requestSpec);
        tabHost.addTab(unReadSpec);
        tabHost.addTab(readSpec);
	}
	
}
