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

import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.User;
/** A subclass of Request representing requests with an attached Route, e.g. creation or editing of a Route.*/
public class RouteRequest extends Request{
	private final Route route;



	public RouteRequest(RequestType type, User user,  Route route) {
		super(type, user);
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}


	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) return false;
		return route.equals(((RouteRequest)obj).route);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + route.hashCode();
	}
	
	@Override
	public String toString() {
		return super.toString() + ", route=" + (route==null ? "NULL" : route.toString());
	}
	
}
