package app.sunshine.android.example.com.sunshine;


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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import app.sunshine.android.example.com.sunshine.data.WeatherContract;
import app.sunshine.android.example.com.sunshine.sync.SunshineSyncAdapter;

public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] FORECAST_COLUMNS = {
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
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    public static final int COL_COORD_LAT = 7;
    public static final int COL_COORD_LONG = 8;

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private static final String POSITION = "mPosition";
    private final int DAYS = 7;
    private RecyclerView recyclerView;
    private ForecastAdapter forecastAdapter;
    private Callback callback;
    private int mPosition;
    private boolean twoPane;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        mPosition = savedInstanceState != null ? savedInstanceState.getInt(POSITION) : 0;
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    private void openMap() {
        if (forecastAdapter != null) {
            Cursor cursor = forecastAdapter.getCursor();
            if (cursor != null && cursor.moveToFirst()) {
                double posLat = cursor.getDouble(COL_COORD_LAT);
                double posLong = cursor.getDouble(COL_COORD_LONG);
                Uri geoLocation = Uri.parse(String.format("geo:%s,%s", posLat, posLong));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }
    }

    private void updateWeather() {
        SunshineSyncAdapter.syncImmediately(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        forecastAdapter = new ForecastAdapter(null, new ForecastAdapter.Listener() {
            @Override
            public void onItemClick(Cursor cursor, int position) {
                if (cursor != null) {
                    mPosition = position;
                    cursor.moveToPosition(position);
                    callback.onItemSelected(getUriFromCursor(cursor));
                }
            }
        });
        recyclerView.setAdapter(forecastAdapter);
        return rootView;
    }

    private Uri getUriFromCursor(Cursor cursor) {
        return WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                Utility.getPreferredLocation(getContext()), cursor.getLong(COL_WEATHER_DATE));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_refresh) {
//            updateWeather();
//        } else
        if (item.getItemId() == R.id.action_open_map) {
            openMap();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLocationChanged() {
        updateWeather();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }


    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String locationSetting = Utility.getPreferredLocation(getContext());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        forecastAdapter.setTwoPane(twoPane);
        forecastAdapter.swapCursor(cursor);
        if (twoPane && cursor.moveToFirst()) {
            callback.onItemSelected(getUriFromCursor(cursor));
        }
        recyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        forecastAdapter.swapCursor(null);
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }

    public interface Callback {

        void onItemSelected(Uri dateUri);
    }

}
