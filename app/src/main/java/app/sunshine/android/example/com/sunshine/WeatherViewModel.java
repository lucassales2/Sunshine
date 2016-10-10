package app.sunshine.android.example.com.sunshine;

import android.database.Cursor;

/**
 * Created by lucas on 05/10/16.
 */

public class WeatherViewModel {
    private long id;
    private long date;
    private String desc;
    private double max;
    private double min;
    private String locationSettings;
    private int conditionId;
    private double latitude;
    private double longitude;

    public WeatherViewModel(Cursor cursor) {
//        id = cursor.getLong(WeatherEntry.COL_WEATHER_ID);
//        date = cursor.getLong(WeatherEntry.COL_WEATHER_DATE);
//        desc = cursor.getString(WeatherEntry.COL_WEATHER_DESC);
//        max = cursor.getDouble(WeatherEntry.COL_WEATHER_MAX_TEMP);
//        min = cursor.getDouble(WeatherEntry.COL_WEATHER_MIN_TEMP);
//        locationSettings = cursor.getString(WeatherEntry.COL_LOCATION_SETTING);
//        conditionId = cursor.getInt(WeatherEntry.COL_WEATHER_CONDITION_ID);
//        latitude = cursor.getDouble(WeatherEntry.COL_COORD_LAT);
//        longitude = cursor.getDouble(WeatherEntry.COL_COORD_LONG);
    }
}
