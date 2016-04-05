package kr.mintech.hello;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import kr.mintech.hello.controllers.ListViewAdapter;

// TODO : Custom ListAdapter (BaseListAdapter를 상속받아야 함 getView 에 Layout) 
// TODO : Custom ListAdapter getView 안에서 어떤 동작을 통해 펼쳐지고 닫혀진다. 


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview;
        ListViewAdapter adapter;


        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_account_box_black_24dp),
                "Box", "Account Box Black 36dp");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_account_circle_black_24dp),
                "Circle", "Account Circle Black 36dp");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_face),
                "Ind", "Assignment Ind Black 36dp");


    }

};













