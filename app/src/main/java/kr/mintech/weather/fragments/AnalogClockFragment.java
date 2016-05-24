package kr.mintech.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import kr.mintech.weather.R;
import kr.mintech.weather.SettingTest;

/**
 * Created by SM on 2016-05-19.
 */
public class AnalogClockFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("어디", "============= AnalogClockFragment onCreateView 진입 ===========");

        CharSequence mLabel;

        View rootView = inflater.inflate(R.layout.fragment_analog, container, false);

        setHasOptionsMenu(true);

    /* =============== H/W BackButton 이벤트 적용 =================*/
        ActionBar actionbar = ((SettingTest) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener()

                                  {
                                      @Override
                                      public boolean onKey(View v, int keyCode, KeyEvent event) {
                                          if (keyCode == KeyEvent.KEYCODE_BACK) {
                                              Intent intent = new Intent(getActivity(), SettingTest.class);
                                              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                              intent.putExtra("finishstatus", true);
                                              startActivity(intent);
                                              getActivity().finish();
                                              return true;
                                          }
                                          return false;
                                      }
                                  }

        );

        /* ========= ActionBar BackButton 이벤트 적용 ========*/


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("어디", "Fragment menu 진입");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("어디", "========== Analog / onOptionsItemSelected ==========");

        switch (item.getItemId()) {
            case R.id.home:

//                Intent intent = new Intent(getActivity(), SettingTest.class);
//                startActivity(intent);
//                break;
            case R.id.btn_my_location:
                getFragmentManager().beginTransaction().remove(this).commit();

                Intent intent2 = new Intent(getActivity(), SettingTest.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
