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


import java.util.List;

import no.ntnu.idi.freerider.model.Journey;

/** A Response subclass representing responses involving one or more attached Journeys. */
public class JourneyResponse extends Response{
	
	private final List<Journey> journeys;
	
	public JourneyResponse(RequestType type,ResponseStatus status, List<Journey> journeys){
		super(type, status);
		this.journeys = journeys;
	}
	
	public JourneyResponse(RequestType type,ResponseStatus status, String errorMessage, List<Journey> journeys){
		super(type, status,errorMessage);
		this.journeys = journeys;
	}

	public List<Journey> getJourneys() {
		return journeys;
	}

	@Override
	public String toString() {
		String ret = super.toString();
		ret += ", journeys=" + (journeys != null ? "NULL" : journeys.size());
		return ret;
	}
}
