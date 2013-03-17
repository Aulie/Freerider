package no.ntnu.idi.freerider.protocol;

import no.ntnu.idi.freerider.model.Car;

public class CarResponse extends Response
{
	private final Car car;
	public CarResponse(RequestType type, ResponseStatus status,Car car) {
		super(type, status);
		this.car = car;
	}
	protected CarResponse(RequestType type,ResponseStatus status, String errorMessage,Car car) {
		super(type, status, errorMessage);
		this.car = car;
	}
	public Car getCar()
	{
		return car;
	}
	@Override
	public String toString() {
		String ret = super.toString();
		ret += ", car=" + (car != null ? "NULL" : "");
		return ret;
	}
}
