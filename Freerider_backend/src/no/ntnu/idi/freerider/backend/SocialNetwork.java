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

import java.util.List;

import no.ntnu.idi.freerider.model.Journey;
/** An interface wrapping social network connections and requests, to minimize coupling. */
public interface SocialNetwork {
	/** Takes the given List of Journeys and removes any which are not visible
	 * to the user identified by searcher, according to their particular visibility.
	 * @param journeys The List to filter.
	 * @param searcher The user asking for the List.
	 * @param accessToken Access token for access to the searcher's friendship relations.
	 */
	public void filterSearch(List<Journey> journeys, String searcherID, String accessToken);

}
