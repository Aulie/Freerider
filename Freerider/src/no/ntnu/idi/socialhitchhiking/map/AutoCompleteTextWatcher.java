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

package no.ntnu.idi.socialhitchhiking.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * This class implements {@link TextWatcher} and is a listener for {@link AutoCompleteTextView}s.
 * It gets auto complete data from the Google Places API.
 *
 */
public class AutoCompleteTextWatcher implements TextWatcher{
	
	/**
	 * The Google Places API requires an API key. The API key determines how many requests
	 * could be made per day. <i>(Only 1,000 queries/day with the free version)</i>
	 */
	private static String GOOGLE_PLACES_API_KEY;

	private Context context;
	
	private AutoCompleteTextView autoCompleteTextView;

	/**
	 * 
	 * @param adapter This should be the adapter used with the {@link AutoCompleteTextView}.
	 */
	public AutoCompleteTextWatcher(Context context, ArrayAdapter<String> adapter, AutoCompleteTextView autoCompleteTextView) {
		this.context = context;
		this.autoCompleteTextView = autoCompleteTextView;
		GOOGLE_PLACES_API_KEY = context.getResources().getString(no.ntnu.idi.socialhitchhiking.R.string.google_places_autocomplete_api_key);
		PersistHelper.initAutoCompleteCache(context);
	}

	/*
	 * (non-Javadoc)
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
		if(s.length() < 2) return;
		boolean onlyCheckCache = false;
		if(count > 1 || count <1){
			onlyCheckCache = true;
		}

		String key = String.valueOf(s);
		if(PersistHelper.autoCompleteCacheContainsKey(key)){
			List<String> list = PersistHelper.getAutoCompleteForKey(context, key);
			setAutoCompleteArrayAdapter(list, autoCompleteTextView, false);
			return;
		}
		if(!onlyCheckCache){
			new AsyncTask<Integer, Void, ArrayList<String>> () {
				@Override
				protected synchronized void onPostExecute(ArrayList<String> resultList) {
					setAutoCompleteArrayAdapter(resultList, autoCompleteTextView, false);
					PersistHelper.addAutoCompleteCacheElement(context, String.valueOf(s), resultList);
				}
				@Override
				protected ArrayList<String> doInBackground(Integer... params) {
					String key = String.valueOf(s);
					ArrayList<String> resultList = new ArrayList<String>();
					try{
						String loc = "&location=63.41810,10.40549&radius=10";
	
						String types2 = "geocode"; 
						URL googlePlaces2 = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ URLEncoder.encode(key, "UTF-8") +loc+"&types="+types2+"&language=no&sensor=true&key="+GOOGLE_PLACES_API_KEY);
						URLConnection urlConnection2 = googlePlaces2.openConnection();
						BufferedReader in2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

						String line2;
						StringBuffer sb2 = new StringBuffer();
						while ((line2 = in2.readLine()) != null) {
							sb2.append(line2);
						}
						JSONObject predictions2 = new JSONObject(sb2.toString());	             
						JSONArray ja2 = new JSONArray(predictions2.getString("predictions"));

						for (int i = 0; i < ja2.length(); i++) {
							JSONObject jo2 = (JSONObject) ja2.get(i); 
							resultList.add(jo2.getString("description"));
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) { 
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return resultList;
				}
			}.execute(1);
		}
	}
	
	private synchronized void setAutoCompleteArrayAdapter(final List<String> list, final AutoCompleteTextView autoCompleteTextView, boolean isRetarded){
		String[] data = list.toArray(new String[list.size()]); 
		ArrayAdapter<?> adapter = new ArrayAdapter<Object>(context, no.ntnu.idi.socialhitchhiking.R.layout.item_list, data);
		autoCompleteTextView.setAdapter(adapter);  

		if(isRetarded){
			int selStartF = autoCompleteTextView.getSelectionStart();
			int selStopF  = autoCompleteTextView.getSelectionEnd();
			autoCompleteTextView.setText(autoCompleteTextView.getText().toString()+"¤");
			autoCompleteTextView.setText(autoCompleteTextView.getText().toString().replace("¤", ""));
			autoCompleteTextView.setSelection(selStartF, selStopF);
		}
	}
	
	@Override
	public void afterTextChanged(Editable editable) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
	}
}
