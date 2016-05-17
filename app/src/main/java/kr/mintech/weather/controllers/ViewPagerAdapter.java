package kr.mintech.weather.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import kr.mintech.weather.fragments.TodayFragment;

/**
 * Created by Mac on 16. 5. 17..
 */
public class ViewPagerAdapter extends FragmentPagerAdapter
{
  private Context context;

  private ArrayList<Fragment> fragments = new ArrayList<>();
  private ArrayList<String> titles = new ArrayList<>();

  public ViewPagerAdapter(FragmentManager fm)
  {
    super(fm);
    addFragment(new TodayFragment(), "Today");
//    addFragment(new WeekFragment(), "Week");
    notifyDataSetChanged();
  }

  @Override
  public Fragment getItem(int position)
  {
    // 해당하는 page의 Fragment를 생성합니다.
    return fragments.get(position);
  }

  @Override
  public int getCount()
  {
    return titles.size();  // 총 5개의 page를 보여줍니다.
  }

  public void addFragment(Fragment fragment, String title)
  {
    fragments.add(fragment);
    titles.add(title);
  }

  @Override
  public CharSequence getPageTitle(int position)
  {
    return titles.get(position);
  }
}
