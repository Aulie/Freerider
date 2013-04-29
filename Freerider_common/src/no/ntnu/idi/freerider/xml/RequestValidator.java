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

package no.ntnu.idi.freerider.xml;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
/** A validator verifying that the document is a Request. Its validity to its own stated specification is assumed. */
class RequestValidator {
	@SuppressWarnings("unchecked")
	static boolean validate(Document request){
		if(request==null) return false;
		Element root = request.getRootElement();
		if(!root.getName().equals("Request")) return false;
		List<Element> contents = root.elements();
		if(contents.size()<2) return false;
		Element header = contents.get(0);
		if(!header.getName().equals("RequestHeader")) return false;
		if(header.attribute("type").getValue().equals("")) return false;
		return true;
	}

}
