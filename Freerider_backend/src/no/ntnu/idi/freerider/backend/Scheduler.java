package no.ntnu.idi.freerider.backend;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Repeatedly runs {@link MaintenanceService} at a given interval
 * @author Thomas
 *
 */
public class Scheduler implements ServletContextListener{
	
	private ScheduledExecutorService scheduler;
	public void contextDestroyed(ServletContextEvent arg0) {
		scheduler.shutdownNow();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new MaintenanceService(), 1, 1, TimeUnit.MINUTES);
		
	}
	
}
