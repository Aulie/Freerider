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
package no.ntnu.idi.freerider.backend;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.xml.RequestParser;
import no.ntnu.idi.freerider.xml.ResponseSerializer;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Servlet which provides the public interface of the backend server.
 * It answers requests provided as XML over HTTP.
 */
@WebServlet(description = "FreeRider public interface Servlet", urlPatterns = { "/Servlet" })
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Charset PREFERRED_CHARSET = Charset.forName("UTF-8");
	private RequestProcessor processor;
	private static Logger logger = LoggerFactory.getLogger(Servlet.class);

	@Override
	public void init() throws ServletException{
		super.init();
		//Configure log4j using config file.
		DOMConfigurator.configure(getServletContext().getRealPath("WEB-INF/log4j.xml"));
		//Configure database connection using config file.
		DBConfigurator.init(getServletContext().getRealPath("WEB-INF/DBConfig.xml"));
		
		processor = new RequestProcessor();	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request req = RequestParser.parse(request.getInputStream());
		Response responseObject = processor.process(req);
		String xmlResponse = ResponseSerializer.serialize(responseObject);
		response.getOutputStream().write(xmlResponse.getBytes(PREFERRED_CHARSET));
		logger.debug("Sent response:\n{}",xmlResponse);
	}


}
