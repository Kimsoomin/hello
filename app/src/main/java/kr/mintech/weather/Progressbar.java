package kr.mintech.weather;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Mac on 16. 4. 15..
 */
public class Progressbar
{
  private Context context;
  private ProgressDialog dialog;

  public void ProgressStart(){
      dialog = ProgressDialog.show(context, "",
        "잠시만 기다려 주세요 ...", true);
  }

  public void ProgressStop(){
      dialog.dismiss();
  }
}
