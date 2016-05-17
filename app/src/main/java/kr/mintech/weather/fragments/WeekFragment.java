package kr.mintech.weather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.controllers.CardViewListViewAdapter;

/**
 * Created by Mac on 16. 5. 17..
 */
public class WeekFragment extends Fragment {
    public static View view;
    private static CardViewListViewAdapter adapter;

    private ArrayList<ListViewItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("어디", "============= WeekFragment onCreateView 진입 ===========");

        View rootView = inflater.inflate(R.layout.fragment_week, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.card_view);
        adapter = new CardViewListViewAdapter();
        listview.setAdapter(adapter);

        return rootView;
    }
}
