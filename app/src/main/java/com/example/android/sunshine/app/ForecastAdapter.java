package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.bumptech.glide.Glide;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private static final String LOG_TAG = CursorAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;
    private boolean mUseTodayLayout;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUseTodayLayout(boolean useTodayLayout){
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType){
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Get View Holder
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        //Weather Icon image resource id
        int weatherImageResourceId = -1;

        //Get View type based on cursor position
        int viewType = getItemViewType(cursor.getPosition());
        Log.i(LOG_TAG, "view type : " + viewType);
        int fallbackIconId;
        if(viewType == VIEW_TYPE_TODAY){
            //First row(Today row) - Get the Art image based on weather condition
            fallbackIconId = Utility.getArtResourceForWeatherCondition(weatherId);
        }
        else{
            //Not First row(Future row) - Get the icon image based on weather condition
            fallbackIconId = Utility.getIconResourceForWeatherCondition(weatherId);
        }
        //If weather icon/art image not found, use the launcher icon as place holder
        if(fallbackIconId == -1){
            fallbackIconId = R.drawable.ic_launcher;
        }

        //Read and bind weather forecast from cursor to view
        String forecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.forecastView.setText(forecast);

        //Bind the weather image to the view through Glide
        Glide.with(context)
                .load(Utility.getArtUrlForWeatherCondition(context, weatherId))
                .error(fallbackIconId)
                .crossFade()
                .into(viewHolder.iconView);
        viewHolder.iconView.setContentDescription(forecast);

        //Read and bind date from cursor to view
        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, date));


        //Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText(Utility.formatTemperature(context, high));
        viewHolder.highTempView.setContentDescription("High Temperature " + Utility.formatTemperature(context, high));

        //Read low temperature from cursor
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low));
        viewHolder.lowTempView.setContentDescription("Low Temperature " + Utility.formatTemperature(context, low));
    }

}