package com.example.android.sunshine.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KarthicK on 7/20/2016.
 */
public class ViewHolder {

    public final ImageView iconView;
    public final TextView dateView;
    public final TextView forecastView;
    public final TextView highTempView;
    public final TextView lowTempView;

    public ViewHolder(View view) {
        this.iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        this.dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        this.forecastView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        this.highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
        this.lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
    }
}
