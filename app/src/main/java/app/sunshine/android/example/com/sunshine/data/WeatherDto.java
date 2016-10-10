package app.sunshine.android.example.com.sunshine.data;

import android.content.ContentValues;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 07/10/16.
 */

public class WeatherDto {

    private City city;
    private List<Info> list;

    public City getCity() {
        return city;
    }

    public List<Info> getList() {
        return list;
    }

    public static class City {
        private long id;
        private String name;
        private Coordinates coord;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Coordinates getCoord() {
            return coord;
        }

        public ContentValues getContentValues(String locationSetting) {
            ContentValues values = new ContentValues();
            values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, getName());
            values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, getCoord().getLat());
            values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, getCoord().getLon());
            return values;

        }

    }

    public static class Coordinates {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public static class Info {
        private String dt;
        private Temperature temp;
        private float pressure;
        private int humidity;
        private List<Weather> weather;
        private float speed;
        private int deg;

        public Temperature getTemp() {
            return temp;
        }

        public float getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public float getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }

    }

    public static class Temperature {
        private float min;
        private float max;

        public float getMin() {
            return min;
        }

        public float getMax() {
            return max;
        }
    }

    public static class Weather {
        private int id;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public List<ContentValues> getAsContentValues(long locationId) {
        List<ContentValues> contentValues = new ArrayList<>();
        Time dayTime = new Time();
        dayTime.setToNow();
        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        // now we work exclusively in UTC
        dayTime = new Time();
        List<Info> list1 = getList();
        for (int i = 0; i < list1.size(); i++) {
            Info info = list1.get(i);
            ContentValues weatherValues = new ContentValues();
            long dateTime = dayTime.setJulianDay(julianStartDay + i);
            String description = info.getWeather().get(0).getDescription();
            description = description.substring(0, 1).toUpperCase() + description.substring(1);

            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationId);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTime);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, info.getHumidity());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, info.getPressure());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, info.getSpeed());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, info.getDeg());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, info.getTemp().getMax());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, info.getTemp().getMin());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, description);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, info.getWeather().get(0).getId());
            contentValues.add(weatherValues);
        }
        return contentValues;
    }
}
