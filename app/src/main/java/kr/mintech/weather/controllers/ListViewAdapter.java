package kr.mintech.weather.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;

public class ListViewAdapter extends BaseAdapter
{
  private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
  LayoutInflater inflater;

  public ListViewAdapter(Context context, LayoutInflater inflater, ArrayList<ListViewItem> listViewItemList)
  {
    this.listViewItemList = listViewItemList;
    this.inflater = inflater;
    //        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void addItem(ListViewItem item)
  {
    listViewItemList.add(item);
  }

  public void addAll(ArrayList<ListViewItem> items)
  {
    this.listViewItemList.addAll(items);
    notifyDataSetChanged();
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

    //리스트뷰 성능에 관한 부분
    ViewHolder holder;
    if (convertView == null)
    {
      convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
//      convertView = inflater.inflate(R.layout.listview_item, parent, false);

      holder = new ViewHolder();
      holder.item = listViewItemList.get(position);
      holder.day = (TextView) convertView.findViewById(R.id.day);
      holder.status = (TextView) convertView.findViewById(R.id.status);
      holder.date = (TextView) convertView.findViewById(R.id.date);
      holder.sunriseTime = (TextView) convertView.findViewById(R.id.sunrise);
      holder.sunsetTime = (TextView) convertView.findViewById(R.id.sunset);

      holder.temperature = (TextView) convertView.findViewById(R.id.temperature);
      holder.icon = (ImageView) convertView.findViewById(R.id.weather_image);
      holder.topContainer = (LinearLayout) convertView.findViewById(R.id.top_container);

      convertView.setTag(holder);
    }

    else
    {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.day.setText(holder.item.getTitle());
    holder.status.setText(holder.item.getStatus());
    holder.date.setText(holder.item.getDate());
    holder.sunriseTime.setText("일출(am): " + holder.item.getSunriseTime());
    holder.sunsetTime.setText("일몰(pm): " + holder.item.getSunsetTime());
    holder.temperature.setText("평균 온도: " + holder.item.getTemperature());

    if (holder.item.getIcon().contains("rain"))
      holder.icon.setImageResource(R.drawable.ic_weather_rain);
    else if (holder.item.getIcon().contains("cloud"))
      holder.icon.setImageResource(R.drawable.ic_weather_cloud);
    else
      holder.icon.setImageResource(R.drawable.ic_weather_clear);

// ============================== ^ holder 테스트 ^==========================
// ============================== 기존 소스 ==============================
//    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
//    ListViewItem item = listViewItemList.get(position);
//
//    TextView day = (TextView) convertView.findViewById(R.id.day);
//    TextView status = (TextView) convertView.findViewById(R.id.status);
//    TextView date = (TextView) convertView.findViewById(R.id.date);
//    TextView sunriseTime = (TextView) convertView.findViewById(R.id.sunrise);
//    TextView sunsetTime = (TextView) convertView.findViewById(R.id.sunset);
//
//    final TextView temperature = (TextView) convertView.findViewById(R.id.temperature);
//    ImageView icon = (ImageView) convertView.findViewById(R.id.weather_image);
//    LinearLayout topContainer = (LinearLayout) convertView.findViewById(R.id.top_container);
//
//    day.setText(item.getTitle());
//    status.setText(item.getStatus());
//    date.setText(item.getDate());
//    sunriseTime.setText("일출(am): " + item.getSunriseTime());
//    sunsetTime.setText("일몰(pm): " + item.getSunsetTime());
//    temperature.setText("평균 온도: " + item.getTemperature());
//
//    if (item.getIcon().contains("rain"))
//      icon.setImageResource(R.drawable.ic_weather_rain);
//    else if (item.getIcon().contains("cloud"))
//      icon.setImageResource(R.drawable.ic_weather_cloud);
//    else
//      icon.setImageResource(R.drawable.ic_weather_clear);

//        topContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                temperature.setVisibility(temperature.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//            }
//        });

    return convertView;
  }

  private static class ViewHolder
  {
    public TextView day;
    public TextView status;
    public TextView date;
    public TextView sunriseTime;
    public TextView sunsetTime;
    public TextView temperature;
    public ImageView icon;
    public LinearLayout topContainer;
    public ListViewItem item;

  }

}