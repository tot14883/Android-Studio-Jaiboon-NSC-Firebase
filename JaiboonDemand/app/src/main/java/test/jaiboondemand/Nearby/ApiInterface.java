package test.jaiboondemand.Nearby;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 12/30/2017.
 */

public interface ApiInterface {
    @GET("place/nearbysearch/json?")
    Call<PlacesPOJO.Root> doPlaces(@Query(value = "keyword", encoded = true) String keyword, @Query(value = "radius", encoded = true) String radius, @Query(value = "location", encoded = true) String location, @Query(value = "key", encoded = true) String key);


    @GET("distancematrix/json") // origins/destinations:  LatLng as string
    Call<ResultDistanceMatrix> getDistance(@Query("key") String key, @Query("origins") String origins, @Query("destinations") String destinations);

}
