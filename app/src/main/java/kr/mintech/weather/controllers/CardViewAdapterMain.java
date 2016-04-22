package kr.mintech.weather.controllers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;

public class CardViewAdapterMain extends RecyclerView.Adapter<CardViewAdapterMain.ViewHolder>
{
  private Context context;
  private ArrayList<ListViewItem> listViewItemList;
  private ListViewItem listViewItemList1;

  public CardViewAdapterMain(ArrayList<ListViewItem> listViewItemList)
  {
    this.listViewItemList = listViewItemList;
    for (int i = 0; i < 1; i++)
    {
      listViewItemList1 = listViewItemList.get(0);
    }
//    add(listViewItemList1);

    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getTitle());
    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getDate());
    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getStatus());
    Log.d("어디", " 오늘 정보만 빼기 listViewItemList1: " + listViewItemList1.getTemperature());
  }
//
//  public void add(ListViewItem listViewItemList1)
//  {
//    Log.d("어디", "add 진입");
//    this.listViewItemList.add(0, listViewItemList1);
//    notifyDataSetChanged();
//  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    public TextView day_main;
    public TextView status_main;
    public TextView date_main;
    public TextView sunriseTime_main;
    public TextView sunsetTime_main;
    public TextView temperature_main;
    public ImageView icon_main;
    public LinearLayout topContainer_main;

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
    }
  }

  @Override
  public CardViewAdapterMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_view, parent, false);
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public void onBindViewHolder(ViewHolder holder, int position)
  {
    Log.d("어디", "viewHolder 들어왔다!!!");

    Log.d("어디", "viewholder : " + listViewItemList1.getTitle());
    Log.d("어디", "viewholder : " + listViewItemList1.getStatus());
    Log.d("어디", "viewholder : " + listViewItemList1.getDate());

    holder.day_main.setText(listViewItemList1.getTitle());
    holder.status_main.setText(listViewItemList1.getStatus());
    holder.date_main.setText(listViewItemList1.getDate());
    holder.sunriseTime_main.setText("일출(am): " + listViewItemList1.getSunriseTime());
    holder.sunsetTime_main.setText("일몰(pm): " + listViewItemList1.getSunsetTime());
    holder.temperature_main.setText("평균 온도: " + listViewItemList1.getTemperature());

    if (listViewItemList1.getIcon().contains("rain"))
      holder.icon_main.setImageResource(R.drawable.ic_weather_rain);
    else if (listViewItemList1.getIcon().contains("cloud"))
      holder.icon_main.setImageResource(R.drawable.ic_weather_cloud);
    else
      holder.icon_main.setImageResource(R.drawable.ic_weather_clear);

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

