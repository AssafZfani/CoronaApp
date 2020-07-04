package zfani.assaf.corona_app.ui.current_location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import zfani.assaf.corona_app.MainViewModel;
import zfani.assaf.corona_app.R;
import zfani.assaf.corona_app.ui.select_country.StatusViewHolder;

public class CurrentLocationFragment extends Fragment {

    MainViewModel mainViewModel;
    private TextView tvNoLocationMessage, tvDetails;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_current_location, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        tvNoLocationMessage = root.findViewById(R.id.tvNoLocationMessage);
        tvDetails = root.findViewById(R.id.tvDetails);
        initView(root);
        return root;
    }

    private void initView(View root) {
        tvNoLocationMessage.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10001);
        }
        mainViewModel.isLocationPermissionGranted().observe(getViewLifecycleOwner(), isLocationGranted -> {
            if (isLocationGranted || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.getFusedLocationProviderClient(requireActivity()).getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        tvNoLocationMessage.setVisibility(View.GONE);
                        try {
                            Address address = mainViewModel.getAddressByLocation(location);
                            if (address != null) {
                                mainViewModel.getCountryStatusByAddress(address).observe(getViewLifecycleOwner(), statuses -> {
                                    if (statuses != null && !statuses.isEmpty()) {
                                        StatusViewHolder statusViewHolder = new StatusViewHolder(root);
                                        statusViewHolder.bindData(statuses.get(0));
                                        statusViewHolder.tvConfirmed.setText(new StringBuilder(getString(R.string.confirmed) + ": " + statusViewHolder.tvConfirmed.getText()));
                                        statusViewHolder.tvRecovered.setText(new StringBuilder(getString(R.string.recovered) + ": " + statusViewHolder.tvRecovered.getText()));
                                        statusViewHolder.tvDeaths.setText(new StringBuilder(getString(R.string.deaths) + ": " + statusViewHolder.tvDeaths.getText()));
                                    }
                                    tvDetails.setText(getString(R.string.current_location_details_message, address.getCountryName()));
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}