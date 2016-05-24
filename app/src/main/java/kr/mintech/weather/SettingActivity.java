package kr.mintech.weather;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import kr.mintech.weather.fragments.NotificationPreferenceFragment;


public class SettingActivity extends AppCompatActivity {
    LinearLayout settingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        LinearLayout notification_container = (LinearLayout) findViewById(R.id.notification_container);
        settingContainer = (LinearLayout) findViewById(R.id.setting_container);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    }

    /* ============================= */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
