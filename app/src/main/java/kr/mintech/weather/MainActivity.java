package kr.mintech.weather;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.DataSetObservable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.mintech.weather.api.APIRequest;
import kr.mintech.weather.beans.Const;
import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.common.PlanetXSDKConstants;
import kr.mintech.weather.common.PlanetXSDKException;
import kr.mintech.weather.common.RequestBundle;
import kr.mintech.weather.common.RequestListener;
import kr.mintech.weather.common.ResponseMessage;
import kr.mintech.weather.controllers.CardViewListViewAdapter;
import kr.mintech.weather.managers.PreferenceManager;

public class MainActivity extends AppCompatActivity
{
  public static View view;
  private static CardViewListViewAdapter adapter;

  //  Locale systemLocale = getResources().getConfiguration().locale;
  //  String strLanguage = systemLocale.getLanguage();

  private double latitude;
  private double longitude;

  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  private Context context;
  private PreferenceManager preferenceManager;

  private static int LCOATION_TIME_OUT_SECOND = 30 * 1000;
  private Handler handler = new Handler();
  private Activity activity;

  private LocationManager locationManager;
  private LocationListener locationListener;
  private String address;
  private ProgressBar progressbar;
  private TextView text;
  private Dialog dialog;

  //  ============== card view ==================

  public static RecyclerView mRecyclerView;
  public static RecyclerView mRecyclerViewMain;
  private static RecyclerView.Adapter mAdapter;
  private static RecyclerView.Adapter mAdapterMain;

  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView.LayoutManager mLayoutManagerMain;
  private static ArrayList<ListViewItem> listViewItems;

  // =============== navi draw ==================

  private String[] navItems = {"Setting", "sk planet 미세먼지"};
  private ListView lvNavList;
  private DrawerLayout mDrawerLayout; // 주 기능
  private ActionBarDrawerToggle mDrawerToggle; // 주 기능
  private ActionBar actionBar;

  // placePicker

  private static final int PLACE_PICKER_REQUEST = 1;
  private TextView mName;
  private TextView mAddress;
  private TextView mAttributions;
  private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
  private String str;
  private String lat;
  private String lon;

  // 미세먼지 sk planet
  APIRequest api;
  RequestBundle requestBundle;
  // http://apis.skplanetx.com/melon/newreleases/songs?version={version}&page={page}&count={count}
  // String URL = "http://apis.skplanetx.com/melon/newreleases/songs";
  String URL = "http://apis.skplanetx.com/weather/dust";
  Map<String, Object> param;

  String hndResult = "";
  String test;
  String dust;

  // 알림바
  NotificationManager nm;
  private static final String INTENT_ACTION = "kr.mintech.weather.MainActivity";
  private static AlarmReceive receive = new AlarmReceive();
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    //  int languageValue = PreferenceManager.getInstance(MainActivity.this).getSelectedLanguage();
    //    if (Locale.getDefault().getLanguage().contains("ko"))
    //    {
    //      startStationName.setText(firstStationBean.nameKo);
    //      endStationName.setText(lastStationBean.nameKo);
    //    }
    //    start();
    //  }
    //    checkLanguage();

    super.onCreate(savedInstanceState);

    start();
  }

  public void start()
  {

    //    ======================기존 리스트 뷰 ==========================
    setContentView(R.layout.activity_main);
    text = (TextView) findViewById(R.id.address);
    ListView listview = (ListView) findViewById(R.id.listview);
    adapter = new CardViewListViewAdapter(MainActivity.this, getLayoutInflater(), new ArrayList<ListViewItem>());
    listview.setAdapter(adapter);

    locationListener = new WeatherLocationListener();
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

    double latitude = Double.longBitsToDouble(pref.getLong("lat", 999999999));
    double longitude = Double.longBitsToDouble(pref.getLong("lon", 999999999));

    String API_URL = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/" + latitude + "," + longitude;
    String MAP_API = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&language=ko";

    new MapJson().execute(MAP_API);
    new DownloadJson().execute(API_URL);

    progressbar = (ProgressBar) findViewById(R.id.progress_bar);

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
        dust = test.substring(3, 5);
        Log.d("어디", "미세먼지 받아옴?????" + dust);
        //        notify.NotifyDust(res);
        receive.add(dust);
        adapter.add(dust);
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

    //  =================== place picker ======================

    text.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        try
        {
          PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
          intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
          Intent intent = intentBuilder.build(MainActivity.this);
          startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException
            | GooglePlayServicesNotAvailableException e)
        {
          e.printStackTrace();
        }
      }
    });

    //    ========================= CardView ===========================
    //
    //    mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
    //    mRecyclerViewMain = (RecyclerView) findViewById(R.id.my_recycler_view_main);
    //
    //    mLayoutManager = new LinearLayoutManager(this);
    //    mLayoutManagerMain = new LinearLayoutManager(this);
    //
    //    mRecyclerView.setLayoutManager(mLayoutManager);
    //    mRecyclerViewMain.setLayoutManager(mLayoutManagerMain);
    //
    //    mRecyclerView.setHasFixedSize(true);
    //    mRecyclerViewMain.setHasFixedSize(true);
    //
    //    mRecyclerView.setAdapter(mAdapter);
    //    mRecyclerViewMain.setAdapter(mAdapterMain);
    //
    //    mRecyclerView.setNestedScrollingEnabled(false);
    //    mRecyclerViewMain.setNestedScrollingEnabled(false);
    //
    //    locationListener = new WeatherLocationListener();
    //    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //
    //    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
    //
    //    latitude = Double.longBitsToDouble(pref.getLong("lat", 999999999));
    //    longitude = Double.longBitsToDouble(pref.getLong("lon", 999999999));
    //
    //    Log.d("어디","start() / lat / " + latitude);
    //    Log.d("어디","start() / lon / " + longitude);
    //
    //    String API_URL = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/" + latitude + "," + longitude;
    //    String MAP_API = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&language=ko";
    //
    //    new MapJson().execute(MAP_API);
    //    new DownloadJson().execute(API_URL);
    //
    //    progressbar = (ProgressBar) findViewById(R.id.progress_bar);

    //    ======================= 네비게이션 드로워 ==========================

    lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);

    lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
    lvNavList.setOnItemClickListener(new DrawerItemClickListener());
    //    lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener()
    //    {
    //      @Override
    //      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    //      {
    //        switch (position)
    //        {
    //          case 0:
    //            Intent intent = new Intent(MainActivity.this, OpenApiExam.class);
    //            startActivity(intent);
    //            break;
    //          case 1:
    //            Toast.makeText(MainActivity.this, "sign up click", Toast.LENGTH_SHORT).show();
    //
    //        }
    //      }
    //    });


    // DrawerLayout 정의
    mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
    // ActionBar의 홈버튼을 Navigation Drawer 토글기능으로 사용
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)
    {

      @Override
      public void onDrawerClosed(View drawerView)
      {
        super.onDrawerClosed(drawerView);
      }

      @Override
      public void onDrawerOpened(View drawerView)
      {
        super.onDrawerOpened(drawerView);
      }

    };
    mDrawerLayout.addDrawerListener(mDrawerToggle);

    alarm_on();
  }

  //  =================== place picker result =========================
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {

    if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
    {

      final Place place = PlacePicker.getPlace(this, data);
      final CharSequence name = place.getName();
      final CharSequence address = place.getAddress();
      String attributions = (String) place.getAttributions();
      if (attributions == null)
      {
        attributions = "";
      }
      str = name.toString();
      Log.d("어디", "str  /  " + str);
      lat = str.substring(1, str.indexOf(","));
      lon = str.substring(13, str.indexOf(")"));

      Log.d("어디", "lat  /  " + lat);
      Log.d("어디", "lon  /  " + lon);

      SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
      SharedPreferences.Editor editor = pref.edit();

      latitude = Double.valueOf(lat);
      longitude = Double.valueOf(lon);

      Log.d("어디", "place picker / latitude / " + latitude);
      Log.d("어디", "place picker / longitude / " + longitude);

      // 저장할 값들을 입력합니다.
      editor.putLong("lat", Double.doubleToRawLongBits(latitude));
      editor.putLong("lon", Double.doubleToRawLongBits(longitude));

      editor.commit();

    }
    else
    {
      super.onActivityResult(requestCode, resultCode, data);
    }

    start();
  }

  private void checkLanguage()
  {
    String language = Locale.getDefault().getLanguage();
    int languageValue = Const.L_EN;
    if (language.equals("ko"))
      languageValue = Const.L_KO;
    else if (language.equals("zh"))
      languageValue = Const.L_ZH;
    else if (language.equals("ja"))
      languageValue = Const.L_JP;

    PreferenceManager.getInstance(this).setSelectedLanguage(languageValue);
  }


  @Override
  protected void onPostCreate(Bundle savedInstanceState)
  {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    Log.d("어디", "onPrepareOptionsMenu 진입");
    // If the nav drawer is open, hide action items related to the content view
    boolean drawerOpen = mDrawerLayout.isDrawerOpen(lvNavList);
    return super.onPrepareOptionsMenu(menu);
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener
  {
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
    {
      Log.d("어디", "DrawerItemClickListener");
      selectItem(position);
    }
  }

  private void selectItem(int position)
  {
    switch (position)
    {
      case 0:
        Toast.makeText(MainActivity.this, "알림바 끄기", Toast.LENGTH_SHORT).show();
        // Notify 끄기
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(111);
        break;
      case 1:
        Intent intent = new Intent(MainActivity.this, OpenApiExam.class);
        startActivity(intent);

    }
    mDrawerLayout.closeDrawer(GravityCompat.START);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    //ActionBar 메뉴 클릭에 대한 이벤트 처리

    switch (item.getItemId())
    {
      case R.id.btn_my_location:
        findNearByStation();
        break;
    }
    if (mDrawerToggle.onOptionsItemSelected(item))
    {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private ArrayList<ListViewItem> generateModels(JSONArray jsonArray)
  {
    ArrayList<ListViewItem> items = new ArrayList<>();

    SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormatHour = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
    try
    {
      for (int i = 0; i < 7; i++)
      {
        JSONObject obj = jsonArray.getJSONObject(i);
        String summary = obj.getString("summary");
        String icon = obj.getString("icon");
        String sunrise = obj.getString("sunriseTime");
        String sunset = obj.getString("sunsetTime");

        String dewPoint = obj.getString("dewPoint");
        String humidity = obj.getString("humidity");
        String windSpeed = obj.getString("windSpeed");
        String pressure = obj.getString("pressure");

        Log.d("어디", "sunset: " + sunset);
        Log.d("어디", "dewPoint: " + dewPoint);
        Log.d("어디", "humidity: " + humidity);
        Log.d("어디", "windSpeed: " + windSpeed);
        Log.d("어디", "pressure: " + pressure);

        Double temperatureMin = obj.getDouble("temperatureMin");
        Double temperatureMax = obj.getDouble("temperatureMax");

        long unixSunrise = Long.parseLong(sunrise) * 1000;
        long unixSunset = Long.parseLong(sunset) * 1000;
        String sunriseTime = dateFormatHour.format(unixSunrise);
        String sunsetTime = dateFormatHour.format(unixSunset);

        Double temperatureAvg = (temperatureMin + temperatureMax) / 2;
        Double temperatureChange = (temperatureAvg - 32) / 1.8;
        String temp = String.format("%.2f", temperatureChange);
        String temperature = temp.substring(0, temp.indexOf("."));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, i);
        String day = dayFormat.format(new Date(cal.getTimeInMillis()));
        String date = dateFormatYear.format(new Date(cal.getTimeInMillis()));
        Log.d("어디", "generateModels / day : " + day);
        Log.d("어디", "generateModels /date : " + date);
        Log.d("어디", "generateModels /summary : " + summary);

        ListViewItem item = new ListViewItem(day, date, summary, icon, temperature, sunriseTime, sunsetTime, dewPoint, humidity, windSpeed, pressure);
        items.add(item);
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }

    //    notify.NotifyMain(items.get(0).getIcon(), items.get(0).getStatus());
    //    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    //    Notification.Builder mBuilder = new Notification.Builder(MainActivity.this);
    //
    //    // 작은 아이콘 이미지.
    //    if (items.get(0).getIcon().contains("rain"))
    //      mBuilder.setSmallIcon(R.drawable.ic_weather_rain);
    //    else if (items.get(0).getIcon().contains("cloud"))
    //      mBuilder.setSmallIcon(R.drawable.ic_weather_cloud);
    //    else
    //      mBuilder.setSmallIcon(R.drawable.ic_weather_clear);
    //
    //    // 알림이 출력될 때 상단에 나오는 문구.
    //    mBuilder.setTicker("미리 보기");
    //
    //    Calendar calendar = Calendar.getInstance();
    //    //알람시간 calendar에 set해주기
    //
    //    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 18, 56);//시간을 10시 01분으로 일단 set했음
    //    calendar.set(Calendar.SECOND, 0);
    //
    //    // 알림 출력 시간.
    //    mBuilder.setWhen(calendar.getTimeInMillis()*60*1000);
    //
    //    // 알림 메세지 갯수
    //    //    mBuilder.setNumber(10);
    //    // 알림 제목.
    //    mBuilder.setContentTitle(items.get(0).getStatus());
    //    // 알림 내용.
    //    mBuilder.setContentText("미세먼지 : " + dust);
    //    // 프로그래스 바.
    //    //    mBuilder.setProgress(100, 50, false);
    //    // 알림시 사운드, 진동, 불빛을 설정 가능.
    //    mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
    //    // 알림 터치시 반응.
    //    mBuilder.setContentIntent(pendingIntent);
    //    // 알림 터치시 반응 후 알림 삭제 여부.
    //    mBuilder.setAutoCancel(true);
    //    // 우선순위.
    //    mBuilder.setPriority(Notification.PRIORITY_MAX);
    //
    //    // 행동 최대3개 등록 가능.
    //    //      mBuilder.addAction(R.mipmap.ic_launcher, "Show", pendingIntent);
    //    //      mBuilder.addAction(R.mipmap.ic_launcher, "Hide", pendingIntent);
    //    //      mBuilder.addAction(R.mipmap.ic_launcher, "Remove", pendingIntent);
    //
    //    // 고유ID로 알림을 생성.
    //    nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    //    nm.notify(111, mBuilder.build());
    return items;
  }

  public void alarm_on()
  {
    // 알람 등록하기
    Log.d("어디", " ====================== setAlarm ================ ");
    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(MainActivity.this, AlarmReceive.class);   //AlarmReceive.class이클레스는 따로 만들꺼임 알람이 발동될때 동작하는 클레이스임

    PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

    Calendar calendar = Calendar.getInstance();
    //알람시간 calendar에 set해주기

    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 18, 44);//시간을 10시 01분으로 일단 set했음
    calendar.set(Calendar.SECOND, 0);

    //알람 예약
    //am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);//이건 한번 알람
    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, sender);//이건 여러번 알람 24*60*60*1000 이건 하루에한번 계속 알람한다는 뜻.
    Toast.makeText(MainActivity.this, "시간설정:" + Integer.toString(calendar.get(calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calendar.get(calendar.MINUTE)), Toast.LENGTH_LONG).show();
  }

  /* ========================== 맵 JSON ==========================*/
  private class MapJson extends AsyncTask<String, String, String>
  {
    @Override
    protected String doInBackground(String... arg0)
    {
      Log.d("어디", "map");
      try
      {
        return (String) getData((String) arg0[0]);
      } catch (Exception e)
      {
        return "Json download failed";
      }
    }

    protected void onPostExecute(String result)
    {
      try
      {
        Log.d("MAP", "onPost");
        JSONObject jsonResult = new JSONObject(result.toString());
        JSONArray resultArray = jsonResult.getJSONArray("results");
        final int max = resultArray.length();
        for (int i = 0; i < max; i++)
        {
          JSONObject obj = resultArray.getJSONObject(2);
          address = obj.getString("formatted_address");
          Log.d("어디", "MAP/onPost/address: " + address);
        }

        text.setText(address);
      } catch (JSONException e)
      {
        Log.e("catch", "catch진입");
        e.printStackTrace();
      }
    }

    private String getData(String strUrl)
    {
      StringBuilder sb = new StringBuilder();

      try
      {
        BufferedInputStream bis = null;
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode;

        con.setConnectTimeout(3000);
        con.setReadTimeout(3000);

        responseCode = con.getResponseCode();

        if (responseCode == 200)
        {
          bis = new BufferedInputStream(con.getInputStream());
          BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
          String line = null;

          while ((line = reader.readLine()) != null)
            sb.append(line);

          bis.close();
        }

      } catch (Exception e)
      {
        e.printStackTrace();
      }

      return sb.toString();
    }
  }

  /* ========================== 날씨 JSON ==========================*/
  private class DownloadJson extends AsyncTask<String, String, String>
  {
    @Override
    protected String doInBackground(String... arg0)
    {
      try
      {
        return (String) getData((String) arg0[0]);
      } catch (Exception e)
      {
        return "Json download failed";
      }
    }

    protected void onPostExecute(String result)
    {
      try
      {
        JSONObject jsonResult = new JSONObject(result.toString());
        JSONObject dailyObject = jsonResult.getJSONObject("daily");
        JSONArray dataArray = dailyObject.getJSONArray("data");

        //        mAdapter = new CardViewAdapter(generateModels(dataArray));
        //        mAdapterMain = new CardViewAdapterMain(generateModels_main(dataArray));
        //
        //        mRecyclerView.setAdapter(mAdapter);
        //        mRecyclerViewMain.setAdapter(mAdapterMain);

        //        listViewItems.addAll(generateModels(dataArray));
        adapter.addAll(generateModels(dataArray));
        receive.addAll(generateModels(dataArray));
      } catch (JSONException e)
      {
        Log.e("catch", "catch진입");
        e.printStackTrace();
      }
    }

    private String getData(String strUrl)
    {
      StringBuilder sb = new StringBuilder();

      try
      {
        BufferedInputStream bis = null;
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode;

        con.setConnectTimeout(3000);
        con.setReadTimeout(3000);

        responseCode = con.getResponseCode();

        if (responseCode == 200)
        {
          bis = new BufferedInputStream(con.getInputStream());
          BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
          String line = null;

          while ((line = reader.readLine()) != null)
            sb.append(line);

          bis.close();
        }

      } catch (Exception e)
      {
        e.printStackTrace();
      }

      return sb.toString();
    }
  }

  /* ================================== Location ==========================================*/
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
  {
    if (grantResults[0] != -1)
      findNearByStation();
  }

  private void findNearByStation()
  {
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
      buildAlertMessageNoGps();
    else
    {
      if (android.os.Build.VERSION.SDK_INT >= 23)
      {
        Log.d("어디", "sdk버전 " + android.os.Build.VERSION.SDK_INT);

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) && PackageManager.PERMISSION_GRANTED == ContextCompat
            .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION))
        {
          requestLocation();
        }
        else
        {
          requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
      }
      else
        requestLocation();
    }
  }

  @SuppressWarnings({"MissingPermission"})
  private void requestLocation()
  {
    dialog = ProgressDialog.show(MainActivity.this, "", "위치를 찾는 중입니다. 잠시 기다려 주세요", false);
    progressbar.setVisibility(View.VISIBLE);
    progressbar.bringToFront();
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle(getString(R.string.app_name));
    builder.setMessage("위치를 찾는 중입니다");
    builder.setPositiveButton(android.R.string.ok, null);
    builder.create().show();
    handler.postDelayed(timeOutFindStationCallBack, LCOATION_TIME_OUT_SECOND);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
  }

  @SuppressWarnings({"MissingPermission"})
  private Runnable timeOutFindStationCallBack = new Runnable()
  {
    public void run()
    {
      locationManager.removeUpdates(locationListener);
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setTitle(getString(R.string.app_name));
      builder.setMessage(getString(R.string.map_gps_Location_fail));
      builder.setPositiveButton(android.R.string.ok, null);
      builder.create().show();
    }
  };

  private void buildAlertMessageNoGps()
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(R.string.app_name).setMessage(R.string.msg_please_gps_enable).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(final DialogInterface dialog, final int id)
      {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
      }
    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      public void onClick(final DialogInterface dialog, final int id)
      {
        dialog.cancel();
      }
    });
    final AlertDialog alert = builder.create();
    alert.show();
  }


  private class WeatherLocationListener implements LocationListener
  {
    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onLocationChanged(Location loc)
    {
      //      preferenceManager.getInstance(MainActivity.this).setLat(loc.getLatitude());
      //      preferenceManager.getInstance(MainActivity.this).setLon(loc.getLongitude());
      Log.d("어디", "리스너 lat" + loc.getLatitude());
      Log.d("어디", "리스너 lon" + loc.getLongitude());

      SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
      SharedPreferences.Editor editor = pref.edit();
      // 저장할 값들을 입력합니다.
      editor.putLong("lat", Double.doubleToRawLongBits(loc.getLatitude()));
      editor.putLong("lon", Double.doubleToRawLongBits(loc.getLongitude()));

      editor.commit();

      handler.removeCallbacks(timeOutFindStationCallBack);
      locationManager.removeUpdates(locationListener);
      dialog.dismiss();
      viewClear();
      start();
    }


    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
  }

  public void notifyDataSetChanged()
  {
    mDataSetObservable.notifyChanged();
  }

  public static void viewClear()
  {
    //    mRecyclerView = null;
  }
};












