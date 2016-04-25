package kr.mintech.weather.managers;

import android.content.Context;

import kr.mintech.weather.beans.Const;

public class PreferenceManager extends BasePreferenceManager
{
  private static PreferenceManager instance;


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

  private static final String KEY_SELECTED_LANGUAGE = "key_selected_language";


  public void setSelectedLanguage(int value)
  {
    put(KEY_SELECTED_LANGUAGE, value);
  }

  public int getSelectedLanguage()
  {
    return get(KEY_SELECTED_LANGUAGE, Const.L_EN);
  }

}
