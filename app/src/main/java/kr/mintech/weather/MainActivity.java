package kr.mintech.weather;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.controllers.CardViewAdapter;
import kr.mintech.weather.controllers.CardViewAdapterMain;
import kr.mintech.weather.managers.PreferenceManager;


public class MainActivity extends AppCompatActivity
{
  //  public static View view;
  //  private ListViewAdapter adapter;

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

  private String[] navItems = {"Sign in", "Sign up", "Setting", "Share Weather", "Help"};
  private ListView lvNavList;
  private DrawerLayout mDrawerLayout; // 주 기능
  private ActionBarDrawerToggle mDrawerToggle; // 주 기능
  private ActionBar actionBar;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    start();

  }

  public void start()
  {
    //    ====================== 기존 리스트 뷰 ==========================
    //    setContentView(R.layout.activity_main);
    //    ListView listview = (ListView) findViewById(R.id.listview);
    //    adapter = new ListViewAdapter(MainActivity.this, getLayoutInflater(), new ArrayList<ListViewItem>());
    //    listview.setAdapter(adapter);
    //
    //    locationListener = new WeatherLocationListener();
    //    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //
    //    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
    //
    //    double latitude = Double.longBitsToDouble(pref.getLong("lat", 999999999));
    //    double longitude = Double.longBitsToDouble(pref.getLong("lon", 999999999));
    //
    //    String API_URL = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/" + latitude + "," + longitude;
    //    String MAP_API = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&language=ko";
    //
    //    new DownloadJson().execute(API_URL);
    //    new MapJson().execute(MAP_API);

    //    progressbar = (ProgressBar) findViewById(R.id.progress_bar);

    //    ========================= CardView ===========================

    setContentView(R.layout.activity_main);
    mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
    mRecyclerViewMain = (RecyclerView) findViewById(R.id.my_recycler_view_main);

    mLayoutManager = new LinearLayoutManager(this);
    mLayoutManagerMain = new LinearLayoutManager(this);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerViewMain.setLayoutManager(mLayoutManagerMain);

    mRecyclerView.setHasFixedSize(true);
    mRecyclerViewMain.setHasFixedSize(true);

    mRecyclerView.setAdapter(mAdapter);
    mRecyclerViewMain.setAdapter(mAdapterMain);

    mRecyclerView.setNestedScrollingEnabled(false);
    mRecyclerViewMain.setNestedScrollingEnabled(false);

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

    //    ======================= 네비게이션 드로워 ==========================

    lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);

    lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
    lvNavList.setOnItemClickListener(new DrawerItemClickListener());

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
      switch (position)
      {
        case 0:
       Toast.makeText(MainActivity.this, "sign in 클릭", Toast.LENGTH_SHORT).show();
          break;
        case 1:
          Toast.makeText(MainActivity.this, "sign up 클릭", Toast.LENGTH_SHORT).show();
          break;
        case 2:
          Toast.makeText(MainActivity.this, "setting 클릭", Toast.LENGTH_SHORT).show();
          break;
        case 3:
          Toast.makeText(MainActivity.this, "share weather 클릭", Toast.LENGTH_SHORT).show();
          break;
        case 4:
          Toast.makeText(MainActivity.this, "help 클릭", Toast.LENGTH_SHORT).show();
          break;
      }
    }
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

  private ArrayList<ListViewItem> generateModels_main(JSONArray jsonArray)
  {
    ArrayList<ListViewItem> items = new ArrayList<>();

    SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormatHour = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");

    try
    {
      for (int i = 0; i < 1; i++)
      {
        JSONObject obj = jsonArray.getJSONObject(i);
        String summary = obj.getString("summary");
        String icon = obj.getString("icon");
        String sunrise = obj.getString("sunriseTime");
        String sunset = obj.getString("sunsetTime");

        String dewPoint = obj.getString("dewPoint");
        String humidity = obj.getString("humidity");
        String windSpeed = obj.getString("windSpeed");
        String visibility = obj.getString("visibility");
        String pressure = obj.getString("pressure");

        Log.d("어디", "generateModels_main / sunset: " + sunset);
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
        Log.d("어디", "generateModels_main /day : " + day);
        Log.d("어디", "generateModels_main /date : " + date);
        Log.d("어디", "generateModels_main /summary : " + summary);

        ListViewItem item = new ListViewItem(day, date, summary, icon, temperature, sunriseTime, sunsetTime, dewPoint, humidity, windSpeed, visibility, pressure);
        items.add(item);
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
    return items;
  }

  private ArrayList<ListViewItem> generateModels(JSONArray jsonArray)
  {
    ArrayList<ListViewItem> items = new ArrayList<>();

    SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormatHour = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");

    try
    {
      for (int i = 1; i < 7; i++)
      {
        JSONObject obj = jsonArray.getJSONObject(i);
        String summary = obj.getString("summary");
        String icon = obj.getString("icon");
        String sunrise = obj.getString("sunriseTime");
        String sunset = obj.getString("sunsetTime");
        Log.d("어디", "sunset: " + sunset);
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

        ListViewItem item = new ListViewItem(day, date, summary, icon, temperature, sunriseTime, sunsetTime);
        items.add(item);
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
    return items;
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

        text = (TextView) findViewById(R.id.address);
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

        mAdapter = new CardViewAdapter(generateModels(dataArray));
        mAdapterMain = new CardViewAdapterMain(generateModels_main(dataArray));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerViewMain.setAdapter(mAdapterMain);

        //        listViewItems.addAll(generateModels(dataArray));
        //        adapter.addAll(generateModels(dataArray));
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
    //    progressbar.setVisibility(View.VISIBLE);
    //    progressbar.bringToFront();
    //    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    //    builder.setTitle(getString(R.string.app_name));
    //    builder.setMessage("위치를 찾는 중입니다");
    //    builder.setPositiveButton(android.R.string.ok, null);
    //    builder.create().show();
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












