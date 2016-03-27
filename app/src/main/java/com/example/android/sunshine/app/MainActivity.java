package com.example.android.sunshine.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            /*Create Dummy data to populate the list view */
            String[] forecastArray = {"Today - Sunny - 63/43", "Tomorrow - Rainy - 60/45", "Monday - Breezy - 49/60",
                                        "Tuesday - Warmer - 65/47", "Wednesday - Pleasant - 69/45", "Thursday - Mild - 64/45",
                                        "Friday - Partly Sunny - 59/37", "Saturday - Sunny - 65/50",
                    "Monday - Breezy - 49/60",
                    "Tuesday - Warmer - 65/47", "Wednesday - Pleasant - 69/45", "Thursday - Mild - 64/45",
                    "Friday - Partly Sunny - 59/37", "Saturday - Sunny - 65/50"};
            List<String> weekForecast = new ArrayList<String> (Arrays.asList(forecastArray));

            /*Initiate ArrayAdapter */
            ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

            /* Get a reference to the list view and attach it to the adapter */
            ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(forecastAdapter);


            return rootView;
        }
    }
}
