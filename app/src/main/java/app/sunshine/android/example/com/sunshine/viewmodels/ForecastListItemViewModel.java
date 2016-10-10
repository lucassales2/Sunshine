package app.sunshine.android.example.com.sunshine.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;

import app.sunshine.android.example.com.sunshine.ForecastFragment;
import app.sunshine.android.example.com.sunshine.Utility;

/**
 * Created by lucas on 09/10/16.
 */

public class ForecastListItemViewModel extends BaseObservable {
    private String day;
    private String desc;
    private String max;
    private String min;
    private int weatherCondition;

    public ForecastListItemViewModel(Cursor cursor, Context context) {
        update(cursor, context);
    }

    public String getMax() {
        return max;
    }

    public String getDay() {
        return day;
    }

    public String getDesc() {
        return desc;
    }

    public String getMin() {
        return min;
    }

    public int getWeatherCondition() {
        return weatherCondition;
    }

    public void update(Cursor cursor, Context context) {
        day = Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE));
        desc = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        max = Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP));
        min = Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
        weatherCondition = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        notifyChange();
    }
}
