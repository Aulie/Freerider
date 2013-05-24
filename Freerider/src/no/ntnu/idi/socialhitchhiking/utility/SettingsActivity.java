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
package no.ntnu.idi.socialhitchhiking.utility;

import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import no.ntnu.idi.socialhitchhiking.map.MapActivityAddPickupAndDropoff;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
/**
 * Class for showing the settings activity
 * @author Thomas Gjerde
 *
 */
public class SettingsActivity extends PreferenceActivity{
	SocialHitchhikingApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (SocialHitchhikingApplication) getApplication();
		addPreferencesFromResource(R.xml.settings_activity);

	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if(preference.getKey().equals("freerider_credits")){
			PackageInfo pInfo;
			String version = "2.0";
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				version = pInfo.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			AlertDialog.Builder ad = new AlertDialog.Builder(SettingsActivity.this);  
			ad.setMessage("Freerider v" + version + "\n\nBy Freerider Team 2:\n\tMade Ziius\n\tThomas Gjerde\n\tKristoffer Aulie\n\tMagnus Lefdal\n\tJosè Luis Trigo\n\tJon-Robert Skårberg\n\tFredrik Tangen");
			ad.setTitle("Credits");
			ad.setPositiveButton("Close",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			  });
			ad.show();
		} else if(preference.getKey().equals("app_info")){
			AlertDialog.Builder ad = new AlertDialog.Builder(SettingsActivity.this);  
			ad.setMessage("Routes that have not been used in 14 days are automatically deleted \nRides are automatically deleted 24 hours after completion");
			ad.setTitle("Privacy information");
			ad.setPositiveButton("Close",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			  });
			ad.show();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public void onBackPressed() {
		Intent main = new Intent(this, no.ntnu.idi.socialhitchhiking.Main.class);
		startActivity(main);
		finish();

		super.onBackPressed();
	}

}
