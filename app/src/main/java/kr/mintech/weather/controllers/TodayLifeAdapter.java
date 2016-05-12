package kr.mintech.weather.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.TodayLifeItem;

/**
 * Created by Mac on 16. 5. 11..
 */
public class TodayLifeAdapter extends BaseAdapter
{
  String language;
  private TodayLifeItem todayLifeItem = new TodayLifeItem();
  private ArrayList<TodayLifeItem> todayLifeItemsList = new ArrayList<TodayLifeItem>();
  LayoutInflater inflater;

  public TodayLifeAdapter()
  {

  }

  public TodayLifeAdapter(Context context, LayoutInflater inflater, TodayLifeItem todayLifeItem)
  {
    this.todayLifeItem = todayLifeItem;
    this.inflater = inflater;
  }

  public void add(TodayLifeItem todayLifeItem)
  {
    this.todayLifeItem = todayLifeItem;
    notifyDataSetChanged();
  }


  //================================================
  @Override
  public int getCount()
  {
    return todayLifeItemsList.size();
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public Object getItem(int position)
  {
    return todayLifeItemsList.get(position);
  }

  @Override
  public View getView(int position, View convertView, final ViewGroup parent)
  {

    final TodayLifeItem item = todayLifeItemsList.get(position);

    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_life_item, parent, false);

    TextView carwash = (TextView) convertView.findViewById(R.id.carwash);
    TextView uv = (TextView) convertView.findViewById(R.id.uv);
    TextView laundry = (TextView) convertView.findViewById(R.id.laundry);
    TextView discomport = (TextView) convertView.findViewById(R.id.discomport);

    carwash.setText("세차 지수");
    uv.setText("자외선 지수");
    laundry.setText("빨래 지수");
    discomport.setText("불쾌 지수");

    return convertView;
  }
}