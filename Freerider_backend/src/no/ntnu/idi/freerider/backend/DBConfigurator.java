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
package no.ntnu.idi.freerider.backend;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/** A configurator for DBConnector, which reads the details of the database connection from an XML file. */
public final class DBConfigurator {
	private static Calendar lastRead = null;
	private static File configFile;
	private static String DB_URL;
	private static String DB_USERNAME;
	private static String DB_PASSWORD;
	
	
	private static void update(){
		if(configFile==null || !configFile.exists()){
			throw new RuntimeException("No database config file found.");
		}
		if(lastRead == null || configFile.lastModified() > lastRead.getTimeInMillis()){
			reloadFields();
		}
	}
	
	private static void reloadFields() {
		try {
			Document document = null;
			document = new SAXReader().read(configFile);
			lastRead = new GregorianCalendar();
			Element root = document.getRootElement();
			Element dbUrlElement = root.element("DATABASE_URL");
			DB_URL = dbUrlElement.getTextTrim();
			Element dbUserNameElement = root.element("DATABASE_USERNAME");
			DB_USERNAME = dbUserNameElement.getTextTrim();
			Element dbPasswordElement = root.element("DATABASE_PASSWORD");
			DB_PASSWORD = dbPasswordElement.getTextTrim();
		} catch (Exception e) {
			throw new RuntimeException("Error reading configuration file.", e);
		}
	}

	/** Retrieve database URL. */
	public static String getDatabaseUrl(){
		update();
		return DB_URL;
	}
	
	/** Retrieve database username. */
	public static String getDatabaseUsername(){
		update();
		return DB_USERNAME;
	}
	
	/** Retrieve database password. */
	public static String getDatabasePassword(){
		update();
		return DB_PASSWORD;
	}
	
	/** Identify the config file. */
	public static void init(File configfile){
		configFile = configfile;
	}

	/** Identify the path to the config file. */
	public static void init(String configPath){
		configFile = new File(configPath);
	}
}
