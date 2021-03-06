package app.sunshine.android.example.com.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DetailFragment mainFragment = DetailFragment.newInstance(getIntent().getData());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weather_detail_container, mainFragment)
                .commit();
    }

}
