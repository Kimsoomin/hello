package kr.mintech.weather.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;

public class CardViewAdapterMain extends RecyclerView.Adapter<CardViewAdapterMain.ViewHolder>
{
  private ArrayList<ListViewItem> listViewItemList;
  private ListViewItem listViewItemList1;
  private Context context;

  public CardViewAdapterMain(ArrayList<ListViewItem> listViewItemList)
  {
    this.listViewItemList = listViewItemList;
    for (int i = 0; i < 1; i++)
    {
      listViewItemList1 = listViewItemList.get(i);
    }

    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getTitle());
    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getStatus());
    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getTemperature());
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    public TextView day_main;
    public TextView status_main;
    public TextView date_main;
    public TextView sunriseTime_main;
    public TextView sunsetTime_main;
    public TextView temperature_main;
    public TextView update_hour_main;
    public ImageView icon_main;
    public LinearLayout topContainer_main;

    public LinearLayout detail_container;
    public Button detail_button;

    public TextView detail_dewpoint;
    public TextView explain_dewpoint;
    public TextView detail_humidity;
    public TextView explain_humidity;
    public TextView detail_windspeed;
    public TextView explain_windspeed;
    public TextView detail_visibility;
    public TextView explain_visibility;
    public TextView detail_pressure;
    public TextView explain_pressure;

    public ImageView img_dewpoint;
    public ImageView img_humidity;
    public ImageView img_windspeed;
    public ImageView img_visibility;
    public ImageView img_pressure;

    public ViewHolder(View view)
    {
      super(view);
      day_main = (TextView) view.findViewById(R.id.day_main);
      status_main = (TextView) view.findViewById(R.id.status_main);
      date_main = (TextView) view.findViewById(R.id.date_main);
      sunriseTime_main = (TextView) view.findViewById(R.id.sunrise_main);
      sunsetTime_main = (TextView) view.findViewById(R.id.sunset_main);
      temperature_main = (TextView) view.findViewById(R.id.temperature_main);
      icon_main = (ImageView) view.findViewById(R.id.weather_image_main);
      topContainer_main = (LinearLayout) view.findViewById(R.id.top_container_main);
      update_hour_main = (TextView) view.findViewById(R.id.update_hour_main);
      detail_button = (Button) view.findViewById(R.id.detail_button);

      detail_container = (LinearLayout) view.findViewById(R.id.detail_container);

      detail_dewpoint = (TextView) view.findViewById(R.id.detail_dewpoint);
      explain_dewpoint = (TextView) view.findViewById(R.id.explain_dewpoint);
      detail_humidity = (TextView) view.findViewById(R.id.detail_humidity);
      explain_humidity = (TextView) view.findViewById(R.id.explain_humidity);
      detail_windspeed = (TextView) view.findViewById(R.id.detail_windspeed);
      explain_windspeed = (TextView) view.findViewById(R.id.explain_windspeed);
      detail_visibility = (TextView) view.findViewById(R.id.detail_visibility);
      explain_visibility = (TextView) view.findViewById(R.id.explain_visibility);
      detail_pressure = (TextView) view.findViewById(R.id.detail_pressure);
      explain_pressure = (TextView) view.findViewById(R.id.explain_pressure);

      img_dewpoint = (ImageView) view.findViewById(R.id.img_dewpoint);
      img_humidity = (ImageView) view.findViewById(R.id.img_humidity);
      img_windspeed = (ImageView) view.findViewById(R.id.img_windspeed);
      img_visibility = (ImageView) view.findViewById(R.id.img_visibility);
      img_pressure = (ImageView) view.findViewById(R.id.img_pressure);
    }
  }

  @Override
  public CardViewAdapterMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item_main, parent, false);
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position)
  {
    final ViewHolder finalHolder = holder;
    Log.d("어디", "메인 viewHolder 들어왔다!!!");

    Log.d("메인", "viewholder : " + listViewItemList1.getTitle());
    Log.d("메인", "viewholder : " + listViewItemList1.getStatus());
    Log.d("메인", "viewholder : " + listViewItemList1.getDate());

    holder.day_main.setText("오늘 " +listViewItemList1.getTitle()+"요일");
    holder.update_hour_main.setText("Last update");
    holder.status_main.setText(listViewItemList1.getStatus());
    holder.date_main.setText(listViewItemList1.getDate());
    holder.sunriseTime_main.setText("일출(am): " + listViewItemList1.getSunriseTime());
    holder.sunsetTime_main.setText("일몰(pm): " + listViewItemList1.getSunsetTime());
    holder.temperature_main.setText(listViewItemList1.getTemperature());
    holder.detail_button.setText("detail");
    holder.detail_dewpoint.setText(listViewItemList1.getDewpoint()+ "°");
    holder.detail_humidity.setText(listViewItemList1.getHumidity()+ "%");
    holder.detail_windspeed.setText(listViewItemList1.getWindspeed()+ "M/초");
    holder.detail_visibility.setText(listViewItemList1.getVisibility()+ "km");
    holder.detail_pressure.setText(listViewItemList1.getPressure()+ "hPa");

    if (listViewItemList1.getIcon().contains("rain"))
      holder.icon_main.setImageResource(R.drawable.ic_weather_rain);
    else if (listViewItemList1.getIcon().contains("cloud"))
      holder.icon_main.setImageResource(R.drawable.ic_weather_cloud);
    else
      holder.icon_main.setImageResource(R.drawable.ic_weather_clear);

    holder.detail_button.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finalHolder.detail_container.setVisibility(finalHolder.detail_container.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        Log.d("어디", "버튼클릭");
      }
    });

  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public int getItemCount()
  {
    return 1;
  }
}

