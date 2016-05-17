package kr.mintech.weather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.controllers.CardViewListViewAdapter;

/**
 * Created by Mac on 16. 5. 17..
 */
public class WeekFragment extends Fragment
{
  public static View view;
  private static CardViewListViewAdapter adapter;

  private ArrayList<ListViewItem> items;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.list_view_item;
    return inflater.inflate(resId, null);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);

//    ListView listview = (ListView) getView().findViewById(R.id.listview);
//    adapter = new CardViewListViewAdapter();
//    listview.setAdapter(adapter);
  }
}
