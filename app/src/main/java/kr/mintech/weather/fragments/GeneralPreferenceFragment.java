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
public class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("어디","========== GeneralPreferenceFragment / onCreate ==========");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
//        bindPreferenceSummaryToValue(findPreference("example_text"));
//        bindPreferenceSummaryToValue(findPreference("example_list"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("어디","========== GeneralPreferenceFragment / onOptionsItemSelected ==========");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingTest2.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

