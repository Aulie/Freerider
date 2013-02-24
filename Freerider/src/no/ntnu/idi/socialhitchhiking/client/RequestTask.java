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
package no.ntnu.idi.socialhitchhiking.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import no.ntnu.idi.freerider.protocol.JourneyResponse;
import no.ntnu.idi.freerider.protocol.Request;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.xml.RequestSerializer;
import no.ntnu.idi.freerider.xml.ResponseParser;
import no.ntnu.idi.socialhitchhiking.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

public class RequestTask {
	private HttpClient httpclient;
	private HttpResponse response;
	HttpEntityEnclosingRequest entityRequest;
	HttpEntity entity;
	private String addr;
	private static Context con;

	private RequestTask(final String addr,String xml) {
		this.addr = addr;
		httpclient = new DefaultHttpClient();
		response = null;
		RequestLine line = new BasicRequestLine("POST", "/Freerider_backend/Servlet", new ProtocolVersion("HTTP", 1, 1));
		entityRequest = new BasicHttpEntityEnclosingRequest(line);
		try {
			 
			entity = new ByteArrayEntity(xml.getBytes("ISO-8859-1"));
			entityRequest.setEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private InputStream getResponse() throws ClientProtocolException, IOException{
		int port = Integer.parseInt(con.getResources().getString(R.string.server_port));
		HttpHost host = new HttpHost(addr, port);
		response = httpclient.execute(host, entityRequest,(HttpContext)null);
		StatusLine statusLine = response.getStatusLine();

		if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			return response.getEntity().getContent();
		} else{
			//Closes the connection.
			InputStream stream = response.getEntity().getContent();
			StringBuilder sb = new StringBuilder();
			while(stream.available() > 0){
				sb.append((char)stream.read());
			}
//			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
	}
	/**
	 * Static method which adds elements and data to an xml file and sends it as a string to the server.
	 * 
	 * @param req - {@link Request}
	 * @return returns a {@link JourneyResponse} to the input {@link Request}
	 * @throws ClientProtocolException 
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static Response sendRequest(final Request req,final Context c) throws ClientProtocolException, IOException, InterruptedException, ExecutionException  {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
	    Callable<Response> callable = new Callable<Response>() {
	        @Override
	        public Response call() throws ClientProtocolException, IOException {
	        	String xml = RequestSerializer.serialize(req);
	    		con = c;
	    		String url = con.getResources().getString(R.string.server_url);
	    		RequestTask requestTask = new RequestTask(url,xml);

	    		return ResponseParser.parse(requestTask.getResponse());
	        }
	    };
	    Future<Response> future = executor.submit(callable);
	    Response ret = future.get();
	    executor.shutdown();
	    return ret;
	}

}