package kr.mintech.hello;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import kr.mintech.hello.beans.ListViewItem;
import kr.mintech.hello.controllers.ListViewAdapter;


public class MainActivity extends Activity {
    private final static String API_URL = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/37.517365,127.026112";
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, i);
                String day = dayFormat.format(new Date(cal.getTimeInMillis()));
                String date = dateFormat.format(new Date(cal.getTimeInMillis()));

                ListViewItem item = new ListViewItem(day, date, summary, icon);
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












