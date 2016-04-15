package kr.mintech.weather.managers;

import android.content.Context;

import kr.mintech.weather.beans.Const;

public class PreferenceManager extends BasePreferenceManager
{
  private static PreferenceManager instance;
  private static Const con;


  public static PreferenceManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (PreferenceManager.class)
      {
        if (instance == null)
          instance = new PreferenceManager(context);
      }
    }
    return instance;
  }


  private PreferenceManager(Context context)
  {
    super(context);
  }

  private static final String KEY_LAT = "key_lat";
  private static final String KEY_LON = "key_lon";


  public void setLat(double lat)
  {
    putLat(KEY_LAT, lat);
  }

  public long getLat()
  {
    return getLat(KEY_LAT, 37);
  }

  public void setLon(double lon) {putLon(KEY_LON, lon);
  }

  public long getLon()
  {
    return getLon(KEY_LON, 127);
  }

}
