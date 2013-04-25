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
/**
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @version: 		1.0
 *
 * Copyright (C) 2012 Freerider Team.
 *
 * Licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package no.ntnu.idi.socialhitchhiking.utility;

import java.util.List;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.socialhitchhiking.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Custom ArrayAdapter<Journey> class. Specifies how a journey should be represented
 * in the GUI.
 * 
 */
public class JourneyAdapter extends ArrayAdapter<Journey>{
		private Activity act;
	
		public JourneyAdapter(Context context, int textViewResourceId,
				List<Journey> objects) {
			super(context, textViewResourceId, objects);
			this.act = (Activity) context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Journey current = this.getItem(position);

			LayoutInflater inflater = act.getLayoutInflater();
			View row = inflater.inflate(R.layout.your_journey_list_item, parent, false);
			
			TextView owner = (TextView)row.findViewById(R.id.your_journey_owner);
			TextView visibility = (TextView)row.findViewById(R.id.your_journey_item_visibility);
			TextView startTime = (TextView)row.findViewById(R.id.your_journey_item_starttime);
			TextView start = (TextView)row.findViewById(R.id.your_journey_item_start);
			TextView stop = (TextView)row.findViewById(R.id.your_journey_item_stop);
			TextView numHitch = (TextView)row.findViewById(R.id.your_journey_item_num_hitched);

			int c=0;
			switch (current.getVisibility()) {
			case FRIENDS:
				c = Color.rgb(0,191,255); //blue
				break;
			case FRIENDS_OF_FRIENDS:
				c = Color.rgb(255,140,0); //orange	
				break;
			case PUBLIC:
				
				c = Color.rgb(50,205,50); //green
				break;
			default:
				break;
			}
			
			String date = current.getStart().getTime().toString();
			if(date.length()>0){
				date = date.replaceAll("CET", "");
				date = date.replaceAll("CEST", "");
			}
			else{
				date = "ingen dato";
			}
			
			owner.setText(current.getRoute().getOwner().getFullName());
			visibility.setText(current.getVisibility().getDisplayName());
			visibility.setTextColor(c);
			startTime.setText(Html.fromHtml("<b>" + "Date: " +"</b>\t" + date));
			start.setText(Html.fromHtml("<b>" + "From: "+"</b> " + current.getRoute().getStartAddress()));
			stop.setText(Html.fromHtml("<b>" + "To: "+"</b>\t\t" + current.getRoute().getEndAddress()));
			numHitch.setText(Integer.toString(current.getHitchhikers().size()));

			return row;
		}
	}
