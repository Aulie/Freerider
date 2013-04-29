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

/** An abstract class representing a response from the server to the client. */
public abstract class Response {
	protected final RequestType type;
	protected final ResponseStatus status;
	private final String errorMessage;
	
	protected Response(RequestType type,ResponseStatus status){
		if(type.getResponseClass() != this.getClass()){
			throw new IllegalArgumentException("Attempted to create " + this.getClass() + " instance of type " + type + " but appropriate class is " + type.getResponseClass());
		}else{
			this.type = type;
		}
		this.status = status;
		this.errorMessage = null;
	}
	
	protected Response(RequestType type,ResponseStatus status,String errorMessage){
		if(type == null) throw new IllegalArgumentException("Response must have a non-null type.");
		if(type.getResponseClass() != this.getClass()){
			throw new IllegalArgumentException("Attempted to create " + this.getClass() + " instance of type " + type + " but appropriate class is " + type.getResponseClass());
		}else{
			this.type = type;
		}
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public RequestType getType() {
		return type;
	}

	public ResponseStatus getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Response type=");
		sb.append(type.toString());
		sb.append(", status=");
		sb.append(status.toString());
		if(errorMessage!= null){
			sb.append(", error_message=");
			sb.append(errorMessage);
		}
		return sb.toString();
	}

	public final String getErrorMessage() {
		return errorMessage;
	}
}

