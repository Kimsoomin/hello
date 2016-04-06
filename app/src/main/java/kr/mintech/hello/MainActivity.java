package kr.mintech.hello;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
};











