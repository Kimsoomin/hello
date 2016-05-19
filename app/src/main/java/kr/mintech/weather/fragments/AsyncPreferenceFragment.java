package kr.mintech.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;

import kr.mintech.weather.R;
import kr.mintech.weather.SettingTest2;

/**
 * Created by SM on 2016-05-19.
 */
public class AsyncPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("어디","========== DataSyncPreferenceFragment / onCreate ==========");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_data_sync);
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("어디","========== DataSyncPreferenceFragment / onOptionsItemSelected ==========");
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingTest2.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
