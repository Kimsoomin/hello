package kr.mintech.weather.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.database.DataSetObservable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.mintech.weather.R;
import kr.mintech.weather.api.APIRequest;
import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.common.PlanetXSDKConstants;
import kr.mintech.weather.common.PlanetXSDKException;
import kr.mintech.weather.common.RequestBundle;
import kr.mintech.weather.common.RequestListener;
import kr.mintech.weather.common.ResponseMessage;
import kr.mintech.weather.controllers.CardViewListViewAdapter;
import kr.mintech.weather.managers.PreferenceManager;

/**
 * Created by Mac on 16. 5. 17..
 */
public class TodayFragment extends Fragment
{
  private double latitude;
  private double longitude;

  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  private Context context;

  private static int LCOATION_TIME_OUT_SECOND = 30 * 1000;
  private Handler handler = new Handler();
  private Activity activity;

  private LocationManager locationManager;
  private LocationListener locationListener;
  private String topAddress;
  private ProgressBar progressbar;
  private TextView text;
  private LinearLayout placePicker;

  //  ============== card view ==================

  public static View view;
  private static CardViewListViewAdapter adapter;

  private ArrayList<ListViewItem> items;


  // ============ 미세먼지 sk planet ============
  APIRequest api;
  RequestBundle requestBundle;
  // http://apis.skplanetx.com/melon/newreleases/songs?version={version}&page={page}&count={count}
  // String URL = "http://apis.skplanetx.com/melon/newreleases/songs";
  String URL = "http://apis.skplanetx.com/weather/dust";
  Map<String, Object> param;

  String hndResult = "";
  String test;
  String dust;
  String valueInit;
  String value;

  // ============ 세차 지수 sk planet ============
  APIRequest api_carwash;
  RequestBundle requestBundle_carwash;
  String URL_carwash = "http://apis.skplanetx.com/weather/windex/carwash";
  Map<String, Object> param_carwash;
  String hndResult_carwash = "";

  // ============ 자외선 지수 sk planet ============
  APIRequest api_uv;
  RequestBundle requestBundle_uv;
  String URL_uv = "http://apis.skplanetx.com/weather/windex/uvindex";
  Map<String, Object> param_uv;
  String hndResult_uv = "";

  // ============ 빨래 지수 sk planet ============
  APIRequest api_laundry;
  RequestBundle requestBundle_laundry;
  String URL_laundry = "http://apis.skplanetx.com/weather/windex/laundry";
  Map<String, Object> param_laundry;
  String hndResult_laundry = "";

  // ============ 불쾌 지수 sk planet ============
  APIRequest api_discomfort;
  RequestBundle requestBundle_discomfort;
  String URL_discomfort = "http://apis.skplanetx.com/weather/windex/thindex";
  Map<String, Object> param_discomfort;
  String hndResult_discomfort = "";

  // ============ 알림바 ============
  NotificationManager nm;

  // =========== 언어설정 ==============
  String language;

  // =========== Setting Activity ===========
  String icon;
  String status;

  // ============= Today Life ==============
  TextView carwashComment;
  TextView uvComment;
  TextView laundryComment;
  TextView discomfortComment;

  String carwashResult;
  String uvResult;
  String laundryResult;
  String discomfortResult;
  String discomfortIndex;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // Activity 데이터통신 테스트
    Bundle bundle = this.getArguments();
    String myValue = bundle.getString("test");
    language = Locale.getDefault().getLanguage();
    int resId = R.layout.fragment_today;
    return inflater.inflate(resId, null);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);

    //    ListView listview = (ListView) getView().findViewById(R.id.listview);
    //    adapter = new CardViewListViewAdapter();
    //    listview.setAdapter(adapter);
    latitude = Double.longBitsToDouble(PreferenceManager.getInstance(getActivity()).getLat());
    longitude = Double.longBitsToDouble(PreferenceManager.getInstance(getActivity()).getLon());

    TextView carwash = (TextView) getView().findViewById(R.id.carwash);
    TextView uv = (TextView) getView().findViewById(R.id.uv);
    TextView laundry = (TextView) getView().findViewById(R.id.laundry);
    TextView discomfort = (TextView) getView().findViewById(R.id.discomfort);

    carwashComment = (TextView) getView().findViewById(R.id.carwash_comment);
    uvComment = (TextView) getView().findViewById(R.id.uv_comment);
    laundryComment = (TextView) getView().findViewById(R.id.laundry_comment);
    discomfortComment = (TextView) getView().findViewById(R.id.discomfort_comment);

    carwash.setText("세차 지수");
    uv.setText("자외선 지수");
    laundry.setText("빨래 지수");
    discomfort.setText("불쾌 지수");

    // =========================미 세 먼 지=============================
    api = new APIRequest();
    APIRequest.setAppKey("8aa2f9e4-0120-333f-add1-a714d569a1e9");

    // url에 삽입되는 파라미터 설정
    param = new HashMap<String, Object>();
    param.put("version", "1");
    param.put("lat", latitude);
    param.put("lon", longitude);

    // 호출시 사용될 값 설정
    requestBundle = new RequestBundle();
    requestBundle.setUrl(URL);
    requestBundle.setParameters(param);
    requestBundle.setHttpMethod(PlanetXSDKConstants.HttpMethod.GET);
    requestBundle.setResponseType(PlanetXSDKConstants.CONTENT_TYPE.JSON);

    RequestListener reqListener = new RequestListener()
    {
      @Override
      public void onPlanetSDKException(PlanetXSDKException e)
      {
        hndResult = e.toString();
      }

      @Override
      public void onComplete(ResponseMessage result)
      {
        // 응답을 받아 메시지 핸들러에 알려준다.
        hndResult = result.getStatusCode() + "\n" + result.toString();
        test = hndResult.split("grade")[1];
        valueInit = hndResult.split("value")[1];
        value = valueInit.substring(3, 7);
        dust = test.substring(3, 5);

        Log.d("어디", "=========== value ===========  " + valueInit);
        Log.d("어디", "=========== value ===========  " + value);

        if (language.contains("en"))
        {
          if (dust.contains("매우 높음"))
          {
            dust = "Very High";
          }
          else if (dust.equals("높음"))
          {
            dust = "High";
          }
          else if (dust.contains("보통"))
          {
            dust = "Middle";
          }
          else if (dust.equals("낮음"))
          {
            dust = "Low";
          }
          else if (dust.contains("매우 낮음"))
          {
            dust = "Very Low";
          }
        }

        adapter.add(dust, value);
        PreferenceManager.getInstance(getActivity()).setDust(dust);
        PreferenceManager.getInstance(getActivity()).setValue(value);
        // 저장할 값들을 입력합니다.
        //        editor.putString("dust", dust);
        //        editor.putString("value", value);
        //        editor.commit();
      }
    };

    try
    {
      // 비동기 호출
      api.request(requestBundle, reqListener);
    } catch (PlanetXSDKException e)
    {
      e.printStackTrace();
    }

    //    ===================세차 지수===========================

    api_carwash = new APIRequest();
    APIRequest.setAppKey("8aa2f9e4-0120-333f-add1-a714d569a1e9");

    // url에 삽입되는 파라미터 설정
    param_carwash = new HashMap<String, Object>();
    param_carwash.put("version", "1");

    // 호출시 사용될 값 설정
    requestBundle_carwash = new RequestBundle();
    requestBundle_carwash.setUrl(URL_carwash);
    requestBundle_carwash.setParameters(param_carwash);
    requestBundle_carwash.setHttpMethod(PlanetXSDKConstants.HttpMethod.GET);
    requestBundle_carwash.setResponseType(PlanetXSDKConstants.CONTENT_TYPE.JSON);

    RequestListener reqListener_carwash = new RequestListener()
    {
      @Override
      public void onPlanetSDKException(PlanetXSDKException e)
      {
        hndResult_carwash = e.toString();
      }

      @Override
      public void onComplete(ResponseMessage result)
      {
        // 응답을 받아 메시지 핸들러에 알려준다.
        hndResult_carwash = result.getStatusCode() + "\n" + result.toString();
        String carwash_comment = hndResult_carwash.split("comment")[1];
        String carwash = carwash_comment.substring(3, carwash_comment.indexOf("}"));
        carwashResult = carwash.substring(0, carwash.length() - 1);

        PreferenceManager.getInstance(getActivity()).setCarwashResult(carwashResult);
      }
    };

    try
    {
      // 비동기 호출
      api_carwash.request(requestBundle_carwash, reqListener_carwash);
    } catch (PlanetXSDKException e)
    {
      e.printStackTrace();
    }

    //    =================== 자외선 지수 ===========================

    api_uv = new APIRequest();
    APIRequest.setAppKey("8aa2f9e4-0120-333f-add1-a714d569a1e9");

    // url에 삽입되는 파라미터 설정
    param_uv = new HashMap<String, Object>();
    param_uv.put("version", "1");
    param_uv.put("lat", latitude);
    param_uv.put("lon", longitude);

    // 호출시 사용될 값 설정
    requestBundle_uv = new RequestBundle();
    requestBundle_uv.setUrl(URL_uv);
    requestBundle_uv.setParameters(param_uv);
    requestBundle_uv.setHttpMethod(PlanetXSDKConstants.HttpMethod.GET);
    requestBundle_uv.setResponseType(PlanetXSDKConstants.CONTENT_TYPE.JSON);

    RequestListener reqListener_uv = new RequestListener()
    {
      @Override
      public void onPlanetSDKException(PlanetXSDKException e)
      {
        hndResult_uv = e.toString();
      }

      @Override
      public void onComplete(ResponseMessage result)
      {
        // 응답을 받아 메시지 핸들러에 알려준다.
        hndResult_uv = result.getStatusCode() + "\n" + result.toString();
        String uv_comment = hndResult_uv.split("comment")[1];
        String uv = uv_comment.substring(3, uv_comment.indexOf(","));
        uvResult = uv.substring(0, uv.length() - 1);

        PreferenceManager.getInstance(getActivity()).setUvResult(uvResult);
      }
    };

    try
    {
      // 비동기 호출
      api_uv.request(requestBundle_uv, reqListener_uv);
    } catch (PlanetXSDKException e)
    {
      e.printStackTrace();
    }

    //    =================== 빨래 지수 ===========================

    api_laundry = new APIRequest();
    APIRequest.setAppKey("8aa2f9e4-0120-333f-add1-a714d569a1e9");

    // url에 삽입되는 파라미터 설정
    param_laundry = new HashMap<String, Object>();
    param_laundry.put("version", "1");
    param_laundry.put("lat", latitude);
    param_laundry.put("lon", longitude);

    // 호출시 사용될 값 설정
    requestBundle_laundry = new RequestBundle();
    requestBundle_laundry.setUrl(URL_laundry);
    requestBundle_laundry.setParameters(param_laundry);
    requestBundle_laundry.setHttpMethod(PlanetXSDKConstants.HttpMethod.GET);
    requestBundle_laundry.setResponseType(PlanetXSDKConstants.CONTENT_TYPE.JSON);

    RequestListener reqListener_laundry = new RequestListener()
    {
      @Override
      public void onPlanetSDKException(PlanetXSDKException e)
      {
        hndResult_laundry = e.toString();
      }

      @Override
      public void onComplete(ResponseMessage result)
      {
        //         응답을 받아 메시지 핸들러에 알려준다.
        hndResult_laundry = result.getStatusCode() + "\n" + result.toString();
        String laundry_comment = hndResult_laundry.split("comment")[1];
        String laundry = laundry_comment.substring(3, laundry_comment.indexOf(","));
        laundryResult = laundry.substring(0, laundry.length() - 1);

        PreferenceManager.getInstance(getActivity()).setUvResult(laundryResult);
      }
    };

    try
    {
      // 비동기 호출
      api_laundry.request(requestBundle_laundry, reqListener_laundry);
    } catch (PlanetXSDKException e)
    {
      e.printStackTrace();
    }

    //    =================== 불쾌 지수 ===========================

    api_discomfort = new APIRequest();
    APIRequest.setAppKey("8aa2f9e4-0120-333f-add1-a714d569a1e9");

    // url에 삽입되는 파라미터 설정
    param_discomfort = new HashMap<String, Object>();
    param_discomfort.put("version", "1");
    param_discomfort.put("lat", latitude);
    param_discomfort.put("lon", longitude);

    // 호출시 사용될 값 설정
    requestBundle_discomfort = new RequestBundle();
    requestBundle_discomfort.setUrl(URL_discomfort);
    requestBundle_discomfort.setParameters(param_discomfort);
    requestBundle_discomfort.setHttpMethod(PlanetXSDKConstants.HttpMethod.GET);
    requestBundle_discomfort.setResponseType(PlanetXSDKConstants.CONTENT_TYPE.JSON);

    RequestListener reqListener_discomfort = new RequestListener()
    {
      @Override
      public void onPlanetSDKException(PlanetXSDKException e)
      {
        hndResult_discomfort = e.toString();
      }

      @Override
      public void onComplete(ResponseMessage result)
      {
        // 응답을 받아 메시지 핸들러에 알려준다.
        hndResult_discomfort = result.getStatusCode() + "\n" + result.toString();
        String discomfort_forecast = hndResult_discomfort.split("forecast")[1];
        String discomfort_4hour = discomfort_forecast.split("index4hour")[1];
        String discomfort = discomfort_4hour.substring(3, discomfort_4hour.indexOf(","));
        discomfortResult = discomfort.substring(0, discomfort.length() - 1);
        //        Log.d("어디", "=========== discomfort_result ===========" + discomfortResult);

        PreferenceManager.getInstance(getActivity()).setUvResult(discomfortResult);
      }
    };

    try
    {
      // 비동기 호출
      api_discomfort.request(requestBundle_discomfort, reqListener_discomfort);
    } catch (PlanetXSDKException e)
    {
      e.printStackTrace();
    }

    //    carwashResult = pref.getString("carwashResult", "?");
    //    uvResult = pref.getString("uvResult", "?");
    //    laundryResult = pref.getString("laundryResult", "?");
    //    discomfortResult = pref.getString("discomfortResult", "?");

    carwashResult = PreferenceManager.getInstance(getActivity()).getCarwashResult();
    uvResult = PreferenceManager.getInstance(getActivity()).getUvResult();
    laundryResult = PreferenceManager.getInstance(getActivity()).getLaundryResult();
    discomfortResult = PreferenceManager.getInstance(getActivity()).getDiscomfortResult();


    double discomfortValue = 0.0;

    if (discomfortResult != null | discomfort.equals(""))
    {
      discomfortValue = Double.parseDouble(discomfortResult.trim());
    }

    Log.d("어디", "============ discomfortValue ===========" + discomfortValue);
    if (discomfortValue >= 80.0)
    {
      discomfortIndex = "매우 높음";
    }
    else if (discomfortValue < 80.0 & discomfortValue >= 75.0)
    {
      discomfortIndex = "높음";
    }
    else if (discomfortValue < 75.0 & discomfortValue >= 68.0)
    {
      discomfortIndex = "보통";
    }
    else if (discomfortValue < 68.0)
    {
      discomfortIndex = "낮음";
    }

    carwashComment.setText(carwashResult);
    uvComment.setText(uvResult);
    laundryComment.setText(laundryResult);
    discomfortComment.setText(discomfortResult + " : " + discomfortIndex);

  }

  @Override
  public void onResume()
  {
    super.onResume();
  }


  @Override
  public void onPause()
  {
    super.onPause();
  }
}
