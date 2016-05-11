package kr.mintech.weather.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

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

  public TodayLifeAdapter(Context context, LayoutInflater inflater, ArrayList<TodayLifeItem> todayLifeItemsList)
  {
    this.todayLifeItemsList = todayLifeItemsList;
    this.inflater = inflater;
  }

  public void addAll(ArrayList<TodayLifeItem> items)
  {
    this.todayLifeItemsList.addAll(items);
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

    return convertView;
  }
}