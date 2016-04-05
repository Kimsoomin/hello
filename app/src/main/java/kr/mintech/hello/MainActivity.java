package kr.mintech.hello;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import kr.mintech.hello.controllers.ListViewAdapter;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView) findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        adapter.addItem("화요일");
        adapter.addItem("수요일");
        adapter.addItem("목요일");
        adapter.addItem("금요일");
        adapter.addItem("토요일");
        adapter.addItem("일요일");
        adapter.addItem("월요일");

        adapter.notifyDataSetChanged();
    }
};













