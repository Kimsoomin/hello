package kr.mintech.hello.beans;

public class ListViewItem {
    private String day;
    private String date;
    private String status;

    public ListViewItem(String day, String date, String status) {
        this.day = day;
        this.date = date;
        this.status = status;
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
}