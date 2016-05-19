package kr.mintech.weather.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.SettingTest;
import kr.mintech.weather.beans.ListViewItem;
import kr.mintech.weather.controllers.CardViewListViewAdapterWeek;

/**
 * Created by SM on 2016-05-19.
 */
public class AnalogClockFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("어디", "============= AnalogClockFragment onCreateView 진입 ===========");

        View rootView = inflater.inflate(R.layout.fragment_setting1, container, false);
        return rootView;
    }
}
