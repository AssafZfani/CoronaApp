package zfani.assaf.corona_app.model;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("Confirmed")
    private String confirmed;
    @SerializedName("Recovered")
    private String recovered;
    @SerializedName("Deaths")
    private String deaths;
    @SerializedName("Date")
    private String date;

    public String getConfirmed() {
        return confirmed;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getDate() {
        return date;
    }
}
