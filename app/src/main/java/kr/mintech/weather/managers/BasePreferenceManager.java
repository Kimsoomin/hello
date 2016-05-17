package kr.mintech.weather.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BasePreferenceManager
{
  protected static SharedPreferences _preference;


  protected BasePreferenceManager(Context context)
  {
    super();
    _preference = PreferenceManager.getDefaultSharedPreferences(context);
  }


  /**
   * key 수동 설정
   *
   * @param key   키 값
   * @param value 내용
   */
  protected static void put(String key, String value)
  {
    SharedPreferences p = _preference;
    SharedPreferences.Editor editor = p.edit();
    editor.putString(key, value);
    editor.commit();
  }


  /**
   * String 값 가져오기
   *
   * @param key 키 값
   * @return String (기본값 null)
   */
  protected static String get(String key)
  {
    SharedPreferences p = _preference;
    return p.getString(key, null);
  }


  /**
   * String 값 가져오기
   *
   * @param key 키 값
   * @return String (기본값 "")
   */
  protected static String getWithNullToBlank(String key)
  {
    SharedPreferences p = _preference;
    return p.getString(key, "");
  }


  /**
   * key 설정
   *
   * @param key   키 값
   * @param value 내용
   */
  protected static void put(String key, boolean value)
  {
    SharedPreferences p = _preference;
    SharedPreferences.Editor editor = p.edit();
    editor.putBoolean(key, value);
    editor.commit();
  }


  /**
   * Boolean 값 가져오기
   *
   * @param key          키 값
   * @param defaultValue 기본값
   * @return Boolean
   */
  protected static boolean get(String key, boolean defaultValue)
  {
    SharedPreferences p = _preference;
    return p.getBoolean(key, defaultValue);
  }


  /**
   * key 설정
   *
   * @param key   키 값
   * @param value 내용
   */
  protected static void put(String key, int value)
  {
    SharedPreferences p = _preference;
    SharedPreferences.Editor editor = p.edit();
    editor.putInt(key, value);
    editor.commit();
  }


  /**
   * int 값 가져오기
   *
   * @param key          키 값
   * @param defaultValue 기본값
   * @return int
   */
  protected static int get(String key, int defaultValue)
  {
    SharedPreferences p = _preference;
    return p.getInt(key, defaultValue);
  }

  /**
   * key 설정
   *
   * @param key   키 값
   * @param value 내용
   */
  protected static void put(String key, long value)
  {
    SharedPreferences p = _preference;
    SharedPreferences.Editor editor = p.edit();
    editor.putLong(key, value);
    editor.commit();
  }


  /**
   * int 값 가져오기
   *
   * @param key          키 값
   * @param defaultValue 기본값
   * @return int
   */
  protected static long getLong(String key, long defaultValue)
  {
    SharedPreferences p = _preference;
    return p.getLong(key, defaultValue);
  }
}