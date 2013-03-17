package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.Car;
import no.ntnu.idi.freerider.model.User;

public class CarRequest extends Request {
	private final Car car;
	public CarRequest(RequestType type, User user, Car car){
		super(type, user);
		this.car = car;
	}
	public Car getCar()
	{
		return car;
	}
	
}
