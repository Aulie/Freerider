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

import no.ntnu.idi.freerider.model.User;


/** An abstract class representing a request from a client to the server. */
public abstract class Request {
	protected final RequestType type;
	protected final User user;


	public User getUser() {
		return user;
	}

	protected Request(RequestType type, User user){
		if(type == null) throw new IllegalArgumentException("Request must have a non-null type.");
		if(type.getRequestClass() != this.getClass()){
			throw new IllegalArgumentException("Attempted to create " + this.getClass() + "of type " + type + " but appropriate class is " + type.getRequestClass());
		}else{
			this.type = type;
		}
		this.user = user;
	}

	public RequestType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int ret = 0;
		ret += user.hashCode();
		ret -= type.hashCode();
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Request)) return false;
		Request other = (Request) obj;
		if(type!=other.type) return false;
		if(!(user.equals(other.user))) return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Request type=");
		sb.append(type.toString());
		sb.append(", user=");
		sb.append(user==null ? "NULL" : user.toString());
		return sb.toString();
	}
}
