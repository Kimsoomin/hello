package kr.mintech.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mac on 16. 4. 25..
 */
public class SignInActivity extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sign_in);
    Button terminate = (Button) findViewById(R.id.terminateActivity);
    terminate.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        finish();
        // 액티비티를 종료합니다.
      }

    });

  }
}
