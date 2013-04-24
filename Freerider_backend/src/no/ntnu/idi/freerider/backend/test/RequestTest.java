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
package no.ntnu.idi.freerider.backend.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import no.ntnu.idi.freerider.backend.DBConfigurator;
import no.ntnu.idi.freerider.backend.DBConnector;
import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.NotificationType;
import no.ntnu.idi.freerider.model.Route;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.model.Visibility;
import no.ntnu.idi.freerider.protocol.JourneyRequest;
import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.NotificationResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.RouteRequest;
import no.ntnu.idi.freerider.protocol.RouteResponse;
import no.ntnu.idi.freerider.protocol.UserRequest;
import no.ntnu.idi.freerider.xml.RequestSerializer;
import no.ntnu.idi.freerider.xml.ResponseParser;

import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** An integration test that makes requests of a server in the project's protocol and tests its responses.*/
public class RequestTest {
private static final String XML_PREFIX = "res" + File.separator + "test" + File.separator;
private static final String HOSTNAME = "vm-6116.idi.ntnu.no";
//private static final String HOSTNAME = "localhost";
static final String SERVER_URL = "http://" + HOSTNAME + ":8080/Freerider_backend/Servlet";
private DBConnector db;

private static final User testUser1 = new User("Robert';DROP TABLE Students", "bobbytables");
private static final User testUser2 = new User("Test","testuser");
private static Journey journey;
	@Before
	public void setUp() throws Exception {
		DBConfigurator.init(new File("WebContent/WEB-INF/DBConfig.xml"));
		db = new DBConnector();
		db.init();
	}

	@After
	public void tearDown() throws Exception {
	}	

	
	@Test
	public void testCREATE_ROUTERequest() throws IOException, DocumentException, SQLException {
		Response response = sendXML(XML_PREFIX + "CREATE_ROUTErequest.xml");
		assertEquals(RequestType.CREATE_ROUTE,response.getType());
		checkForErrors(response);
	}

	private static void checkForErrors(Response response) {
		assertEquals(ResponseStatus.OK,response.getStatus());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testGET_ROUTES() throws MalformedURLException, FileNotFoundException, IOException, SQLException{
		Response response  = sendXML(XML_PREFIX + "GET_ROUTES.xml");
		assertEquals(RequestType.GET_ROUTES,response.getType());
		assertFalse(((RouteResponse) response).getRoutes().size() > 0);
		checkForErrors(response);
	}
	
	@Test
	public void testCREATE_JOURNEY() throws SQLException, MalformedURLException, IOException{
		Route testRoute = db.getRoute("testroute", testUser1.getID());
		Calendar start = new GregorianCalendar();
		start.add(Calendar.DATE, 1);
		Journey newJourney = new Journey(-1,testRoute,start,null,Visibility.FRIENDS);
		Request request = new JourneyRequest(RequestType.CREATE_JOURNEY,testUser1,newJourney);
		Response response = sendRequest(request);
		assertEquals(RequestType.CREATE_JOURNEY, response.getType());
		checkForErrors(response);
		if(response instanceof JourneyResponse){
			journey = ((JourneyResponse) response).getJourneys().get(0);
		}else{
			fail("Incorrect response class.");
		}
	}
	
	@Test
	public void testUPDATE_JOURNEY() throws SQLException, MalformedURLException, IOException{
		Calendar oldStart = journey.getStart();
		Calendar newStart = new GregorianCalendar();
		newStart.add(Calendar.DATE, 1);
		journey.setStart(newStart);
		Request request = new JourneyRequest(RequestType.UPDATE_JOURNEY,testUser1,journey);
		Response response = sendRequest(request);
		assertEquals(RequestType.UPDATE_JOURNEY,response.getType());
		checkForErrors(response);
		List<Journey >journeys = ((JourneyResponse) response).getJourneys();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String expected = df.format(newStart.getTime());
		String actual = df.format(journeys.get(0).getStart().getTime());
		assertEquals(expected,actual);
		String old = df.format(oldStart.getTime());
		assertNotSame(old, actual);
		
	}
	
	@Test
	public void testHITCHHIKER_REQUESTNotification() throws MalformedURLException, IOException, SQLException{
		Notification note = new Notification(testUser2.getID(), testUser1.getID(),testUser2.getFullName(),"Some comment.", journey.getSerial(), NotificationType.HITCHHIKER_REQUEST);
		Request request = new NotificationRequest(testUser2 , note);
		Response response = sendRequest(request);
		assertEquals(RequestType.SEND_NOTIFICATION,response.getType());
		checkForErrors(response);
	}

	@Test
	public void testPULL_NOTIFICATIONS() throws MalformedURLException, IOException, SQLException{
		Request request = new UserRequest(RequestType.PULL_NOTIFICATIONS,testUser1);
		Response response = sendRequest(request);
		assertEquals(RequestType.PULL_NOTIFICATIONS,response.getType());
		checkForErrors(response);
		List<Notification> notes = ((NotificationResponse) response).getNotifications();
		assertEquals(1,notes.size());
		Notification note = notes.get(0);
		assertEquals(testUser2.getID(),note.getSenderID());
		assertEquals(testUser1.getID(),note.getRecipientID());
		assertEquals("Some comment.",note.getComment());
		assertEquals(NotificationType.HITCHHIKER_REQUEST,note.getType());
		
	}

	
	@Test
	public void testDRIVER_ACCEPTNotification() throws MalformedURLException, IOException, SQLException{
		Notification note = new Notification(testUser1.getID(), testUser2.getID(),testUser1.getFullName(), "No comment.", journey.getSerial(), NotificationType.REQUEST_ACCEPT);
		Request request = new NotificationRequest(testUser1 , note);
		Response response = sendRequest(request);
		assertEquals(RequestType.SEND_NOTIFICATION,response.getType());
		checkForErrors(response);
		//assertEquals(testUser2.getID(),db.getHitchhikerID(journey.getSerial()));
	}
	
	
	@Test
	public void testGET_JOURNEYS() throws MalformedURLException, IOException, SQLException{
		Request request = new UserRequest(RequestType.GET_JOURNEYS,testUser1);
		Response response = sendRequest(request);
		assertEquals(RequestType.GET_JOURNEYS,response.getType());
		checkForErrors(response);
		assertTrue(((JourneyResponse) response).getJourneys().size() >= 1);
	}
	
	@Test
	public void testDRIVER_CANCELNotification() throws MalformedURLException, IOException, SQLException{
		Notification note = new Notification(testUser1.getID(), testUser2.getID(),testUser1.getFullName(), "I can't go after all.", journey.getSerial(), NotificationType.DRIVER_CANCEL);
		Request request = new NotificationRequest(testUser1 , note);
		Response response = sendRequest(request);
		assertEquals(RequestType.SEND_NOTIFICATION,response.getType());
		checkForErrors(response);
	}
	
	@Test
	public void testHIKER_ACCEPTSNotification() throws MalformedURLException, IOException, SQLException{
		List<Journey> testJourneys = db.getJourneys(testUser1);
		assertFalse("Test journey was in GET_JOURNEYS result after sending DRIVER_CANCEL.",testJourneys.contains(journey));
		Notification note = new Notification(testUser2.getID(), testUser1.getID(), testUser2.getFullName(),"I said no comment.", journey.getSerial(), NotificationType.HITCHHIKER_ACCEPTS_DRIVER_CANCEL);
		Request request = new NotificationRequest(testUser2 , note);
		Response response = sendRequest(request);
		assertEquals(RequestType.SEND_NOTIFICATION,response.getType());
		checkForErrors(response);
	}
	
	@Test
	public void testGET_JOURNEYS_EMPTY() throws MalformedURLException, IOException, SQLException{
		Request request = new UserRequest(RequestType.GET_JOURNEYS,testUser1);
		Response response = sendRequest(request);
		assertEquals(RequestType.GET_JOURNEYS,response.getType());
		checkForErrors(response);
		int journeycount = ((JourneyResponse) response).getJourneys().size();
		assertTrue("User " + testUser1.getID() + " should have no journeys, had " + journeycount,journeycount == 0);
	}
	
	@Test
	public void testDELETE_ROUTERequest() throws IOException, DocumentException, SQLException {
		Route routeToDelete = db.getRoute("testroute", testUser1.getID());
		Request toSend = new RouteRequest(RequestType.DELETE_ROUTE,testUser1,routeToDelete);
		Response response = sendRequest(toSend);
		assertEquals(RequestType.DELETE_ROUTE,response.getType());
		checkForErrors(response);
	}

	
	@Test
	public void testSEARCHrequest() throws IOException, DocumentException, SQLException {
		Response response = sendXML(XML_PREFIX + "SEARCH.xml");
		assertEquals(RequestType.SEARCH,response.getType());
		
		checkForErrors(response);
		List<Journey> list = ((JourneyResponse) response).getJourneys();
		System.out.println("Search found " + (list != null ? list.size() : "NULL") + " journeys.");
	}
	
	
	@Test
	public void testCREATE_USER() throws MalformedURLException, FileNotFoundException, IOException, SQLException{
		db.deleteTestUser();
		Response response = sendXML(XML_PREFIX + "CREATE_USER.xml");
		assertEquals(RequestType.CREATE_USER,response.getType());
		checkForErrors(response);
	}
	
	
	private Response sendRequest(Request toSend) throws IOException,
	MalformedURLException {
		HttpURLConnection conn = (HttpURLConnection) new URL(SERVER_URL).openConnection();
		conn.setDoOutput(true);
		InputStream xmlStream = new StringInputStream(RequestSerializer.serialize(toSend));
		IOUtils.copy(xmlStream, conn.getOutputStream());
		
		Response response = ResponseParser.parse(conn.getInputStream());
		return response;
	}
	
	private Response sendXML(String xmlPath) throws MalformedURLException,
	IOException, FileNotFoundException {
		URL url = new URL(SERVER_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		OutputStream stream = conn.getOutputStream();
		IOUtils.copy(new FileInputStream(xmlPath), stream);
		return ResponseParser.parse(conn.getInputStream());
	}

}

/** Private utility class to turn Strings into InputStreams */
class StringInputStream extends InputStream{
	private String string;
	private int mark;
	private int position;
	
	StringInputStream(String content){
		string = content;
		position = 0;
	}

	@Override
	public boolean markSupported(){
		return true;
	}
	@Override
	public int read() throws IOException {
		if(position < string.length()) return string.charAt(position++);
		return -1;
	}
	
	@Override
	public int available(){
		return string.length()-position;
	}
	
	@Override
	public void mark(int readlimit){
		mark = position;
	}
	@Override
	public void reset(){
		position = mark;
	}
}
