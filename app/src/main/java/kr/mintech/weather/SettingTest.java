package kr.mintech.weather;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import kr.mintech.weather.fragments.CalendarFragment;
import kr.mintech.weather.fragments.AnalogClockFragment;
import kr.mintech.weather.fragments.DigitalClockFragment;

public class SettingTest extends AppCompatActivity {
    int mCurrentFragmentIndex;
    public final static int FRAGMENT_ONE = 0;
    public final static int FRAGMENT_TWO = 1;
    public final static int FRAGMENT_THREE = 2;
    LinearLayout settingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_test);

        LinearLayout general_container = (LinearLayout) findViewById(R.id.general_container);
        LinearLayout notification_container = (LinearLayout) findViewById(R.id.notification_container);
        LinearLayout sync_container = (LinearLayout) findViewById(R.id.sync_container);
        settingContainer = (LinearLayout) findViewById(R.id.setting_container);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        general_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                actionBar.setDisplayHomeAsUpEnabled(false);
                mCurrentFragmentIndex = FRAGMENT_ONE;
                fragmentReplace(mCurrentFragmentIndex);
            }
        });

        notification_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentFragmentIndex = FRAGMENT_TWO;
                fragmentReplace(mCurrentFragmentIndex);
            }
        });

        sync_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentFragmentIndex = FRAGMENT_THREE;
                fragmentReplace(mCurrentFragmentIndex);
            }
        });

    }

    public void fragmentReplace(int reqNewFragmentIndex) {

        android.support.v4.app.Fragment newFragment = null;

        newFragment = getFragment(reqNewFragmentIndex);

        final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        settingContainer.removeAllViews();

        transaction.replace(R.id.setting_container, newFragment);
        transaction.commit();

    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case FRAGMENT_ONE:
                newFragment = new AnalogClockFragment();
                break;
            case FRAGMENT_TWO:
                newFragment = new DigitalClockFragment();
                break;
            case FRAGMENT_THREE:
                newFragment = new CalendarFragment();
                break;

            default:
                break;
        }

        return newFragment;
    }

    /* ============================= */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        final Fragment fragment;
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("어디", "====== SettingTest 에서 ActionBar ======");
//                finish();
//                Intent testIntent = new Intent(SettingTest.this, MainActivity.class);
//                startActivity(testIntent);

                if (mCurrentFragmentIndex == 0 | mCurrentFragmentIndex == 1 | mCurrentFragmentIndex == 2) {
                    Log.d("어디", "onOption if");
                    switch (mCurrentFragmentIndex) {
                        case 0:
                            fragment = new AnalogClockFragment();
                            break;
                        case 1:
                            fragment = new DigitalClockFragment();
                            break;
                        case 2:
                            fragment = new CalendarFragment();
                            break;
                    }

                } else {
                    Log.d("어디", "onOption else");
                    finish();
                    Intent testIntent = new Intent(SettingTest.this, MainActivity.class);
                    startActivity(testIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* =========== Back Button TEST ===============*/

}
