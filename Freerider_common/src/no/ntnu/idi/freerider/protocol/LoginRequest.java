package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.User;

/** A Request subclass used to log a user into the system. */
public class LoginRequest extends Request {
	private final String accessToken;

	public LoginRequest( User user, String accessToken) {
		super(RequestType.LOGIN, user);
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}
	

}
