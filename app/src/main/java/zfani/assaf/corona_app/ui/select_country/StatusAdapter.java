package zfani.assaf.corona_app.ui.select_country;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import zfani.assaf.corona_app.R;
import zfani.assaf.corona_app.model.Status;

public class StatusAdapter extends ListAdapter<Status, StatusViewHolder> {

    protected StatusAdapter() {
        super(new DiffUtil.ItemCallback<Status>() {
            @Override
            public boolean areItemsTheSame(@NonNull Status oldItem, @NonNull Status newItem) {
                return oldItem.getDate().equalsIgnoreCase(newItem.getDate());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Status oldItem, @NonNull Status newItem) {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
