package kr.mintech.weather.beans;

public class ListViewItem
{
  private String icon;
  private String day;
  private String date;
  private String status;
  private String temperature;
  private String sunriseTime;
  private String sunsetTime;
  public double lon = -1, lat = -1;

  public ListViewItem(String day, String date, String status, String icon, String temperature, String sunriseTime, String sunsetTime)
  {
    this.day = day;
    this.date = date;
    this.status = status;
    this.icon = icon;
    this.temperature = temperature;
    this.sunriseTime = sunriseTime;
    this.sunsetTime = sunsetTime;
  }


  public String getTitle()
  {
    return this.day;
  }

  public String getDate()
  {
    return this.date;
  }

  public String getStatus()
  {
    return this.status;
  }

  public String getIcon()
  {
    return this.icon;
  }

  public String getTemperature()
  {
    return this.temperature;
  }

  public String getSunsetTime(){
    return this.sunsetTime;
  }

  public String getSunriseTime()
  {
    return this.sunriseTime;
  }

}