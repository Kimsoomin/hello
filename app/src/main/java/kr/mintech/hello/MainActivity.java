package kr.mintech.hello;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

import kr.mintech.hello.beans.ListViewItem;
import kr.mintech.hello.controllers.ListViewAdapter;


public class MainActivity extends Activity {
    ArrayList<ListViewItem> listViewItem = new ArrayList<ListViewItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String url = "http://www.word.pe.kr/keyword/testJson.php";
        String url = "https://api.forecast.io/forecast/33edacd677aacf2ec509a82f12ff327d/37.514236,127.03159299999993";
        // call data from web URL
        try {
            ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                new DownloadJson().execute(url);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "네트워크를 확인하세요", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView listview = (ListView) findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, getLayoutInflater(), listViewItem);

        listview.setAdapter(adapter);

        adapter.addAll(generateModels());
    }

    private ArrayList<ListViewItem> generateModels() {
        ArrayList<ListViewItem> items = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");

        for (int i = 0; i < 7; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i);
            String day = dayFormat.format(new Date(cal.getTimeInMillis()));
            String date = dateFormat.format(new Date(cal.getTimeInMillis()));

            ListViewItem item = new ListViewItem(day, date, "맑음");
            items.add(item);
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
            try {
                JSONArray jArray = new JSONArray(result);

                String[] jsonWeather = {"time", "summary"};
                String[][] parsedData = new String[jArray.length()][jsonWeather.length];

                JSONObject json = null;
                for (int i = 0; i < jArray.length(); i++) {
                    json = jArray.getJSONObject(i);
                    if (json != null) {
                        for (int j = 0; j < jsonWeather.length; j++) {
                            parsedData[i][j] = json.getString(jsonWeather[j]);
                        }
                    }
                }

                for (int i = 0; i < parsedData.length; i++) {
                    Log.e("mini", "time" + i + ":" + parsedData[i][0]);
                    Log.e("mini", "summary" + i + ":" + parsedData[i][1]);
                    Log.e("mini", "----------------------------------");
                }
            } catch (JSONException e) {
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












