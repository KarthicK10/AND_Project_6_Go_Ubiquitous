package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String FORECASTFRAGMENT_TAG = "FFTAG";

    private String mLocation;

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "onStart Called");
        super.onStart();
    }


    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "onResume Called");
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager
        if(location != null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if(ff != null){
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy Called");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onStop Called");
        super.onStop();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "onPause Called");
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate Called");
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.i(LOG_TAG, "onCreateView Called");
        return super.onCreateView(parent, name, context, attrs);
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

        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if(id == R.id.action_map){
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String preferedLocation = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", preferedLocation).build();

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(geoLocation);
        if(mapIntent.resolveActivity(getPackageManager()) != null)
            startActivity(mapIntent);
        else
            Log.d(LOG_TAG, "Couldn't call " + preferedLocation + ", No map app found" );
    }
}
