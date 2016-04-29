package kr.mintech.weather;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by Mac on 16. 4. 29..
 */
public class AlarmOnOffActivity extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.alarm_on_off);
    final ToggleButton tb = (ToggleButton) this.findViewById(R.id.alarm_toggle);
    tb.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (tb.isChecked())
        {
          tb.setTextColor(Color.BLUE);
        }
        else
        {
          tb.setTextColor(Color.RED);
        }
      }
    });
  }
}