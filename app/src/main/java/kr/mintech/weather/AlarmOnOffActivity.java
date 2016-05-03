package kr.mintech.weather;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Locale;

public class AlarmOnOffActivity extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.alarm_on_off);
    final ToggleButton tb = (ToggleButton) this.findViewById(R.id.alarm_toggle);
    final CheckBox checkBox = (CheckBox) this.findViewById(R.id.alarm_checkbox);
    final RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.alarm_radio);
    final RadioButton radioButton = (RadioButton) this.findViewById(R.id.alarm_on_radio);
    final RadioButton radioButton2 = (RadioButton) this.findViewById(R.id.alarm_off_radio);

    final String language = Locale.getDefault().getLanguage();
    if (language.contains("en"))
    {
      tb.setTextOff("Notification Off");
      tb.setTextOn("Notification On");
      tb.setText("Notification Off");
    }

    if (language.contains("en"))
    {
      checkBox.setText("Notification On");
    }

    if (language.contains("en"))
    {
      radioButton.setText("Notification On");
      radioButton2.setText("Notification Off");
    }

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
          case R.id.alarm_on_radio:
            if (language.contains("en")){
              Toast.makeText(AlarmOnOffActivity.this, "Notificaion On", Toast.LENGTH_SHORT).show();
            }
            else {
            Toast.makeText(AlarmOnOffActivity.this, "알림 On", Toast.LENGTH_SHORT).show();
            }

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmOnOffActivity.this, AlarmReceive.class);   //AlarmReceive.class이클레스는 따로 만들꺼임 알람이 발동될때 동작하는 클레이스임

            PendingIntent sender = PendingIntent.getBroadcast(AlarmOnOffActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 14, 40);//시간을 10시 01분으로 일단 set했음
            calendar.set(Calendar.SECOND, 0);

            //알람 예약
            //am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);//이건 한번 알람
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, sender);//이건 여러번 알람 24*60*60*1000 이건 하루에한번 계속 알람한다는 뜻.

            break;
          case R.id.alarm_off_radio:
            if (language.contains("en")){
              Toast.makeText(AlarmOnOffActivity.this, "Notificaion Off", Toast.LENGTH_SHORT).show();
            }
            else
            {
              Toast.makeText(AlarmOnOffActivity.this, "알림 Off", Toast.LENGTH_SHORT).show();
            }
            Intent intent2 = new Intent(AlarmOnOffActivity.this, AlarmReceive.class);
            PendingIntent sender2 = PendingIntent.getBroadcast(AlarmOnOffActivity.this, 0, intent2, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(sender2);

            break;
        }
      }
    });

    tb.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (tb.isChecked())
        {
          Toast.makeText(AlarmOnOffActivity.this, "알림 On", Toast.LENGTH_SHORT).show();

          tb.setTextColor(Color.BLUE);

          AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
          Intent intent = new Intent(AlarmOnOffActivity.this, AlarmReceive.class);   //AlarmReceive.class이클레스는 따로 만들꺼임 알람이 발동될때 동작하는 클레이스임

          PendingIntent sender = PendingIntent.getBroadcast(AlarmOnOffActivity.this, 0, intent, 0);

          Calendar calendar = Calendar.getInstance();
          //알람시간 calendar에 set해주기

          calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 10, 45);//시간을 10시 01분으로 일단 set했음
          calendar.set(Calendar.SECOND, 0);

          //알람 예약
          //am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);//이건 한번 알람
          am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, sender);//이건 여러번 알람 24*60*60*1000 이건 하루에한번 계속 알람한다는 뜻.
        }
        else
        {
          Toast.makeText(AlarmOnOffActivity.this, "알림 Off", Toast.LENGTH_SHORT).show();
          tb.setTextColor(Color.RED);

          Intent intent = new Intent(AlarmOnOffActivity.this, AlarmReceive.class);
          PendingIntent sender = PendingIntent.getBroadcast(AlarmOnOffActivity.this, 0, intent, 0);
          AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
          alarmManager.cancel(sender);
        }
      }
    });
  }
}