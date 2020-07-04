package zfani.assaf.corona_app.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("country/{country_name}")
    Call<List<Status>> getCountryStatus(@Path("country_name") String countryName, @Query("from") String fromDate, @Query("to") String endDate);
}
