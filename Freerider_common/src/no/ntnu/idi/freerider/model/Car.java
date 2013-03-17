package no.ntnu.idi.freerider.model;
import no.ntnu.idi.freerider.xml.Base64;

public class Car{

	public int getCarId()
	{
		return carId;
	}

	public void setCarId(int carId)
	{
		this.carId = carId;
	}
	int carId;
    String carName;
    double comfort;
    byte[] photo;
    public Car(int carId,String carName, double comfort){
        this.carId = carId;
    	this.carName = carName;
        this.comfort = comfort;
        String dummyString = "null";
        this.photo = dummyString.getBytes();
    }

    public Car(int carId, String carName, double comfort, byte[] photo){
        this.carId = carId;
    	this.carName = carName;
        this.comfort = comfort;
        this.photo = photo;
    }

    public String getCarName(){
        return carName;
    }

    public void setCarName(String carName){
        this.carName = carName;
    }

    public double getComfort(){
        return comfort;
    }

    public void setComfort(double comfort){
        this.comfort = comfort;
    }

    public byte[] getPhoto(){
        return photo;
    }

    public void setPhoto(byte[] photo){
        this.photo = photo;
    }
    public String getPhotoAsBase64(){
    	return Base64.encodeToString(this.photo, Base64.DEFAULT);
    }
    public void setPhotoAsBase64(String input){
    	this.photo = Base64.decode(input, Base64.DEFAULT);
    }
}
