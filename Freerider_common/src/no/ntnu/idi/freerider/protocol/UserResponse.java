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

import no.ntnu.idi.freerider.model.User;
/**
 * A Response subclass for responses involving users.
 * @author Thomas Gjerde
 *
 */
public class UserResponse extends Response{
	private User user;
	public UserResponse(RequestType type, ResponseStatus status) {
		super(type, status);
	}
	
	public UserResponse(RequestType type, ResponseStatus status, User user) {
		super(type, status);
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}

	public void setUser(User user){
		this.user = user;
	}

	public UserResponse(RequestType type, ResponseStatus status, String errorMessage) {
		super(type, status,errorMessage);
	}
}
