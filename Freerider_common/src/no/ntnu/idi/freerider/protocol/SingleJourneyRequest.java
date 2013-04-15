package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.User;

public class SingleJourneyRequest extends Request{
	private final int journeySerial;
	public SingleJourneyRequest(RequestType type, User user, int journeySerial) {
		super(type, user);
		this.journeySerial = journeySerial;
	}
	public int getJourneySerial() {
		return journeySerial;
	}
}
