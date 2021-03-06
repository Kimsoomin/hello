package kr.mintech.weather;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.DataSetObservable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.common.PlanetXSDKConstants;
import kr.mintech.weather.common.PlanetXSDKException;
import kr.mintech.weather.common.RequestBundle;
import kr.mintech.weather.common.RequestListener;
import kr.mintech.weather.common.ResponseMessage;
import kr.mintech.weather.controllers.CardViewListViewAdapter;
import kr.mintech.weather.controllers.CardViewListViewAdapterWeek;
import kr.mintech.weather.fragments.TodayFragment;
import kr.mintech.weather.fragments.WeekFragment;
import kr.mintech.weather.managers.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    APIRequest api;
    RequestBundle requestBundle;
    // http://apis.skplanetx.com/melon/newreleases/songs?version={version}&page={page}&count={count}
    // String URL = "http://apis.skplanetx.com/melon/newreleases/songs";
    String URL = "http://apis.skplanetx.com/weather/dust";
    Map<String, Object> param;
    String hndResult = "";
    String test;
    String dust_value;
    String valueInit;
    String value;
    /* ================= */

    private TodayFragment todayFragment = new TodayFragment();
    private WeekFragment weekFragment = new WeekFragment();

    // =============== navi draw ==================

    private String[] navItemsKo = {"설정"};
    private String[] navItemsEn = {"Setting"};

    private ListView lvNavList;
    private DrawerLayout mDrawerLayout; // 주 기능
    private ActionBarDrawerToggle mDrawerToggle; // 주 기능
    private ActionBar actionBar;

    // ============ placePicker =============

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, 127.0303081), new LatLng(37.430610, 121.972090));
    private String str;
    private String lat;
    private String lon;

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

    public static String addressInit;

    //  ============== card view ==================

    public static View view;
    private static CardViewListViewAdapter adapter;

    private ArrayList<ListViewItem> items;

    // ============ 미세먼지 sk planet ============
    String dust;

    // =========== 언어설정 ==============
    String language;

    // =========== Setting Activity ===========
    String icon;
    String status;

    //  ============= View Pager ===============
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static CardViewListViewAdapterWeek weekAdapter;

    String API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = Locale.getDefault().getLanguage();
        setContentView(R.layout.activity_main);

        weekAdapter = new CardViewListViewAdapterWeek(MainActivity.this, getLayoutInflater(), new ArrayList<ListViewItem>());

        locationListener = new WeatherLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        text = (TextView) findViewById(R.id.address);
        placePicker = (LinearLayout) findViewById(R.id.place_picker);

        init();
    }

    public void init() {
        Log.d("어디", "init() 진입");

        double latitude = Double.longBitsToDouble(PreferenceManager.getInstance(MainActivity.this).getLat());
        double longitude = Double.longBitsToDouble(PreferenceManager.getInstance(MainActivity.this).getLon());

        Log.d("어디", "init() latitude ///" + latitude);
        Log.d("어디", "init() longitude///" + longitude);

        API_URL = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/" + latitude + "," + longitude;
        String MAP_API = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&language=ko";

        new MapJson().execute(MAP_API);
        new DustAsync().execute("");
        new DownloadJson().execute(API_URL);
        progressbar = (ProgressBar) findViewById(R.id.progress_bar);

        //  =================== place picker ======================

        placePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        //    ======================= 네비게이션 드로워 ==========================

        lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        if (language.contains("ko")) {
            lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItemsKo));
        } else {
            lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItemsEn));
        }
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        // DrawerLayout 정의
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        // ActionBar의 홈버튼을 Navigation Drawer 토글기능으로 사용
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    //  =================== place picker result =========================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            addressInit = address.toString();
            Log.d("어디", "======== addressInit ========" + addressInit);
            Log.d("어디", " ============ addressInit.length() ========== / " + addressInit.length());

            String google_URL = "http://maps.google.com/maps/api/geocode/json?address=" + addressInit + "&ka&sensor=false";

            if (addressInit.length() == 0) {
                PreferenceManager.getInstance(MainActivity.this).setAddressInit(addressInit);
                Log.d("어디", "place picker" + name);
                str = name.toString();
                Log.d("어디", "str  /  " + str);
                lat = str.substring(1, str.indexOf(","));
                lon = str.substring(13, str.indexOf(")"));
                latitude = Double.valueOf(lat);
                longitude = Double.valueOf(lon);

                PreferenceManager.getInstance(MainActivity.this).setLat(Double.doubleToRawLongBits(latitude));
                PreferenceManager.getInstance(MainActivity.this).setLon(Double.doubleToRawLongBits(longitude));

                init();
            } else {
                Log.d("어디", "onActivityResult / else 들어오니");
                new geoPointTask().execute(addressInit);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //  =============================================

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy", "MainActivity");
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("어디", "onPrepareOptionsMenu 진입");
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(lvNavList);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            Log.d("어디", "DrawerItemClickListener");
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position) {
//            case 0:
//                Log.d("어디", " selectItem position=1");
//                Intent intent = new Intent(MainActivity.this, SettingActivityAgo.class);
//                intent.putExtra("dust", dust);
//                intent.putExtra("icon", icon);
//                intent.putExtra("status", status);
//
//                startActivity(intent);
//                break;
//            case 1:
//                Log.d("어디", " selectItem position=2");
//                Intent testIntent = new Intent(MainActivity.this, SettingTest.class);
//                startActivity(testIntent);
//                break;
//
//            case 2:
//                Log.d("어디", " selectItem position=3");
//                Intent testIntent2 = new Intent(MainActivity.this, SettingActivity.class);
//                startActivity(testIntent2);
//                break;

            case 0:
                Log.d("어디", " selectItem position=0");
                Intent testIntent2 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(testIntent2);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //ActionBar 메뉴 클릭에 대한 이벤트 처리
        switch (item.getItemId()) {
            case R.id.btn_my_location:
                findNearByStation();
                break;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<ListViewItem> generateModels(JSONArray jsonArray) {
        items = new ArrayList<>();

        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatHour = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        try {
            for (int i = 0; i < 7; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String summary = obj.getString("summary");
                String icon = obj.getString("icon");
                String sunrise = obj.getString("sunriseTime");
                String sunset = obj.getString("sunsetTime");

                String dewPoint = obj.getString("dewPoint");
                String humidity = obj.getString("humidity");
                String windSpeed = obj.getString("windSpeed");
                String pressure = obj.getString("pressure");

                Double temperatureMin = obj.getDouble("temperatureMin");
                Double temperatureMax = obj.getDouble("temperatureMax");

                long unixSunrise = Long.parseLong(sunrise) * 1000;
                long unixSunset = Long.parseLong(sunset) * 1000;
                String sunriseTime = dateFormatHour.format(unixSunrise);
                String sunsetTime = dateFormatHour.format(unixSunset);

                String temperMax = Double.toString(temperatureMax);
                String temperMin = Double.toString(temperatureMin);

                Double temperatureAvg = (temperatureMin + temperatureMax) / 2;
                Double temperatureChange = (temperatureAvg - 32) / 1.8;
                String temp = String.format("%.2f", temperatureChange);
                String temperature = temp.substring(0, temp.indexOf("."));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, i);
                String day = dayFormat.format(new Date(cal.getTimeInMillis()));
                String date = dateFormatYear.format(new Date(cal.getTimeInMillis()));

                if (language.contains("ko")) {
                    if (summary.contains("Mostly cloudy overnight")) {
                        summary = "밤새 흐림";
                    } else if (summary.contains("Mostly cloudy throughout the day")) {
                        summary = "하루 종일 흐림";
                    } else if (summary.contains("Rain until evening")) {
                        summary = "저녁까지 비";
                    } else if (summary.contains("Clear throughout the day")) {
                        summary = "하루 종일 맑음";
                    } else if (summary.contains("Mostly cloudy")) {
                        summary = "대부분 흐리고 오후에 미풍";
                    } else if (summary.contains("Mostly cloudy in the morning")) {
                        summary = "오전 대부분 흐림";
                    } else if (summary.contains("Rain starting in the afternoon")) {
                        summary = "오후부터 비";
                    } else if (summary.contains("Rain until afternoon")) {
                        summary = "오후까지 비 그리고 오후부터 시작한 미풍은 저녁까지";
                    } else if (summary.contains("Rain starting in the evening")) {
                        summary = "저녁부터 내리는 비";
                    } else if (summary.contains("Windy until evening")) {
                        summary = "저녁까지 바람 계속";
                    } else if (summary.contains("Rain in the morning")) {
                        summary = "아침부터 내리는 비 그리고 오후부터 시작 된 미풍은 저녁까지";
                    } else if (summary.contains("Partly cloudy until afternoon")) {
                        summary = "오후까지 가끔 구름";
                    } else if (summary.contains("Heavy rain until evening")) {
                        summary = "저녁까지 강한 비";
                    } else if (summary.contains("Windy until evening")) {
                        summary = "저녁까지 바람";
                    } else if (summary.contains("Partly cloudy")) {
                        summary = "가끔 구름";
                    } else if (summary.contains("Overcast throughout the day")) {
                        summary = "하루 종일 흐림";
                    } else if (summary.contains("Rain and windy throughout the day")) {
                        summary = "하루 종일 비 바람";
                    } else if (summary.contains("Light rain in the morning")) {
                        summary = "아침에 가벼운 비";
                    } else if (summary.contains("Light rain overnight")) {
                        summary = "밤새 가벼운 비";
                    } else if (summary.contains("Light rain until afternoon")) {
                        summary = "오후까지 가벼운 비";
                    } else if (summary.contains("Light rain starting in the evening")) {
                        summary = "저녁부터 시작되 가벼운 비";
                    } else if (summary.contains("Light rain until evening")) {
                        summary = "저녁까지 가벼운 비";
                    } else if (summary.contains("Light rain in the afternoon and evening")) {
                        summary = "저녁까지 가벼운 비";
                    } else if (summary.contains("Drizzle overnight")) {
                        summary = "새벽에 이슬비";
                    } else if (summary.contains("Rain throughout the day")) {
                        summary = "하루종일 비";
                    } else if (summary.contains("Partly cloudy overnight")) {
                        summary = "밤새 가끔 구름";
                    } else if (summary.contains("Light rain throughout the day")) {
                        summary = "종일 가벼운 비";
                    } else if (summary.contains("Drizzle in the morning")) {
                        summary = "아침에 이슬비";
                    } else if (summary.contains("Light rain starting in the afternoon")) {
                        summary = "오후부터 시작하는 가벼운 비";
                    } else if (summary.contains("Drizzle starting in the evening")) {
                        summary = "저녁부터 시작하는 이슬비";
                    } else if (summary.contains("Drizzle until afternoon")) {
                        summary = "오후까지 이슬비";
                    } else if (summary.contains("Drizzle until afternoon")) {
                        summary = "오후까지 이슬비";
                    }

                }

                ListViewItem item = new ListViewItem(day, date, summary, icon, temperature, sunriseTime, sunsetTime, dewPoint, humidity, windSpeed, pressure);
                items.add(item);

                TodayFragment fragment = new TodayFragment();
                Bundle args = new Bundle();
                args.putInt("test", 1);
                fragment.setArguments(args);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (items.get(0) == null) {
            icon = "";
            status = "";
        } else {
            icon = items.get(0).getIcon();
            status = items.get(0).getStatus();
        }


        PreferenceManager.getInstance(MainActivity.this).setIcon(icon);
        PreferenceManager.getInstance(MainActivity.this).setStatus(status);

        return items;
    }

    /* ========================== 위경도로 주소 찾기 JSON ==========================*/
    private class MapJson extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                return (String) getData((String) arg0[0]);
            } catch (Exception e) {
                return "Json download failed";
            }
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResult = new JSONObject(result.toString());
                JSONArray resultArray = jsonResult.getJSONArray("results");
                final int max = resultArray.length();
                for (int i = 0; i < max; i++) {
                    JSONObject obj = resultArray.getJSONObject(2);
                    topAddress = obj.getString("formatted_address");

                }
                Log.d("어디", "onPostEx resultArray.length() / " + resultArray.length());

                Log.d("어디", "MapJSON addressInit/ " + PreferenceManager.getInstance(MainActivity.this).getAddressInit());

                if (resultArray.length() == 0) {
                    if (PreferenceManager.getInstance(MainActivity.this).getAddressInit().length() > 2) {
                        text.setText(PreferenceManager.getInstance(MainActivity.this).getAddressInit());
                    } else {
                        text.setText("확인되지 않는 주소입니다");
                    }
                }
                if (resultArray.length() != 0) {
                    text.setText(topAddress);
                }


            } catch (JSONException e) {
                Log.e("catch", "catch진입");
                e.printStackTrace();
            }
        }

        private String getData(String strUrl) {
            StringBuilder sb = new StringBuilder();

            try {
                BufferedInputStream bis = null;
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int responseCode;

                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);

                responseCode = con.getResponseCode();

                if (responseCode == 200) {
                    bis = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
                    String line = null;

                    while ((line = reader.readLine()) != null)
                        sb.append(line);

                    bis.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        }
    }

    /* ========================== 날씨 JSON ==========================*/
    private class DownloadJson extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                return (String) getData((String) arg0[0]);
            } catch (Exception e) {
                return "Json download failed";
            }
        }

        protected void onPostExecute(String result) {
            try {
                Log.d("어디", "============ 날씨 JSON 진입 ===========");
                JSONObject jsonResult = new JSONObject(result.toString());
                JSONObject dailyObject = jsonResult.getJSONObject("daily");
                JSONArray dataArray = dailyObject.getJSONArray("data");

                todayFragment.addAll(generateModels(dataArray));
                weekFragment.addAll(generateModels(dataArray));

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);


            } catch (JSONException e) {
                Log.e("catch", "catch진입");
                e.printStackTrace();
            }
        }

        private String getData(String strUrl) {
            StringBuilder sb = new StringBuilder();

            try {
                BufferedInputStream bis = null;
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int responseCode;

                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);

                responseCode = con.getResponseCode();

                if (responseCode == 200) {
                    bis = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
                    String line = null;

                    while ((line = reader.readLine()) != null)
                        sb.append(line);

                    bis.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        }
    }

  /* ========================== 주소로 위경도 찾기 JSON ==========================*/

    private class geoPointTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            getGeoPoint(getLocationInfo(params[0].replace("\n", " ").replace(" ", "%20")));  //주소를 넘겨준다(공백이나 엔터는 제거합니다)

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        public JSONObject getLocationInfo(String address) {

            HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + address + "&ka&sensor=false");
            //해당 url을 인터넷창에 쳐보면 다양한 위도 경도 정보를 얻을수있다(크롬 으로실행하세요)
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return jsonObject;
        }

        public void getGeoPoint(JSONObject jsonObject) {
            Double lon = new Double(0);
            Double lat = new Double(0);
            Log.d("어디", "getGeoPoint /" + jsonObject.toString());

            try {
                lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.d("어디", "경도:" + lon); //위도/경도 결과 출력
            Log.d("어디", "위도:" + lat);
            Log.d("어디", "======getGeoPoint======" + Double.doubleToLongBits(lat));
            Log.d("어디", "======getGeoPoint======" + Double.doubleToLongBits(lon));

            PreferenceManager.getInstance(MainActivity.this).setLat(Double.doubleToLongBits(lat));
            PreferenceManager.getInstance(MainActivity.this).setLon(Double.doubleToLongBits(lon));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init();
                        }
                    });
                }
            }).start();
        }
    }

    /* ================================== Location ==========================================*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] != -1)
            findNearByStation();
    }

    private void findNearByStation() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            buildAlertMessageNoGps();
        else {
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                Log.d("어디", "sdk버전 " + android.os.Build.VERSION.SDK_INT);

                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) && PackageManager.PERMISSION_GRANTED == ContextCompat
                        .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    requestLocation();
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
            } else
                requestLocation();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void requestLocation() {
        Toast.makeText(MainActivity.this, "위치를 찾는 중입니다", Toast.LENGTH_SHORT).show();
        handler.postDelayed(timeOutFindStationCallBack, LCOATION_TIME_OUT_SECOND);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @SuppressWarnings({"MissingPermission"})
    private Runnable timeOutFindStationCallBack = new Runnable() {
        public void run() {
            locationManager.removeUpdates(locationListener);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.app_name));
            builder.setMessage(getString(R.string.map_gps_Location_fail));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
        }
    };

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name).setMessage(R.string.msg_please_gps_enable).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private class WeatherLocationListener implements LocationListener {
        @SuppressWarnings({"MissingPermission"})
        @Override
        public void onLocationChanged(Location loc) {
            Log.d("어디", "리스너 lat" + loc.getLatitude());
            Log.d("어디", "리스너 lon" + loc.getLongitude());

            PreferenceManager.getInstance(MainActivity.this).setLat(Double.doubleToRawLongBits(loc.getLatitude()));
            PreferenceManager.getInstance(MainActivity.this).setLon(Double.doubleToRawLongBits(loc.getLongitude()));

            handler.removeCallbacks(timeOutFindStationCallBack);
            locationManager.removeUpdates(locationListener);

            //      dialog.dismiss();
            init();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    // ================ Fragment ===================

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("test");
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ArrayList<String> titles = new ArrayList<>();
        public Fragment fragment;

        public ArrayList<Fragment> fragments = new ArrayList<>();


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                case 0: {
                    fragment = new TodayFragment();
                    break;
                }
                case 1: {
                    fragment = new WeekFragment();
                    break;
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            titles.add(0, "Today");
            titles.add(1, "Week");

            return titles.get(position);
        }
    }

    public class DustAsync extends AsyncTask<String, String, String> {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("어디", "DustJson doInBackground 진입");
            api = new APIRequest();
            APIRequest.setAppKey("2fa79986-1dd0-3a04-b767-43e6b86138fe");

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

            RequestListener reqListener = new RequestListener() {
                @Override
                public void onPlanetSDKException(PlanetXSDKException e) {
                    hndResult = e.toString();
                }

                @Override
                public void onComplete(ResponseMessage result) {
                    // 응답을 받아 메시지 핸들러에 알려준다.
                    try {
                        hndResult = result.getStatusCode() + "\n" + result.toString();
                        test = hndResult.split("grade")[1];
                        valueInit = hndResult.split("value")[1];
                        value = valueInit.substring(3, 7);
                        dust = test.substring(3, 5);

                        Log.d("어디", "=========== DustJson valueInit ===========  " + hndResult);
                        Log.d("어디", "=========== DustJson value ===========  " + value);
                        Log.d("어디", "=========== DustJson dust ===========  " + dust);

                        if (language.contains("en")) {
                            if (dust.contains("매우 높음")) {
                                dust = "Very High";
                            } else if (dust.equals("높음")) {
                                dust = "High";
                            } else if (dust.contains("보통")) {
                                dust = "Middle";
                            } else if (dust.equals("낮음")) {
                                dust = "Low";
                            } else if (dust.contains("매우 낮음")) {
                                dust = "Very Low";
                            }
                        }

                        PreferenceManager.getInstance(MainActivity.this).setDust(dust);
                        PreferenceManager.getInstance(MainActivity.this).setValue(value);

                    } catch (Exception e) {
                        e.printStackTrace();

                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기
                            }
                        });
                        alert.setMessage("금일 서비스가 중지 되었습니다. 내일 이용하실 수 있습니다.");
                        alert.show();
                    }
                }
            };

            try {
                // 비동기 호출
                api.request(requestBundle, reqListener);
            } catch (PlanetXSDKException e) {
                e.printStackTrace();
            }
            return dust;
        }

        @Override
        protected void onPostExecute(String dustValue) {
            super.onPostExecute(dustValue);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
}












