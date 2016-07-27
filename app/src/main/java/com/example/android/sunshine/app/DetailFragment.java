package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by KarthicK on 7/23/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private static final int DETAIL_LOADER = 0;
    private ShareActionProvider mShareActionProvider;
    private String mForecastStr;
    private Uri mUri;

    private static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMIDITY = 5;
    static final int COL_WEATHER_WIND_SPEED = 6;
    static final int COL_WEATHER_DEGREES = 7;
    static final int COL_WEATHER_PRESSURE = 8;
    static final int COL_WEATHER_CONDITION_ID = 9;


    private TextView mFriendlyDayTextView;
    private TextView mMonthDayTextView;
    private TextView mHighTempTextView;
    private TextView mLowTempTextView;
    private TextView mHumidityTextView;
    private TextView mWindTextView;
    private TextView mPressureTextView;
    private TextView mForecastTextView;
    private ImageView mIconView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader onCreateLoader(int id, Bundle args) {

       if(null != mUri) {
           // Now create and return a CursorLoader that will take care of
           // creating a Cursor for the data being displayed.
           return new CursorLoader(
                   getActivity(),
                   mUri,
                   DETAIL_COLUMNS,
                   null,
                   null,
                   null
           );
       }
       return  null;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link# FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link android.content.CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param cursor   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor == null || !cursor.moveToFirst()){
            return;
        }

            /* Get values from cursor and format if necessary */
        int weatherConditionId = cursor.getInt(COL_WEATHER_CONDITION_ID);
        String dateString = Utility.formatDate(cursor.getLong(COL_WEATHER_DATE));
        String friendlyDay = Utility.getFriendlyDayString(getContext(), cursor.getLong(COL_WEATHER_DATE));
        String monthDay = Utility.getFormattedMonthDay(getContext(), cursor.getLong(COL_WEATHER_DATE));
        String forecast = cursor.getString(COL_WEATHER_DESC);
        boolean isMetric = Utility.isMetric(getActivity());
        String high = Utility.formatTemperature(getContext(), cursor.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(getContext(), cursor.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
        String humidity = getActivity().getString(R.string.format_humidity, cursor.getDouble(COL_WEATHER_HUMIDITY));
        String wind = Utility.getFormattedWind( getContext(), cursor.getFloat(COL_WEATHER_WIND_SPEED), cursor.getFloat(COL_WEATHER_DEGREES) );
        String pressure = getActivity().getString(R.string.format_pressure, cursor.getDouble(COL_WEATHER_PRESSURE));

        /*Bind values to views */
        mFriendlyDayTextView.setText(friendlyDay);
        mMonthDayTextView.setText(monthDay);
        mHighTempTextView.setText(high);
        mLowTempTextView.setText(low);
        mHumidityTextView.setText(humidity);
        mWindTextView.setText(wind);
        mPressureTextView.setText(pressure);
        mForecastTextView.setText(forecast);
        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherConditionId));
        /*End Bind values to views */

        //We will need this for share intent
        mForecastStr = String.format("%s - %s - %s/%s", dateString, forecast, high, low);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the Uri passed on from main activity through detail activity
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mFriendlyDayTextView = (TextView) rootView.findViewById(R.id.detail_friendly_day_textview);
        mMonthDayTextView = (TextView) rootView.findViewById(R.id.detail_month_day_textview);
        mHighTempTextView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mLowTempTextView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityTextView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindTextView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureTextView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);
        mForecastTextView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        return  rootView;
    }

    public void onLocationChanged (String newLocation){
        //replace the URI since the location has changed
        Uri uri = mUri;
        if(uri != null){
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link ##Activity # onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate the menu. This adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        //Retrive the share menu item.
        MenuItem menuItem = menu.findItem(R.id.action_share);

        //Get the provider and set/change the intent
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if(mForecastStr != null){
            //Attache the intent to the share action provider.
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }


    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }


}
