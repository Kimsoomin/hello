package kr.mintech.weather.fragments;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import kr.mintech.weather.MainActivity;
import kr.mintech.weather.R;
import kr.mintech.weather.SettingActivity;

/**
 * Created by SM on 2016-05-19.
 */
public class NotificationPreferenceFragment extends PreferenceFragment {
    Context context;
    NotificationManager nm;
    public static String dust;
    String icon;
    String status;
    SwitchPreference mFirst;
    SwitchPreference mSecond;
    Ringtone ringtone;
    String stringValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dust = kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getDust();
        icon = kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getIcon();
        status = kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getStatus();

        Log.d("어디", "Noti frag / " + dust);
        Log.d("어디", "Noti frag / " + icon);
        Log.d("어디", "Noti frag / " + status);

        Log.d("어디", "========== NotificationPreferenceFragment / onCreate ==========");
        addPreferencesFromResource(R.xml.pref_notification);

        mFirst = (SwitchPreference) findPreference("notifications_new_message");
        mSecond = (SwitchPreference) findPreference("notifications_new_message_vibrate");

        Log.d("어디","NotiFrag vibrate / " +kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getVibrate());
        mSecond.setChecked(kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getVibrate());

        setOnPreferenceChange(findPreference("notifications_new_message_ringtone"));
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        Log.d("onDestroy", "Notifragment");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("어디", "========== NotificationPreferenceFragment / onOptionsItemSelected ==========");
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            final PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            final Notification.Builder mBuilder = new Notification.Builder(getActivity());

            mFirst.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.d("어디", "mSecond 클릭하기전 mSecond.isChecked() / " + mSecond.isChecked());
                    Log.d("어디", "Click mFirst.isChecked() /" + mFirst.isChecked());
                    long[] vibrate = {0, 100, 200, 300};
                    long[] vibrate2 = {0, 0, 0, 0};

                    if (mFirst.isChecked() == true) {
                        if (icon.contains("rain"))
                            mBuilder.setSmallIcon(R.drawable.ic_weather_rain);
                        else if (icon.contains("cloud"))
                            mBuilder.setSmallIcon(R.drawable.ic_weather_cloud);
                        else
                            mBuilder.setSmallIcon(R.drawable.ic_weather_clear);

                        // 알림이 출력될 때 상단에 나오는 문구.
                        mBuilder.setTicker("미리 보기");

                        mBuilder.setWhen(System.currentTimeMillis());

                        // 알림 메세지 갯수
                        //    mBuilder.setNumber(10);
                        // 알림 제목.
                        mBuilder.setContentTitle(status);
                        // 알림 내용.
                        mBuilder.setContentText("미세먼지 : " + dust);
                        // 프로그래스 바.
                        //    mBuilder.setProgress(100, 50, false);
                        // 알림시 사운드, 진동, 불빛을 설정 가능.
                        // 알림 터치시 반응.
                        mBuilder.setContentIntent(pendingIntent);
                        // 알림 터치시 반응 후 알림 삭제 여부.
                        mBuilder.setAutoCancel(false);
                        // 우선순위.
                        mBuilder.setPriority(Notification.PRIORITY_MAX);

                        mBuilder.setOngoing(true);

//                        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                        if (kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getVibrate() == true){
                            mBuilder.setVibrate(vibrate);
                        } else if (kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).getVibrate() == false){
                            mBuilder.setVibrate(vibrate2);
                        }

                        Log.d("어디", "벨소리 지정 / " + stringValue);
                        Uri ringUri = Uri.parse(stringValue);
                        mBuilder.setSound(ringUri);

                        // 고유ID로 알림을 생성.
                        nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.notify(111, mBuilder.build());
                    } else {
                        nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.cancelAll();
                    }

                    return false;
                }
            });

            mSecond.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Log.d("어디", "ClickListener mSecond.isChecked() / " + mSecond.isChecked());
                    if (mSecond.isChecked() == true) {
                        kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).setVibrate(true);

                    } else if (mSecond.isChecked() == false) {
                        kr.mintech.weather.managers.PreferenceManager.getInstance(getActivity()).setVibrate(false);
                    }
                    return false;
                }
            });


            stringValue = newValue.toString();

            if (preference instanceof RingtonePreference) {

                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary("무음으로 설정됨");

                } else {
                    ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        preference.setSummary(null);

                    } else {
                        String name = ringtone
                                .getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            }

            return true;
        }
    };

    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        onPreferenceChangeListener.onPreferenceChange(mPreference,
                PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
    }

}

