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

import no.ntnu.idi.freerider.model.Visibility;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

public class SettingsManager {
	private ConnectivityManager conMan;
	private WifiManager wifiMan;
	private LocationManager locMan;
	private SharedPreferences pref;
	private SocialHitchhikingApplication app;
	private OnSharedPreferenceChangeListener settingsListener = new OnSharedPreferenceChangeListener() {
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {
			if(key.equals("updates_interval") && sharedPreferences.getBoolean("perform_polling", false)){
				app.startService();
			}
			else if(key.equals("perform_polling") && sharedPreferences.getBoolean("perform_polling", false)){
				app.startService();
			}
			else if (key.equals("perform_polling") && !sharedPreferences.getBoolean("perform_polling", false)){
				app.killService();
			}
		}
	};
	public SettingsManager(Context contxt){
		this.app = (SocialHitchhikingApplication) contxt;
		pref = PreferenceManager.getDefaultSharedPreferences(contxt);
		pref.registerOnSharedPreferenceChangeListener(settingsListener);
		conMan = (ConnectivityManager)contxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiMan = (WifiManager) contxt.getSystemService(Context.WIFI_SERVICE);
		locMan = (LocationManager) contxt.getSystemService(Context.LOCATION_SERVICE);
	}
	public boolean isBackgroundData(){
		conMan.getActiveNetworkInfo();
		return conMan.getBackgroundDataSetting();
	}
	public boolean isWifi(){
		return wifiMan.isWifiEnabled();
	}
	public boolean isCheckSettings(){
		return pref.getBoolean("connection", true);
	}
	public boolean isOnline(){
		if (conMan.getActiveNetworkInfo().isConnectedOrConnecting()) { // Device is online 
			return true;
		}
		
		return false;
	}
	public boolean isGPS(){
		return locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	public String getUpdateInterval(){
		return pref.getString("updates_interval", "-1");
	}
	public boolean isPullNotifications(){
		return pref.getBoolean("perform_polling", true);
	}
	public Visibility getFacebookPrivacy(){
		String privacy = pref.getString("privacy", "public");
		
		if(privacy.equals("friends")){
			return Visibility.FRIENDS;
		}
		else if(privacy.equals("friends_of_friends")){
			return Visibility.FRIENDS_OF_FRIENDS;
		}
		else return Visibility.PUBLIC;
		
	}
	public String getAccessToken(){
		return pref.getString("access_token", "");
	}
	
	
}
