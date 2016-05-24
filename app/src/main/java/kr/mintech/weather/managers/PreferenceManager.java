package kr.mintech.weather.managers;

import android.content.Context;

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

  private static final String KEY_IS_FIRST = "key_is_first";
  //유저정보 추가
  private static final String KEY_USER_ID = "key_user_id";
  private static final String KEY_USER_LOGIN_ID = "key_user_login_id";
  private static final String KEY_USER_NAME = "key_user_name";
  private static final String KEY_USER_PASSWORD = "key_user_password";
  private static final String KEY_USER_EMAIL = "key_user_email";
  private static final String KEY_USER_PROFILE_IMAGE = "key_user_profile_image";
  private static final String KEY_ACCESS_TOKEN = "key_access_token";
  private static final String KEY_DEVICE_TOKEN = "key_device_token";
  private static final String KEY_DONT_ASK_NAME_EMPTY = "key_dont_ask_name_empty"; // 로그인할 때 이름이나 이메일이 비어있을 때 바꿀지 물어보기
  private static final String KEY_USER_PHONE = "key_user_phone";

  private static final String KEY_MY_TASK_SHOW_CONFIRMED = "key_my_task_show_confirmed";
  private static final String KEY_MY_TASK_SORT_TYPE = "key_my_task_sort_type";
  private static final String KEY_MY_GCM_KEY = "key_my_gcm_key";
  private static final String KEY_SHOW_SYSTEM_MESSAGE = "key_show_system_message";

  private static final String KEY_MODE_CALENDAR = "key_mode_calendar";

  private static final String KEY_ENABLE_SOUND = "key_enable_sound";
  private static final String KEY_ENABLE_VIBRATE = "key_enable_vibrate";
  private static final String KEY_ENABLE_NON_ALARM_TIME = "key_enable_non_alarm_time";
  private static final String KEY_NON_ALARM_TIME = "key_non_alarm_time";


  private static final String KEY_TASK_FILTER = "key_task_filter_v1";
  private static final String KEY_TASK_SORT = "key_task_sort_v1";
  private static final String KEY_TASK_FILTER_STATUS = "key_task_filter_status";

  private static final String KEY_ALARM_DAY = "key_alarm_day";

  //  ===================== Weather ========================

  public void setDust(String dust)
  {
    put("dust", dust);
  }

  public String getDust()
  {
    return get("dust");
  }

  public void setValue(String value)
  {
    put("value", value);
  }

  public String getValue()
  {
    return get("value");
  }

  public void setCarwashResult(String carwashResult)
  {
    put("carwashResult", carwashResult);
  }

  public String getCarwashResult()
  {
    return get("carwashResult");
  }

  public void setUvResult(String uvResult)
  {
    put("uvResult", uvResult);
  }

  public String getUvResult()
  {
    return get("uvResult");
  }

  public void setLaundryResult(String laundryResult)
  {
    put("laundryResult", laundryResult);
  }

  public String getLaundryResult()
  {
    return get("laundryResult");
  }

  public void setDiscomfortResult(String discomfortResult)
  {
    put("discomfortResult", discomfortResult);
  }

  public String getDiscomfortResult()
  {
    return get("discomfortResult");
  }

  public void setLat(long lat)
  {
    put("lat", lat);
  }

  public long getLat()
  {
    return getLong("lat", 37);
  }

  public void setLon(long lon)
  {
    put("lon", lon);
  }

  public long getLon()
  {
    return getLong("lon", 127);
  }


  public void setIcon(String icon)
  {
    put("icon", icon);
  }

  public String getIcon()
  {
    return get("icon");
  }

  public void setStatus(String status)
  {
    put("status", status);
  }

  public String getStatus()
  {
    return get("status");
  }

  public void setAddressInit(String addressInit)
  {
    put("addressInit", addressInit);
  }

  public String getAddressInit()
  {
    return get("addressInit");
  }

  public void setVibrate(boolean vibrate)
  {
    put("vibrate", vibrate);
  }

  public boolean getVibrate()
  {
    return get("vibrate", false);
  }

  //  PreferenceManager.getInstance(getApplicationContext()).setUserId(String.valueOf(user.getId()));
  //  PreferenceManager.getInstance(LoginActivity.this).getUserLoginId())
}
