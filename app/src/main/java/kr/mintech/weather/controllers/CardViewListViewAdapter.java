package kr.mintech.weather.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.ListViewItem;

public class CardViewListViewAdapter extends BaseAdapter {
    String language;
    public static ListViewItem listViewItem = new ListViewItem();
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    LayoutInflater inflater;

    public CardViewListViewAdapter() {

    }

    public CardViewListViewAdapter(Context context, LayoutInflater inflater, ArrayList<ListViewItem> listViewItemList) {
        this.listViewItemList = listViewItemList;
        this.inflater = inflater;

    }

    public void addAll(ArrayList<ListViewItem> items) {
        this.listViewItemList.addAll(items);
        notifyDataSetChanged();
    }

    public void add(String dust, String value) {
        this.listViewItem.setDust(dust);
        this.listViewItem.setDustValue(value);
        Log.d("어디","========== 미세먼지 todayAdapter add ==========" +listViewItem.getDust());
    }

    public void addListviewitem(ListViewItem listviewitem) {
        this.listViewItem = listviewitem;
    }

    //================================================
    @Override
    public int getCount() {
        return 1;
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

        language = Locale.getDefault().getLanguage();
        // ============================== 기존 소스 =================================
        final ListViewItem item = listViewItemList.get(position);

        if (position == 0) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item_main, parent, false);

            TextView day_main = (TextView) convertView.findViewById(R.id.day_main);
            TextView status_main = (TextView) convertView.findViewById(R.id.status_main);
            TextView date_main = (TextView) convertView.findViewById(R.id.date_main);
            TextView temperature_main = (TextView) convertView.findViewById(R.id.temperature_main);
            TextView dust_main = (TextView) convertView.findViewById(R.id.dust_main);
            TextView dust_value_main = (TextView) convertView.findViewById(R.id.dust_value_main);
            ImageView icon_main = (ImageView) convertView.findViewById(R.id.weather_image_main);
            LinearLayout topContainer_main = (LinearLayout) convertView.findViewById(R.id.top_container_main);
            TextView detail_text = (TextView) convertView.findViewById(R.id.detail_button_main);
            final ImageView down_arrow = (ImageView) convertView.findViewById(R.id.down_arrow);
            final ImageView up_arrow = (ImageView) convertView.findViewById(R.id.up_arrow);

            final LinearLayout detail_container_main = (LinearLayout) convertView.findViewById(R.id.detail_container_main);
            final LinearLayout detail_arrow = (LinearLayout) convertView.findViewById(R.id.detail_arrow);

            TextView detail_dewpoint_main = (TextView) convertView.findViewById(R.id.detail_dewpoint_main);
            TextView explain_dewpoint_main = (TextView) convertView.findViewById(R.id.explain_dewpoint_main);
            TextView detail_humidity_main = (TextView) convertView.findViewById(R.id.detail_humidity_main);
            TextView explain_humidity_main = (TextView) convertView.findViewById(R.id.explain_humidity_main);
            TextView detail_windspeed_main = (TextView) convertView.findViewById(R.id.detail_windspeed_main);
            TextView explain_windspeed_main = (TextView) convertView.findViewById(R.id.explain_windspeed_main);
            TextView detail_pressure_main = (TextView) convertView.findViewById(R.id.detail_pressure_main);
            TextView explain_pressure_main = (TextView) convertView.findViewById(R.id.explain_pressure_main);

            TextView detail_sunriseTime_main = (TextView) convertView.findViewById(R.id.detail_sunrise_main);
            TextView detail_sunsetTime_main = (TextView) convertView.findViewById(R.id.detail_sunset_main);
            TextView explain_sunriseTime_main = (TextView) convertView.findViewById(R.id.explain_sunrise_main);
            TextView explain_sunsetTime_main = (TextView) convertView.findViewById(R.id.explain_sunset_main);

            ImageView img_dewpoint_main = (ImageView) convertView.findViewById(R.id.img_dewpoint_main);
            ImageView img_humidity_main = (ImageView) convertView.findViewById(R.id.img_humidity_main);
            ImageView img_windspeed_main = (ImageView) convertView.findViewById(R.id.img_windspeed_main);
            ImageView img_pressure_main = (ImageView) convertView.findViewById(R.id.img_pressure_main);

            TextView explain_sunrise_main = (TextView) convertView.findViewById(R.id.explain_sunrise_main);
            TextView explain_sunset_main = (TextView) convertView.findViewById(R.id.explain_sunset_main);

            // =====================================
            if (language.contains("en")) {
                day_main.setText("Today / " + item.getTitle());
                detail_windspeed_main.setText(item.getWindspeed() + "M/second");
                dust_main.setText("Dust : " + listViewItem.getDust());
                dust_main.setText("DustValue : " + listViewItem.getDustValue());
                explain_dewpoint_main.setText(R.string.dewpoint);
                explain_windspeed_main.setText(R.string.windspeed);
                explain_pressure_main.setText(R.string.pressure);
                explain_humidity_main.setText(R.string.humidity);
                explain_humidity_main.setText(R.string.humidity);
                explain_sunrise_main.setText(R.string.sunrise);
                explain_sunset_main.setText(R.string.sunset);
            } else {
                day_main.setText("오늘 / " + item.getTitle() + "요일");
                detail_windspeed_main.setText(item.getWindspeed() + "M/초");
                Log.d("어디", "미세먼지 Todayadapter / " + listViewItem.getDust());
                if (listViewItem.getDust() == null | listViewItem.getDustValue() ==null){
                    listViewItem.setDust("지원되는 않는 위치입니다");
                    listViewItem.setDustValue("지원되는 않는 위치입니다");
                }
                dust_main.setText("미세먼지 : " + listViewItem.getDust());
                dust_value_main.setText(listViewItem.getDustValue() + " PM10");
            }

            Log.d("어디","===== todayAdapter ==== /"+item.getSunriseTime());
            Log.d("어디","===== todayAdapter ==== /"+item.getSunsetTime());
            Log.d("어디","===== todayAdapter ==== /"+item.getHumidity());
            detail_text.setText("DETAIL");
            status_main.setText(item.getStatus());
            date_main.setText(item.getDate());
            detail_sunriseTime_main.setText(item.getSunriseTime());
            detail_sunsetTime_main.setText(item.getSunsetTime());
            temperature_main.setText(item.getTemperature());
            detail_dewpoint_main.setText(item.getDewpoint() + "°");
            detail_humidity_main.setText(item.getHumidity() + "%");

            detail_pressure_main.setText(item.getPressure() + "hPa");

            if (item.getIcon().contains("rain"))
                icon_main.setImageResource(R.drawable.ic_weather_rain);
            else if (item.getIcon().contains("cloud"))
                icon_main.setImageResource(R.drawable.ic_weather_cloud);
            else
                icon_main.setImageResource(R.drawable.ic_weather_clear);

            detail_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_container_main.setVisibility(detail_container_main.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    if (down_arrow.getVisibility() == View.VISIBLE) {
                        down_arrow.setVisibility(View.GONE);
                        up_arrow.setVisibility(View.VISIBLE);
                    } else {
                        down_arrow.setVisibility(View.VISIBLE);
                        up_arrow.setVisibility(View.GONE);
                    }
                    Log.d("어디", "버튼클릭");
                }
            });
        }
        return convertView;
    }
}

