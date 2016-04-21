package kr.mintech.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Mac on 16. 4. 15..
 */
public class Dialog extends Activity
{
  private Handler mHandler;
  private ProgressDialog mProgressDialog;
  private Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.main);
    mHandler = new Handler();

    runOnUiThread(new Runnable()
    {
      @Override
      public void run()
      {
        mProgressDialog = ProgressDialog.show(context,"",
            "잠시만 기다려 주세요.",true);
        mHandler.postDelayed( new Runnable()
        {
          @Override
          public void run()
          {
            try
            {
              if (mProgressDialog!=null&&mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
              }
            }
            catch ( Exception e )
            {
              e.printStackTrace();
            }
          }
        }, 3000);
      }
    } );
  }
}