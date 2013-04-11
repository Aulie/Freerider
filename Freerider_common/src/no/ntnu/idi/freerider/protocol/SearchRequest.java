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
package no.ntnu.idi.freerider.protocol;

import java.util.Calendar;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.User;
/**
 * A Request subclass representing a search for available Journeys.
 *
 */
public class SearchRequest extends Request{
	private final Location startPoint, endPoint;
	private final Calendar startTime;
	private final int numDays;

	public SearchRequest(User user, Location startPoint, Location endPoint, Calendar startTime, int numDays) {
		super(RequestType.SEARCH, user);
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.startTime = startTime;
		this.numDays = numDays;
	}
 
	public Location getStartPoint() {
		return startPoint;
	}

	public Location getEndPoint() {
		return endPoint;
	}

	

	public Calendar getStartTime() {
		return startTime;
	}

	public int getNumDays()
	{
		return numDays;
	}
	
}
