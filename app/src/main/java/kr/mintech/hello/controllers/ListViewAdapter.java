package kr.mintech.hello.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.mintech.hello.R;
import kr.mintech.hello.beans.ListViewItem;

public class ListViewAdapter extends BaseAdapter {
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
        ImageView weatherImage = (ImageView) convertView.findViewById(R.id.weather_image);
        LinearLayout topContainer = (LinearLayout) convertView.findViewById(R.id.top_container);
        //===================
        final Button button = (Button) convertView.findViewById(R.id.button);

        day.setText(item.getTitle());
        status.setText(item.getStatus());
        date.setText(item.getDate());
//        weatherImage.setImageResource(item.getWeatherImage());

        topContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(button.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "Toast", Toast.LENGTH_LONG).show();
            }
        }));
        return convertView;
    }
}