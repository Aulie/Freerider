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
package no.ntnu.idi.freerider.xml;


import java.util.List;


import org.dom4j.Document;
import org.dom4j.Element;

/** A validator verifying that a Document represents a Response. Its validity as XML is assumed.*/ 
class ResponseValidator {
	@SuppressWarnings("unchecked")
	public static boolean validate(Document request){
		if(request==null) return false;
		Element root = request.getRootElement();
		if(!root.getName().equals(ProtocolConstants.RESPONSE)) return false;
		List<Element> contents = root.elements();
		if(contents.size()<2) return false;
		Element header = contents.get(0);
		if(!header.getName().equals(ProtocolConstants.RESPONSE_HEADER)) return false;
		String type = header.attributeValue(ProtocolConstants.REQUEST_TYPE_ATTRIBUTE);
		if(type== null || type.equals("")) return false;
		return true;
	}
}
