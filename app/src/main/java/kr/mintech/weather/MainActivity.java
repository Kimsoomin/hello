package kr.mintech.weather;


import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
import kr.mintech.weather.controllers.ListViewAdapter;


public class MainActivity extends AppCompatActivity
{
    private final static String API_URL = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/37.517365,127.026112";
    private ListViewAdapter adapter;
    private View view;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        actionBar = getActionBar();
//        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
//
//        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
//        actionBar.setDisplayShowTitleEnabled(false);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
//        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.
//
//        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
//        View mCustomView = LayoutInflater.from(this).inflate(R.layout.listview_item, null);
//        actionBar.setCustomView(mCustomView);
//
//
//        // 액션바에 백그라운드 색상을 아래처럼 입힐 수 있습니다.
//        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));
//
//
//        // 액션바에 백그라운드 이미지를 아래처럼 입힐 수 있습니다. (drawable 폴더에 img_action_background.png 파일이 있어야 겠죠?)
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_action_background));

        ListView listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(MainActivity.this, getLayoutInflater(), new ArrayList<ListViewItem>());
        listview.setAdapter(adapter);

        new DownloadJson().execute(API_URL);

    }

    private ArrayList<ListViewItem> generateModels(JSONArray jsonArray) {
        ArrayList<ListViewItem> items = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");

        try {
            for (int i = 0; i < 7; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String summary = obj.getString("summary");
                String icon = obj.getString("icon");
                Double temperatureMin = obj.getDouble("temperatureMin");
                Double temperatureMax = obj.getDouble("temperatureMax");

                Double temperatureAvg = (temperatureMin + temperatureMax) / 2;
                Double temperatureChange = ( temperatureAvg - 32 ) + 1.8;
                String temperature = String.format("%.2f" , temperatureChange);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, i);
                String day = dayFormat.format(new Date(cal.getTimeInMillis()));
                String date = dateFormat.format(new Date(cal.getTimeInMillis()));

                ListViewItem item = new ListViewItem(day, date, summary, icon, temperature);
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

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
            //백그라운드 작업이 완료되었을 때

            try {
                Log.e("try", "진입은 하니?");

                JSONObject jsonResult = new JSONObject(result.toString());
                JSONObject dailyObject = jsonResult.getJSONObject("daily");
                JSONArray dataArray = dailyObject.getJSONArray("data");

                adapter.addAll(generateModels(dataArray));

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
};












