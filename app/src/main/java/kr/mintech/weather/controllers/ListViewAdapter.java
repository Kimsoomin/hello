package kr.mintech.weather.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;

public class ListViewAdapter extends BaseAdapter {
    public static ProgressBar progressBar;
    public static View view;
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    LayoutInflater inflater;

    public ListViewAdapter(Context context, LayoutInflater inflater, ArrayList<ListViewItem> listViewItemList) {
        this.listViewItemList = listViewItemList;
        this.inflater = inflater;
    }

    public void addItem(ListViewItem item) {
        listViewItemList.add(item);
    }

    public void addAll(ArrayList<ListViewItem> items) {
        this.listViewItemList.addAll(items);
        notifyDataSetChanged();
    }

    //================================================
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        ListViewItem item = listViewItemList.get(position);
        TextView day = (TextView) convertView.findViewById(R.id.day);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView sunriseTime = (TextView) convertView.findViewById(R.id.sunrise);
        TextView sunsetTime = (TextView) convertView.findViewById(R.id.sunset);

        final TextView temperature = (TextView) convertView.findViewById(R.id.temperature);
        ImageView icon = (ImageView) convertView.findViewById(R.id.weather_image);
        LinearLayout topContainer = (LinearLayout) convertView.findViewById(R.id.top_container);

        //===================

        day.setText(item.getTitle());
        status.setText(item.getStatus());
        date.setText(item.getDate());
        sunriseTime.setText("일출시간: "+item.getSunriseTime());
        sunsetTime.setText("일몰시간: "+item.getSunsetTime());
        temperature.setText("평균 온도: "+item.getTemperature());

        if (item.getIcon().contains("rain"))
            icon.setImageResource(R.drawable.ic_weather_rain);
        else if (item.getIcon().contains("cloud"))
            icon.setImageResource(R.drawable.ic_weather_cloud);
        else
            icon.setImageResource(R.drawable.ic_weather_clear);

//        topContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                temperature.setVisibility(temperature.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//            }
//        });

        return convertView;
    }
}