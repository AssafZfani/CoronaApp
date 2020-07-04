package zfani.assaf.corona_app.ui.select_country;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import zfani.assaf.corona_app.MainViewModel;
import zfani.assaf.corona_app.R;

public class SelectCountryFragment extends Fragment implements View.OnClickListener {

    private MainViewModel mainViewModel;
    private Button btnFromDate, btnToDate;
    private TabLayout tlCountries;
    private TextView tvEmptyListMessage;
    private StatusAdapter statusAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_country, container, false);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        btnFromDate = root.findViewById(R.id.btnFromDate);
        btnToDate = root.findViewById(R.id.btnToDate);
        btnFromDate.setOnClickListener(this);
        btnToDate.setOnClickListener(this);
        tlCountries = root.findViewById(R.id.tlCountries);
        for (String country : getResources().getStringArray(R.array.countries)) {
            tlCountries.addTab(tlCountries.newTab().setText(country)
                    .setIcon(getResources().getIdentifier("ic_" + country.toLowerCase(), "drawable", requireContext().getPackageName())));
        }
        tlCountries.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                initCountryStatuses(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tvEmptyListMessage = root.findViewById(R.id.tvEmptyListMessage);
        ((RecyclerView) root.findViewById(R.id.rvStatuses)).setAdapter(statusAdapter = new StatusAdapter());
        return root;
    }

    @Override
    public void onClick(View view) {
        new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
            ((Button) view).setText(new StringBuilder(day + "/" + (month + 1) + "/" + year));
            TabLayout.Tab tab = tlCountries.getTabAt(tlCountries.getSelectedTabPosition());
            if (tab != null) {
                initCountryStatuses(tab.getText());
            }
        }, 2020, 0, 1).show();
    }

    private void initCountryStatuses(CharSequence countryName) {
        String fromDate = btnFromDate.getText().toString(), endDate = btnToDate.getText().toString();
        if (countryName != null && !fromDate.isEmpty() && !endDate.isEmpty()) {
            if (mainViewModel.areValidDates(fromDate, endDate)) {
                mainViewModel.getCountryStatus(countryName.toString(), fromDate, endDate).observe(getViewLifecycleOwner(), statuses -> {
                    statusAdapter.submitList(statuses);
                    tvEmptyListMessage.setVisibility(statuses == null || statuses.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } else {
                statusAdapter.submitList(null);
                tvEmptyListMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}