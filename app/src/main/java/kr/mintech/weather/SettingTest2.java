package kr.mintech.weather;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import kr.mintech.weather.fragments.AsyncPreferenceFragment;
import kr.mintech.weather.fragments.GeneralPreferenceFragment;
import kr.mintech.weather.fragments.NotificationPreferenceFragment;


public class SettingTest2 extends AppCompatActivity {
    int mCurrentFragmentIndex;
    public final static int FRAGMENT_ONE = 0;
    public final static int FRAGMENT_TWO = 1;
    public final static int FRAGMENT_THREE = 2;
    LinearLayout settingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_test2);

        LinearLayout general_container = (LinearLayout) findViewById(R.id.general_container);
        LinearLayout notification_container = (LinearLayout) findViewById(R.id.notification_container);
        LinearLayout sync_container = (LinearLayout) findViewById(R.id.sync_container);
        settingContainer = (LinearLayout) findViewById(R.id.setting_container);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        general_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingContainer.removeAllViews();
                getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content,
                                new GeneralPreferenceFragment()).commit();
            }
        });

        notification_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingContainer.removeAllViews();
                getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content,
                                new NotificationPreferenceFragment()).commit();
            }
        });

        sync_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingContainer.removeAllViews();
                getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content,
                                new AsyncPreferenceFragment()).commit();
            }
        });
    }

    /* ============================= */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
