package kr.mintech.weather.beans;

public class ListViewItem {
    private String icon;
    private String day;
    private String date;
    private String status;
    private String temperature;

    public ListViewItem(String day, String date, String status, String icon, String temperature) {
        this.day = day;
        this.date = date;
        this.status = status;
        this.icon = icon;
        this.temperature = temperature;
    }


    public String getTitle() {
        return this.day;
    }

    public String getDate() {
        return this.date;
    }

    public String getStatus() {
        return this.status;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getTemperature() { return this.temperature;}
}