package kr.mintech.weather.beans;

/**
 * Created by shakeJ on 16. 4. 12..
 */
public class Const
{
  public long lat = 37;
  public long lon = 127;

  public static final int L_KO = 0;
  public static final int L_EN = 1;
  public static final int L_ZH = 2;
  public static final int L_JP = 3;

  public static final String L_CODE_KO = "ko";
  public static final String L_CODE_EN = "en";
  public static final String L_CODE_ZH = "zh";
  public static final String L_CODE_JP = "jp";

  public static String getLanguageCodeFromValue(int value)
  {
    if (value == L_KO)
      return L_CODE_KO;
    else if (value == L_EN)
      return L_CODE_EN;
    else if (value == L_ZH)
      return L_CODE_ZH;
    else
      return L_CODE_JP;
  }

}
