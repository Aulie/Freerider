package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.TripPreferences;

public class PreferenceResponse extends Response{
	private final TripPreferences preference;
	public PreferenceResponse(RequestType type, ResponseStatus status,TripPreferences preference) {
		super(type, status);
		this.preference = preference;
	}
	protected PreferenceResponse(RequestType type,ResponseStatus status, String errorMessage,TripPreferences preference) {
		super(type, status, errorMessage);
		this.preference = preference;
	}
	public TripPreferences getPreferences() {
		return this.preference;
	}
	@Override
	public String toString() {
		String ret = super.toString();
		ret += ", preferences=" + (preference != null ? "NULL" : "");
		return ret;
	}
}
