package com.example.lenovo.hkgorprivateapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<DataModel> data = Collections.emptyList();
    private LayoutInflater inflater;
    private static GridViewClickListener itemListener;
    private GridView mGv;
    DisplayMetrics mMetrics;

    public GridViewAdapter(DisplayMetrics metrics, GridView gridView, Context context, ArrayList<DataModel> data, GridViewClickListener itemListener) {
        this.context = context;
        this.mGv = gridView;
        this.data = data;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        mMetrics = metrics;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView img;
        ImageView playPause;
        CardView itemParent;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

        rowView = inflater.inflate(R.layout.item, null);
        rowView.setMinimumHeight((int) (height/3.6));

        rowView.setTag(getItemId(position));
        holder.img = (ImageView) rowView.findViewById(R.id.image_view);
        holder.img.setImageBitmap(data.get(position).getMyBitmap());
        holder.playPause = (ImageView) rowView.findViewById(R.id.iv_play_pause);
        holder.itemParent = (CardView) rowView.findViewById(R.id.card_view);




        // String assetPath = "file:///assets/equalizer.gif";
        //   Glide.with(context).load(assetPath).asGif().into(holder.img);

        holder.img.setImageBitmap(data.get(position).getMyBitmap());

        //   holder.playPause.setSelected(data.get(position).isChecked());


        holder.itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.gdItemClickListener(holder.itemParent, position);

            }
        });

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//        rowView.setLayoutParams(new GridView.LayoutParams(params));

//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                mGv.getHeight() / 5);
//        rowView.setLayoutParams(param);
        return rowView;
    }
}
