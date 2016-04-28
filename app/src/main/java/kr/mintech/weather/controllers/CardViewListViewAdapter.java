package kr.mintech.weather.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;

public class CardViewListViewAdapter extends BaseAdapter
{
  private ListViewItem listViewItem = new ListViewItem();
  private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
  LayoutInflater inflater;

  public CardViewListViewAdapter()
  {

  }

  public ListViewItem CardViewListViewAdapterReceive()
  {
    return this.listViewItem;
  }

  public CardViewListViewAdapter(Context context, LayoutInflater inflater, ArrayList<ListViewItem> listViewItemList)
  {
    this.listViewItemList = listViewItemList;
    this.inflater = inflater;
  }

  public void addAll(ArrayList<ListViewItem> items)
  {
    this.listViewItemList.addAll(items);
    notifyDataSetChanged();
  }

  public void add(String res)
  {
    this.listViewItem.setDust(res);
  }

  //================================================
  @Override
  public int getCount()
  {
    return listViewItemList.size();
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public Object getItem(int position)
  {
    return listViewItemList.get(position);
  }

  @Override
  public View getView(int position, View convertView, final ViewGroup parent)
  {
// ============================== 기존 소스 =================================
    final ListViewItem item = listViewItemList.get(position);
    
    if (position==0)
    {
      convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item_main, parent, false);

      TextView day_main = (TextView) convertView.findViewById(R.id.day_main);
      TextView status_main = (TextView) convertView.findViewById(R.id.status_main);
      TextView date_main = (TextView) convertView.findViewById(R.id.date_main);
      TextView sunriseTime_main = (TextView) convertView.findViewById(R.id.sunrise_main);
      TextView sunsetTime_main = (TextView) convertView.findViewById(R.id.sunset_main);
      TextView temperature_main = (TextView) convertView.findViewById(R.id.temperature_main);
      TextView dust_main = (TextView) convertView.findViewById(R.id.dust_main);
      ImageView icon_main = (ImageView) convertView.findViewById(R.id.weather_image_main);
      LinearLayout topContainer_main = (LinearLayout) convertView.findViewById(R.id.top_container_main);
      Button detail_button = (Button) convertView.findViewById(R.id.detail_button);

      final LinearLayout detail_container = (LinearLayout) convertView.findViewById(R.id.detail_container);

      TextView detail_dewpoint = (TextView) convertView.findViewById(R.id.detail_dewpoint);
      TextView explain_dewpoint = (TextView) convertView.findViewById(R.id.explain_dewpoint);
      TextView detail_humidity = (TextView) convertView.findViewById(R.id.detail_humidity);
      TextView explain_humidity = (TextView) convertView.findViewById(R.id.explain_humidity);
      TextView detail_windspeed = (TextView) convertView.findViewById(R.id.detail_windspeed);
      TextView explain_windspeed = (TextView) convertView.findViewById(R.id.explain_windspeed);
      TextView detail_pressure = (TextView) convertView.findViewById(R.id.detail_pressure);
      TextView explain_pressure = (TextView) convertView.findViewById(R.id.explain_pressure);

      ImageView img_dewpoint = (ImageView) convertView.findViewById(R.id.img_dewpoint);
      ImageView img_humidity = (ImageView) convertView.findViewById(R.id.img_humidity);
      ImageView img_windspeed = (ImageView) convertView.findViewById(R.id.img_windspeed);
      ImageView img_pressure = (ImageView) convertView.findViewById(R.id.img_pressure);

      // =====================================

      day_main.setText("오늘 " +item.getTitle()+"요일");
      status_main.setText(item.getStatus());
      date_main.setText(item.getDate());
      sunriseTime_main.setText(item.getSunriseTime());
      sunsetTime_main.setText(item.getSunsetTime());
      temperature_main.setText(item.getTemperature());
      detail_button.setText("detail");
      detail_dewpoint.setText(item.getDewpoint()+ "°");
      detail_humidity.setText(item.getHumidity()+ "%");
      detail_windspeed.setText(item.getWindspeed()+ "M/초");
      detail_pressure.setText(item.getPressure()+ "hPa");
      Log.d("어디","어댑터 getDust : " +listViewItem.getDust());
      dust_main.setText("미세먼지 : " +listViewItem.getDust());

      if (item.getIcon().contains("rain"))
        icon_main.setImageResource(R.drawable.ic_weather_rain);
      else if (item.getIcon().contains("cloud"))
        icon_main.setImageResource(R.drawable.ic_weather_cloud);
      else
        icon_main.setImageResource(R.drawable.ic_weather_clear);

      detail_button.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          detail_container.setVisibility(detail_container.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
          Log.d("어디", "버튼클릭");
        }
      });
    }
/* ========== else ===========*/
    else {

      convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
      
      TextView day = (TextView) convertView.findViewById(R.id.day);
      TextView status = (TextView) convertView.findViewById(R.id.status);
      TextView date = (TextView) convertView.findViewById(R.id.date);
      TextView sunriseTime = (TextView) convertView.findViewById(R.id.sunrise);
      TextView sunsetTime = (TextView) convertView.findViewById(R.id.sunset);
      TextView temperature = (TextView) convertView.findViewById(R.id.temperature);
      ImageView icon = (ImageView) convertView.findViewById(R.id.weather_image);
      LinearLayout topContainer = (LinearLayout) convertView.findViewById(R.id.top_container);

      day.setText(item.getTitle());
      status.setText(item.getStatus());
      date.setText(item.getDate());
      sunriseTime.setText("일출(am): " + item.getSunriseTime());
      sunsetTime.setText("일몰(pm): " + item.getSunsetTime());
      temperature.setText("평균 온도: " + item.getTemperature());

      if (item.getIcon().contains("rain"))
        icon.setImageResource(R.drawable.ic_weather_rain);
      else if (item.getIcon().contains("cloud"))
        icon.setImageResource(R.drawable.ic_weather_cloud);
      else
        icon.setImageResource(R.drawable.ic_weather_clear);

    }

    return convertView;
  }
}

