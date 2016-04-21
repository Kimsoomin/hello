package kr.mintech.weather.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mintech.weather.R;
import kr.mintech.weather.beans.MyData;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder>
{
  private ArrayList<MyData> mDataset;

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mTextView;

    public ViewHolder(View view) {
      super(view);
      mImageView = (ImageView)view.findViewById(R.id.image);
      mTextView = (TextView)view.findViewById(R.id.textview);
    }
  }

  public CardViewAdapter(ArrayList<MyData> myDataset) {
    mDataset = myDataset;
  }

  @Override
  public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.my_view, parent, false);
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    holder.mTextView.setText(mDataset.get(position).text);
    holder.mImageView.setImageResource(mDataset.get(position).img);
  }

  @Override
  public int getItemCount() {
    return mDataset.size();
  }
}

