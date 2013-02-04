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
package no.ntnu.idi.freerider.model;

/** An enum describing how large a subset
 *  of the poster's relationship graph should be able to see
 *  what is posted.
 */
public enum Visibility {
	/** Should be visible to direct friends only. (Distance <= 1)*/
	FRIENDS,
	/** Should be visible to direct friends, or their direct friends. (Distance <= 2)*/
	FRIENDS_OF_FRIENDS,
	/** Should be visible to any person. (Distance <= infinity)*/
	PUBLIC;
	
	public String getDisplayName(){
		switch(this){
		case PUBLIC:
			return "Public";
		case FRIENDS_OF_FRIENDS:
			return "Friends of friends";
		case FRIENDS:
			return "Friends";
		default:
			return toString();
		}
	}
}
