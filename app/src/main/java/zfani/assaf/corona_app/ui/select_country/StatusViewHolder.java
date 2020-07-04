package zfani.assaf.corona_app.ui.select_country;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zfani.assaf.corona_app.R;
import zfani.assaf.corona_app.model.Status;

public class StatusViewHolder extends RecyclerView.ViewHolder {

    public final TextView tvDate, tvConfirmed, tvRecovered, tvDeaths;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.tvDate);
        tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
        tvRecovered = itemView.findViewById(R.id.tvRecovered);
        tvDeaths = itemView.findViewById(R.id.tvDeaths);
    }

    public void bindData(Status status) {
        if (tvDate != null) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(status.getDate().replace("T00:00:00Z", ""));
                if (date != null) {
                    tvDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        tvConfirmed.setText(status.getConfirmed());
        tvRecovered.setText(status.getRecovered());
        tvDeaths.setText(status.getDeaths());
    }
}
