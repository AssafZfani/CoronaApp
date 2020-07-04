package zfani.assaf.corona_app.ui.scan_nearby;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Set;

import zfani.assaf.corona_app.MainViewModel;
import zfani.assaf.corona_app.R;

public class ScanNearByFragment extends Fragment {

    private MainViewModel mainViewModel;
    private TextView tvBluetoothMessage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan_nearby, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        tvBluetoothMessage = root.findViewById(R.id.tvBluetoothdMessage);
        initView();
        return root;
    }

    private void initView() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                requireActivity().startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 10002);
            }
            mainViewModel.isBluetoothEnabled().observe(getViewLifecycleOwner(), isBluetoothEnabled -> {
                if (isBluetoothEnabled || bluetoothAdapter.isEnabled()) {
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        boolean foundDevice = false;
                        for (BluetoothDevice device : pairedDevices) {
                            if (foundDevice = device.getAddress().equalsIgnoreCase("A0:14:3D:96:FF:5E")) {
                                break;
                            }
                        }
                        tvBluetoothMessage.setText(getString(R.string.bluetooth_detected_message, (foundDevice ? "an" : "no")));
                        tvBluetoothMessage.setTextColor(getResources().getColor(foundDevice ? R.color.colorPrimary : android.R.color.holo_green_dark));
                    } else {
                        tvBluetoothMessage.setText(R.string.empty_bluetooth_list_message);
                    }
                }
            });
        }
    }
}