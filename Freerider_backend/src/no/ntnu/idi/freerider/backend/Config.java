package no.ntnu.idi.freerider.backend;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * Starts when server starts
 * @author Thomas
 *
 */
public class Config implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
    	ServerLogger.write("Server started");
    }
    public void contextDestroyed(ServletContextEvent event) {
     
    }
}
