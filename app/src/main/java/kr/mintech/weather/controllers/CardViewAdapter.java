package kr.mintech.weather.controllers;

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

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder>
{
  private ArrayList<ListViewItem> listViewItemList;

  public CardViewAdapter(ArrayList<ListViewItem> listViewItemList)
  {
    this.listViewItemList = listViewItemList;
    Log.d("어디", "CardViewAdapter / title : " + listViewItemList.get(0).getTitle());
    Log.d("어디", "CardViewAdapter / title : " + listViewItemList.get(1).getTitle());
    Log.d("어디", "CardViewAdapter / title : " + listViewItemList.get(2).getTitle());
    Log.d("어디", "CardViewAdapter / title : " + listViewItemList.get(3).getTitle());
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    public TextView day;
    public TextView status;
    public TextView date;
    public TextView sunriseTime;
    public TextView sunsetTime;
    public TextView temperature;
    public ImageView icon;
    public LinearLayout topContainer;

    public ViewHolder(View view)
    {
      super(view);
      day = (TextView) view.findViewById(R.id.day);
      status = (TextView) view.findViewById(R.id.status);
      date = (TextView) view.findViewById(R.id.date);
      sunriseTime = (TextView) view.findViewById(R.id.sunrise);
      sunsetTime = (TextView) view.findViewById(R.id.sunset);
      temperature = (TextView) view.findViewById(R.id.temperature);
      icon = (ImageView) view.findViewById(R.id.weather_image);
      topContainer = (LinearLayout) view.findViewById(R.id.top_container);

    }
  }

  @Override
  public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position)
  {

    Log.d("어디", "viewHolder 들어왔다!!!");
    final ListViewItem item = listViewItemList.get(position);

    Log.d("어디", "viewholder : " + item.getTitle());
    Log.d("어디", "viewholder : " + item.getStatus());
    Log.d("어디", "viewholder : " + item.getDate());
    Log.d("어디", "viewholder : " + item.getSunriseTime());

    holder.day.setText(item.getTitle());
    holder.status.setText(item.getStatus());
    holder.date.setText(item.getDate());
    holder.sunriseTime.setText("일출(am): " + item.getSunriseTime());
    holder.sunsetTime.setText("일몰(pm): " + item.getSunsetTime());
    holder.temperature.setText("평균 온도: " + item.getTemperature());

    if (item.getIcon().contains("rain"))
      holder.icon.setImageResource(R.drawable.ic_weather_rain);
    else if (item.getIcon().contains("cloud"))
      holder.icon.setImageResource(R.drawable.ic_weather_cloud);
    else
      holder.icon.setImageResource(R.drawable.ic_weather_clear);

  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public int getItemCount()
  {
    Log.d("어디","getItemCount / 리스뷰 사이즈: " +listViewItemList.size());
    //    return listViewItemList.size();
    return listViewItemList == null ? 0 : listViewItemList.size();
  }
}

