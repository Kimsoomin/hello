package kr.mintech.weather.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class WeatherFragment extends Fragment
{
  private static int LCOATION_TIME_OUT_SECOND = 30 * 1000;

  private static View view;
  private ProgressBar progressBar;
  private Handler handler = new Handler();

  private static double setDestFindNearStation = -1;

  private LocationManager locationManager;
  private LocationListener locationListener;

  private Activity activity;


//  public void receive()
//  {
//    findNearByStation();
//  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    return view;
  }


  @Override
  public void onResume()
  {
  }


//  @Override
//  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//  {
//    if (grantResults[0] != -1)
//      findNearByStation();
//  }
//
//  private void findNearByStation()
//  {
//    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//    locationListener = new WeatherLocationListener();
//
//    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//      buildAlertMessageNoGps();
//    else
//    {
//      progressBar.setVisibility(View.VISIBLE);
//      progressBar.bringToFront();
//
//      if (android.os.Build.VERSION.SDK_INT >= 23)
//      {
//        Log.d("어디", "sdk버전 " + android.os.Build.VERSION.SDK_INT);
//        Log.d("어디", "check " + ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION));
//
//        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//            && PackageManager.PERMISSION_GRANTED ==ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION))
//          requestLocation();
//        else
//        {
//          requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//        }
//      }
//      else
//        requestLocation();
//    }
//  }
//
//
//  private void requestLocation()
//  {
//    handler.postDelayed(timeOutFindStationCallBack, LCOATION_TIME_OUT_SECOND);
//    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//  }
//
//  private Runnable timeOutFindStationCallBack = new Runnable()
//  {
//    public void run()
//    {
//      locationManager.removeUpdates(locationListener);
//      Builder builder = new Builder(getActivity());
//      builder.setTitle(getString(R.string.app_name));
//      builder.setMessage(getString(R.string.map_gps_Location_fail));
//      builder.setPositiveButton(android.R.string.ok, null);
//      builder.create().show();
//    }
//  };
//
//  private void buildAlertMessageNoGps()
//  {
//    final Builder builder = new Builder(activity);
//    builder.setTitle(R.string.app_name).setMessage(R.string.msg_please_gps_enable).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
//    {
//      public void onClick(final DialogInterface dialog, final int id)
//      {
//        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//      }
//    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
//    {
//      public void onClick(final DialogInterface dialog, final int id)
//      {
//        dialog.cancel();
//      }
//    });
//    final AlertDialog alert = builder.create();
//    alert.show();
//  }
//
//
//  private class WeatherLocationListener implements LocationListener
//  {
//
//    @Override
//    public void onLocationChanged(Location loc)
//    {
//      handler.removeCallbacks(timeOutFindStationCallBack);
//      Log.w("WARN", "LocationChanged! " + loc.getLatitude() + " / " + loc.getLongitude());
//      locationManager.removeUpdates(locationListener);
//      setDestFindNearStation = -1;
//      progressBar.setVisibility(View.GONE);
//    }
//
//
//    @Override
//    public void onProviderDisabled(String provider)
//    {
//    }
//
//
//    @Override
//    public void onProviderEnabled(String provider)
//    {
//    }
//
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras)
//    {
//    }
//  }

}


