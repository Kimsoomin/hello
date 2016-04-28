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
  private String dewpoint;
  private String humidity;
  private String windspeed;
  private String pressure;
  private String dust;

  public ListViewItem(){};
  public ListViewItem(String day, String date, String status, String icon, String temperature,
                      String sunriseTime, String sunsetTime, String dewpoint, String humidity, String windspeed, String pressure)
  {
    this.day = day;
    this.date = date;
    this.status = status;
    this.icon = icon;
    this.temperature = temperature;
    this.sunriseTime = sunriseTime;
    this.sunsetTime = sunsetTime;
    this.dewpoint = dewpoint;
    this.humidity = humidity;
    this.windspeed = windspeed;
    this.pressure = pressure;

  }

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

  public String getSunsetTime()
  {
    return this.sunsetTime;
  }

  public String getSunriseTime()
  {
    return this.sunriseTime;
  }

  public String getDewpoint()
  {
    return this.dewpoint;
  }

  public String getHumidity()
  {
    return this.humidity;
  }

  public String getWindspeed()
  {
    return this.windspeed;
  }


  public String getPressure()
  {
    return this.pressure;
  }

  public void setDust(String dust)
  {
    this.dust = dust;
  }

  public String getDust()
  {
    return this.dust;
  }
}