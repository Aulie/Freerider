package no.ntnu.idi.socialhitchhiking.utility;

import java.util.Comparator;

import no.ntnu.idi.freerider.model.Route;
/**
 * Custom comparator for sorting a route list by frequency
 * @author Thomas
 *
 */
public class RouteComparator implements Comparator<Route>{

	@Override
	public int compare(Route lhs, Route rhs) {
		Integer leftInt = lhs.getFrequency();
		Integer rightInt = rhs.getFrequency();
		return rightInt.compareTo(leftInt);
	}

}
