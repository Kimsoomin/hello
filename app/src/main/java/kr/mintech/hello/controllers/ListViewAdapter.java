package kr.mintech.hello.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.mintech.hello.R;
import kr.mintech.hello.beans.ListViewItem;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public void addItem(String title) {
        ListViewItem item = new ListViewItem();
        item.setTitle(title);
        listViewItemList.add(item);
    }

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
        TextView title = (TextView) convertView.findViewById(R.id.text_view);
        final Button button = (Button) convertView.findViewById(R.id.button);

        title.setText(listViewItemList.get(position).getTitle());
        title.setOnClickListener(new View.OnClickListener() {
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