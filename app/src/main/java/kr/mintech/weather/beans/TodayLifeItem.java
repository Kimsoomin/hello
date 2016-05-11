package kr.mintech.weather.beans;

public class TodayLifeItem
{
  private String carwash;
  private String uv;
  private String laundry;
  private String discomfort;

  public TodayLifeItem()
  {
  }

  public TodayLifeItem(String carwash, String uv, String laundry, String discomfort)
  {
    this.carwash = carwash;
    this.uv = uv;
    this.laundry = laundry;
    this.discomfort = discomfort;

  }

  public String getCarwash()
  {
    return this.carwash;
  }

  public String getUv()
  {
    return this.uv;
  }

  public String getLaundry()
  {
    return this.laundry;
  }

  public String getDiscomfort()
  {
    return this.discomfort;
  }

}