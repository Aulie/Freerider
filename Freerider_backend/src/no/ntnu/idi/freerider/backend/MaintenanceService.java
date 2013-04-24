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
package no.ntnu.idi.freerider.backend;


import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import no.ntnu.idi.freerider.model.Journey;

/**
 * Deletes old routes each time it is triggered
 * @author Thomas
 *
 */
public class MaintenanceService implements Runnable {
	public void run() {
		//Routes
		ServerLogger.write("Run service routine");
		DBConnector db = new DBConnector();
		try {
			db.init();
			List<SimpleRoute> list = db.getAllSimpleRoutes();
			for(int i = 0; i<list.size();i++) {
				
				Calendar cal = Calendar.getInstance();  
				Calendar now = Calendar.getInstance();
				cal.setTime(list.get(i).getDateModified());
				cal.add(Calendar.DAY_OF_MONTH, 14);
				ServerLogger.write("Now:" + now.getTime().toString());
				ServerLogger.write("Compare:" + cal.getTime().toString());
				
				if(now.after(cal)){
					try{
						db.deleteRouteBySerial(list.get(i).getSerial());
					} catch(Exception e) {// Change to more specific exception
						ServerLogger.write(e.getMessage());
					}	
				}
			}
		} catch (Exception e) {//Change to more specific exception
			ServerLogger.write("Error: " + e.getMessage());
		}
		
		//Journeys
		try {
			List<Journey> journeys = db.getAllJourneys();
			Calendar cal = Calendar.getInstance();  
			cal.add(Calendar.DAY_OF_MONTH, -1);
			for(int i = 0; i < journeys.size(); i++){
				if(journeys.get(i).getStart().before(cal)){
					db.sendRating(journeys.get(i));
					
					db.deleteJourneyWithoutCheck(journeys.get(i));
				}
			}
		} catch (SQLException e) {
			ServerLogger.write("Error: " + e.getMessage());
		}
		
	}
}
