package kr.mintech.weather;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import kr.mintech.weather.managers.PreferenceManager;

/**
 * Created by Mac on 16. 5. 9..
 */
public class SettingActivityAgo extends AppCompatActivity
{
  NotificationManager nm;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.setting_ago);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    TextView notification = (TextView) findViewById(R.id.notification);
    TextView notification_extend = (TextView) findViewById(R.id.notification_extend);
    final Switch switchButton = (Switch) findViewById(R.id.switch_button);

    SharedPreferences pref = getSharedPreferences("setting_ago", Activity.MODE_PRIVATE);
    Log.d("어디", "Setting" + pref.getBoolean("notification", true));

    switchButton.setChecked(pref.getBoolean("notification", true));

    notification_extend.setText("확장 알림");

    final String language = Locale.getDefault().getLanguage();
    if (language.contains("en"))
    {
      switchButton.setTextOff("Notification Off");
      switchButton.setTextOn("Notification On");
      switchButton.setText("Notification Off");
    }

    Intent intent = getIntent();
    final String dust = PreferenceManager.getInstance(SettingActivityAgo.this).getDust();
    final String icon = PreferenceManager.getInstance(SettingActivityAgo.this).getIcon();
    final String status = PreferenceManager.getInstance(SettingActivityAgo.this).getStatus();

    Log.d("어디", "===== 설정 ==== /" + dust);
    Log.d("어디", "===== 설정 ==== /" + icon);
    Log.d("어디", "===== 설정 ==== /" + status);

    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    final Notification.Builder mBuilder = new Notification.Builder(SettingActivityAgo.this);

    switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        if (switchButton.isChecked())
        {
          SharedPreferences pref = getSharedPreferences("setting_ago", Activity.MODE_PRIVATE);
          SharedPreferences.Editor editor = pref.edit();
          editor.putBoolean("notification", true);
          editor.commit();

          Toast.makeText(SettingActivityAgo.this, "알림 On", Toast.LENGTH_SHORT).show();

          // 작은 아이콘 이미지.
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
          mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
          // 알림 터치시 반응.
          mBuilder.setContentIntent(pendingIntent);
          // 알림 터치시 반응 후 알림 삭제 여부.
          mBuilder.setAutoCancel(false);
          // 우선순위.
          mBuilder.setPriority(Notification.PRIORITY_MAX);

          mBuilder.setOngoing(true);

          // 행동 최대3개 등록 가능.
          //      mBuilder.addAction(R.mipmap.ic_launcher, "Show", pendingIntent);
          //      mBuilder.addAction(R.mipmap.ic_launcher, "Hide", pendingIntent);
          //      mBuilder.addAction(R.mipmap.ic_launcher, "Remove", pendingIntent);

          // 고유ID로 알림을 생성.
          nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          nm.notify(111, mBuilder.build());

        }
        else
        {
          Toast.makeText(SettingActivityAgo.this, "알림 Off", Toast.LENGTH_SHORT).show();

          SharedPreferences pref = getSharedPreferences("setting_ago", Activity.MODE_PRIVATE);
          SharedPreferences.Editor editor = pref.edit();
          editor.putBoolean("notification", false);
          editor.commit();

          nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          nm.cancelAll();

        }
      }
    });

    //    ============== AlarmManager 를 이용한 Notification ================
    //    tb.setOnClickListener(new View.OnClickListener()
    //    {
    //      @Override
    //      public void onClick(View v)
    //      {
    //        if (tb.isChecked())
    //        {
    //          Toast.makeText(SettingActivityAgo.this, "알림 On", Toast.LENGTH_SHORT).show();
    //
    //          tb.setTextColor(Color.BLUE);
    //
    //          AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    //          Intent intent = new Intent(SettingActivityAgo.this, AlarmReceive.class);   //AlarmReceive.class이클레스는 따로 만들꺼임 알람이 발동될때 동작하는 클레이스임
    //
    //          PendingIntent sender = PendingIntent.getBroadcast(SettingActivityAgo.this, 0, intent, 0);
    //
    //          Calendar calendar = Calendar.getInstance();
    //          //알람시간 calendar에 set해주기
    //
    //          calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 10, 45);//시간을 10시 01분으로 일단 set했음
    //          calendar.set(Calendar.SECOND, 0);
    //
    //          //알람 예약
    //          //am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);//이건 한번 알람
    //          am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, sender);//이건 여러번 알람 24*60*60*1000 이건 하루에한번 계속 알람한다는 뜻.
    //        }
    //        else
    //        {
    //          Toast.makeText(SettingActivityAgo.this, "알림 Off", Toast.LENGTH_SHORT).show();
    //          tb.setTextColor(Color.RED);
    //
    //          Intent intent = new Intent(SettingActivityAgo.this, AlarmReceive.class);
    //          PendingIntent sender = PendingIntent.getBroadcast(SettingActivityAgo.this, 0, intent, 0);
    //          AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    //          alarmManager.cancel(sender);
    //        }
    //      }
    //    });

  }

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
