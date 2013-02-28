package no.ntnu.idi.freerider.backend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for writing to server log
 * @author Thomas
 *
 */
public class ServerLogger {
	/**
	 * Write to server log
	 * @param message
	 */
	public static void write(String message) {
		try {
		  FileWriter fstream = new FileWriter("/root/FreeRider/log.log",true);
		  BufferedWriter out = new BufferedWriter(fstream);
		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date now = new Date();
		  out.write(dateFormat.format(now) + ": " + message + "\n");
		  out.close();
		  } catch (Exception e){
		  
		  }
	}
}
