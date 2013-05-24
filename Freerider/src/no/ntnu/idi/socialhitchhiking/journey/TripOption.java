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

/**
 * Object class containing trip options
 * @author Jose Luis Trigo
 * Class has 3 attributes:  
 * icon: to be shown on the left of each option 
 * title: name of the option 
 * subtitle: actual value of the option
 */

public class TripOption {
	public int icon;
    public String title;
    public String subtitle;
    
    public TripOption(){
        super();
    }
    
    /**
     * @param icon to be shown on the left of each option ({@link int})
     * @param title name of the option ({@link String})
     * @param subtitle actual value of the option ({@link String})
     */
    public TripOption(int icon, String title, String subtitle) {
        super();
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }
}
