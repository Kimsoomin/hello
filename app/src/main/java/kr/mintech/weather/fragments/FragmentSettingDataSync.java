package kr.mintech.weather.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.mintech.weather.R;

/**
 * Created by SM on 2016-05-19.
 */
public class FragmentSettingDataSync extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("어디", "============= WeekFragment onCreateView 진입 ===========");

        View rootView = inflater.inflate(R.layout.fragment_setting3, container, false);

        return rootView;
    }
}