package test.jaiboondemand.Nearby;

/**
 * Created by User on 12/30/2017.
 */

public class PlaceModel {

    public String name, address, distance, duration;

    public PlaceModel(){}
    public PlaceModel(String name, String address, String distance, String duration) {

        this.name = name;
        this.address = address;
        this.distance = distance;
        this.duration = duration;
    }

}
