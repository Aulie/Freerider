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
package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.Car;

public class CarResponse extends Response
{
	private final Car car;
	public CarResponse(RequestType type, ResponseStatus status,Car car) {
		super(type, status);
		this.car = car;
	}
	protected CarResponse(RequestType type,ResponseStatus status, String errorMessage,Car car) {
		super(type, status, errorMessage);
		this.car = car;
	}
	public Car getCar()
	{
		return car;
	}
	@Override
	public String toString() {
		String ret = super.toString();
		ret += ", car=" + (car != null ? "NULL" : "");
		return ret;
	}
}
