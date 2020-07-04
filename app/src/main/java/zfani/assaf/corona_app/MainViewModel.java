package zfani.assaf.corona_app;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;
import zfani.assaf.corona_app.model.ApiClient;
import zfani.assaf.corona_app.model.ApiInterface;
import zfani.assaf.corona_app.model.Status;

public class MainViewModel extends AndroidViewModel {

    private static final String url = "https://api.covid19api.com/";
    private MutableLiveData<Boolean> isLocationPermissionGranted, isBluetoothEnabled;

    public MainViewModel(@NonNull Application application) {
        super(application);
        isLocationPermissionGranted = new MutableLiveData<>(false);
        isBluetoothEnabled = new MutableLiveData<>(false);
    }

    @EverythingIsNonNull
    public LiveData<List<Status>> getCountryStatus(String countryName, String fromDate, String endDate) {
        MutableLiveData<List<Status>> statusList = new MutableLiveData<>();
        ApiClient.getClient(url).create(ApiInterface.class).getCountryStatus(countryName, getParsingDate(fromDate), getParsingDate(endDate)).enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                statusList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {

            }
        });
        return statusList;
    }

    private String getParsingDate(String dateStr) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr);
            if (date != null) {
                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean areValidDates(String fromDate, String endDate) {
        try {
            Date from = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fromDate);
            Date end = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(endDate);
            if (from != null && end != null) {
                return from.before(end);
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    public Address getAddressByLocation(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(getApplication(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (addresses != null && !addresses.isEmpty()) {
            return addresses.get(0);
        }
        return null;
    }

    public LiveData<List<Status>> getCountryStatusByAddress(Address address) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String yesterday = simpleDateFormat.format(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        String today = simpleDateFormat.format(System.currentTimeMillis());
        return getCountryStatus(address.getCountryCode(), yesterday, today);
    }

    public MutableLiveData<Boolean> isLocationPermissionGranted() {
        return isLocationPermissionGranted;
    }

    public void setIsLocationPermissionGranted(boolean isLocationPermissionGranted) {
        this.isLocationPermissionGranted.setValue(isLocationPermissionGranted);
    }

    public MutableLiveData<Boolean> isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    public void setIsBluetoothEnabled(boolean isBluetoothEnabled) {
        this.isBluetoothEnabled.setValue(isBluetoothEnabled);
    }
}
