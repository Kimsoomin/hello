package kr.mintech.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;

import kr.mintech.weather.R;
import kr.mintech.weather.SettingActivity;

/**
 * Created by SM on 2016-05-19.
 */
public class GeneralPreferenceFragment extends PreferenceFragment{
    private AppCompatDelegate mDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("어디","========== GeneralPreferenceFragment / onCreate ==========");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("어디","========== GeneralPreferenceFragment / onOptionsItemSelected ==========");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

