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
import kr.mintech.weather.controllers.CardViewListViewAdapterWeek;

/**
 * Created by Mac on 16. 5. 17..
 */
public class WeekFragment extends Fragment {
    public static View view;
    private static CardViewListViewAdapterWeek adapter;

    public static ListViewItem listViewItem = new ListViewItem();
    public static ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public WeekFragment() {
    }

    public void addAll(ArrayList<ListViewItem> items)
    {
        Log.d("어디","========= addAll 진입 ==========");
        this.listViewItemList.addAll(items);
        Log.d("어디","========= addAll / listViewItemList =========="+listViewItemList.get(0).getTemperature());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("어디", "============= WeekFragment onCreateView 진입 ===========");

        View rootView = inflater.inflate(R.layout.fragment_week, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.listview_week);
        adapter = new CardViewListViewAdapterWeek(getActivity(), inflater, new ArrayList<ListViewItem>());
        listview.setAdapter(adapter);
        adapter.addAll(listViewItemList);

        return rootView;
    }
}
