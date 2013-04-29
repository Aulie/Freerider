/*******************************************************************************
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @contributor(s): Freerider Team 2 (Group 3, IT2901 Spring 2013, NTNU)
 * @version: 2.0
 * 
 * Copyright 2013 Freerider Team 2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package no.ntnu.idi.freerider.model;
import no.ntnu.idi.freerider.xml.Base64;

/**
 * Object for storing car info
 * @author Thomas Gjerde
 *
 */
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
