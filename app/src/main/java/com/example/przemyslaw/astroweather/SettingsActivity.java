package com.example.przemyslaw.astroweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private Button commit;
    private EditText degreeLatitude;
    private EditText degreeLongitude;
    private EditText minuteLatitude;
    private EditText minuteLongitude;
    private EditText timeRefresh;

    private int degreeLat;
    private int degreeLong;
    private int minuteLat;
    private int minuteLong;
    private int timeRefr;

    public final static String TIME_REFRESH = "Settings time refresh";
    public final static String DEGREE_LONG = "Settings degree longitude";
    public final static String DEGREE_LAT = "Settings degree latitude";
    public final static String MINUTE_LONG = "Settings minute longitude";
    public final static String MINUTE_LAT = "Settings minute latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                setContentView(R.layout.activity_settings);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setContentView(R.layout.activity_settings_landscape);
                break;
        }

        init();

        Intent intent = getIntent();
        String tR = intent.getStringExtra(MainActivity.M_TIME_REFRESH);
        String dLong = intent.getStringExtra(MainActivity.M_DEGREE_LONG);
        String dLat = intent.getStringExtra(MainActivity.M_DEGREE_LAT);
        String mLong = intent.getStringExtra(MainActivity.M_MINUTE_LONG);
        String mLat = intent.getStringExtra(MainActivity.M_MINUTE_LAT);
        if((tR != null) && (dLong != null) && (dLat != null) && (mLong != null) && (mLat != null)) {
            degreeLatitude.setText(dLat);
            degreeLongitude.setText(dLong);
            minuteLatitude.setText(mLat);
            minuteLongitude.setText(mLong);
            timeRefresh.setText(tR);
            timeRefr = Integer.parseInt(tR);
            degreeLat = Integer.parseInt(dLat);
            degreeLong = Integer.parseInt(dLong);
            minuteLat = Integer.parseInt(mLat);
            minuteLong = Integer.parseInt(mLong);
         }

        commit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkDegree() && checkMinute() && checkRefresh()) {
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    intent.putExtra(TIME_REFRESH, String.valueOf(timeRefr));
                    intent.putExtra(DEGREE_LONG, String.valueOf(degreeLong));
                    intent.putExtra(DEGREE_LAT, String.valueOf(degreeLat));
                    intent.putExtra(MINUTE_LONG, String.valueOf(minuteLong));
                    intent.putExtra(MINUTE_LAT, String.valueOf(minuteLat));
                    SettingsActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(SettingsActivity.this, "Niepoprawne dane", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkDegree(){
        String str1 = degreeLatitude.getText().toString();
        String str2 = degreeLongitude.getText().toString();
        try {
            degreeLat = Integer.parseInt(str1);
            degreeLong = Integer.parseInt(str2);
            if(Math.abs(degreeLat) < 0 || Math.abs(degreeLat) > 90)
                return false;
            else if(Math.abs(degreeLong) < 0 || Math.abs(degreeLong) > 180)
                return false;
            else
                return true;
        }catch(NumberFormatException n){
            return false;
        }
    }

    public boolean checkMinute(){
        String str1 = minuteLatitude.getText().toString();
        String str2 = minuteLongitude.getText().toString();
        try {
            minuteLat = Integer.parseInt(str1);
            minuteLong = Integer.parseInt(str2);
            if(Math.abs(minuteLat) < 0 || Math.abs(minuteLat) > 60)
                return false;
            else if(Math.abs(minuteLong) < 0 || Math.abs(minuteLong) > 60)
                return false;
            else
                return true;
        }catch(NumberFormatException n){
            return false;
        }
    }

    public boolean checkRefresh(){
        String str1 = timeRefresh.getText().toString();
        try {
            timeRefr = Integer.parseInt(str1);
            if(timeRefr < 1 || timeRefr > 60)
                return false;
            else
                return true;
        }catch(NumberFormatException n){
            return false;
        }
    }

    public void init(){
        commit = (Button) findViewById(R.id.buttonCommit);
        degreeLatitude = (EditText) findViewById(R.id.editTextDegreeLatitude);
        degreeLongitude = (EditText) findViewById(R.id.editTextDegreeLongitude);
        minuteLatitude = (EditText) findViewById(R.id.editTextMinuteLatitude);
        minuteLongitude = (EditText) findViewById(R.id.editTextMinuteLongitude);
        timeRefresh = (EditText) findViewById(R.id.editTextRefresh);
    }

}
