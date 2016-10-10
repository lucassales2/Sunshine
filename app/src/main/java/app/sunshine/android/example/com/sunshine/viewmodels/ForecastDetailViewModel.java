package app.sunshine.android.example.com.sunshine.viewmodels;

import android.content.Context;
import android.database.Cursor;

import app.sunshine.android.example.com.sunshine.R;
import app.sunshine.android.example.com.sunshine.Utility;

import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_CONDITION_ID;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_DATE;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_DEGREES;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_DESC;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_HUMIDITY;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_MAX_TEMP;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_MIN_TEMP;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_PRESSURE;
import static app.sunshine.android.example.com.sunshine.DetailFragment.COL_WEATHER_WIND_SPEED;

/**
 * Created by lucas on 09/10/16.
 */

public class ForecastDetailViewModel {

    private final String friendlyDayString;
    private final String formattedMonthDay;
    private final String desc;
    private final String max;
    private final String min;
    private final String humidity;
    private final float degrees;
    private final String wind;
    private final String pressure;
    private final int weatherCondition;

    public ForecastDetailViewModel(Cursor cursor, Context context) {
        long date = cursor.getLong(COL_WEATHER_DATE);
        friendlyDayString = Utility.getFriendlyDayString(context, date);
        formattedMonthDay = Utility.getFormattedMonthDay(context, date);
        desc = cursor.getString(COL_WEATHER_DESC);
        max = Utility.formatTemperature(context, cursor.getDouble(COL_WEATHER_MAX_TEMP));
        min = Utility.formatTemperature(context, cursor.getDouble(COL_WEATHER_MIN_TEMP));
        humidity = context.getString(R.string.format_humidity, cursor.getDouble(COL_WEATHER_HUMIDITY));
        degrees = cursor.getFloat(COL_WEATHER_DEGREES);
        wind = Utility.getFormattedWind(context, cursor.getFloat(COL_WEATHER_WIND_SPEED), degrees);
        pressure = context.getString(R.string.format_pressure, cursor.getDouble(COL_WEATHER_PRESSURE));
        weatherCondition = cursor.getInt(COL_WEATHER_CONDITION_ID);
    }

    public String getFriendlyDayString() {
        return friendlyDayString;
    }

    public String getFormattedMonthDay() {
        return formattedMonthDay;
    }

    public String getDesc() {
        return desc;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }

    public String getHumidity() {
        return humidity;
    }

    public float getDegrees() {
        return degrees;
    }

    public String getWind() {
        return wind;
    }

    public String getPressure() {
        return pressure;
    }

    public int getWeatherCondition() {
        return weatherCondition;
    }
}
