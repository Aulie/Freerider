package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.TripPreferences;
import no.ntnu.idi.freerider.model.User;

public class PreferenceRequest extends Request {
	private final TripPreferences preference;
	public PreferenceRequest(RequestType type, User user, TripPreferences preference) {
		super(type, user);
		this.preference = preference;
		// TODO Auto-generated constructor stub
	}
	
	public TripPreferences getPreference() {
		return preference;
	}
}
