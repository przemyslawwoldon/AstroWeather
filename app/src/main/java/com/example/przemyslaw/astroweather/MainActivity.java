package com.example.przemyslaw.astroweather;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

import layout.MoonFragment;
import layout.MoonFragmentF;
import layout.SunFragment;
import layout.SunFragmentF;

public class MainActivity extends AppCompatActivity{
    private TextView actualTime;
    private TextView degreeLatitude;
    private TextView degreeLongitude;
    private TextView minuteLatitude;
    private TextView minuteLongitude;

    private Calendar calendar;
    private int hour;
    private int minute;
    private int second;

    private static int degreeLong;
    private static int degreeLat;
    private static int minuteLong;
    private static int minuteLat;
    private static int timeRefr = 15;
    private static StringBuffer sb = new StringBuffer();

    public final static String M_TIME_REFRESH = "Main time refresh";
    public final static String M_DEGREE_LONG = "Main Degree longitude";
    public final static String M_DEGREE_LAT = "Main degree latitude";
    public final static String M_MINUTE_LONG = "Main minute longitude";
    public final static String M_MINUTE_LAT = "Main minute latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                setContentView(R.layout.activity_main);
                ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setContentView(R.layout.activity_main_landscape);
                double latitudeTemp = calculateLaitude();
                double longitudeTemp = calculateLongitude();
                android.app.FragmentManager manager = getFragmentManager();
                SunFragmentF sunFragmentF = SunFragmentF.newInstance(String.valueOf(latitudeTemp), String.valueOf(longitudeTemp), String.valueOf(timeRefr));
                MoonFragmentF moonFragmentF = MoonFragmentF.newInstance(String.valueOf(latitudeTemp), String.valueOf(longitudeTemp), String.valueOf(timeRefr));
                android.app.FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment1, sunFragmentF);
                fragmentTransaction.replace(R.id.fragment2, moonFragmentF);
                fragmentTransaction.commit();
                //threadFrag.start();
                break;
        }

        init();
        startClock();

        Intent intent = getIntent();
        String tR = intent.getStringExtra(SettingsActivity.TIME_REFRESH);
        String dLong = intent.getStringExtra(SettingsActivity.DEGREE_LONG);
        String dLat = intent.getStringExtra(SettingsActivity.DEGREE_LAT);
        String mLong = intent.getStringExtra(SettingsActivity.MINUTE_LONG);
        String mLat = intent.getStringExtra(SettingsActivity.MINUTE_LAT);
        if((tR != null) && (dLong != null) && (dLat != null) && (mLong != null) && (mLat != null)) {
            degreeLatitude.setText(dLat);
            degreeLongitude.setText(dLong);
            minuteLatitude.setText(mLat);
            minuteLongitude.setText(mLong);
            timeRefr = Integer.parseInt(tR);
        }
        setValues();

    }

//przbudowa main ma 3 fragmenty z pager
    //1  tak jak main
    //2
    //3 pogoda


    public static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {
                default:
                    double latitudeTemp = calculateLaitude();
                    double longitudeTemp = calculateLongitude();
                    //BasicData
                    return SunFragment.newInstance(String.valueOf(latitudeTemp), String.valueOf(longitudeTemp), String.valueOf(timeRefr));
                case 0:
                    double latitudeTemp1 = calculateLaitude();
                    double longitudeTemp1 = calculateLongitude();
                    //BasicData
                    return SunFragment.newInstance(String.valueOf(latitudeTemp1), String.valueOf(longitudeTemp1), String.valueOf(timeRefr));
                case 1:
                    double latitudeTemp2 = calculateLaitude();
                    double longitudeTemp2 = calculateLongitude();
                    //AdditionalData
                    return MoonFragment.newInstance(String.valueOf(latitudeTemp2), String.valueOf(longitudeTemp2), String.valueOf(timeRefr));
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    public void init(){
        actualTime = (TextView) findViewById(R.id.textViewActualTime);
        degreeLatitude = (TextView) findViewById(R.id.textViewDegreeLatitude);
        degreeLongitude = (TextView) findViewById(R.id.textViewDegreeLongitude);
        minuteLatitude = (TextView) findViewById(R.id.textViewMinuteLatitude);
        minuteLongitude = (TextView) findViewById(R.id.textViewMinuteLongitude);
    }

    public void setValues() {
        degreeLong = Integer.parseInt(degreeLongitude.getText().toString());
        degreeLat = Integer.parseInt(degreeLatitude.getText().toString());
        minuteLong = Integer.parseInt(minuteLongitude.getText().toString());
        minuteLat = Integer.parseInt(minuteLatitude.getText().toString());
    }

    public void startClock(){
        Thread timeTread = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                calendar = Calendar.getInstance();
                                hour = calendar.get(Calendar.HOUR_OF_DAY);
                                minute = calendar.get(Calendar.MINUTE);
                                second = calendar.get(Calendar.SECOND);
                                actualTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second));
                            }
                        });
                        Thread.sleep(1000);
                    }
                }catch(InterruptedException e){}
            }
        };
        timeTread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu); //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                settingsIntent.putExtra(M_TIME_REFRESH, String.valueOf(timeRefr));
                settingsIntent.putExtra(M_DEGREE_LONG, String.valueOf(degreeLong));
                settingsIntent.putExtra(M_DEGREE_LAT, String.valueOf(degreeLat));
                settingsIntent.putExtra(M_MINUTE_LONG, String.valueOf(minuteLong));
                settingsIntent.putExtra(M_MINUTE_LAT, String.valueOf(minuteLat));
                MainActivity.this.startActivity(settingsIntent);
                return true;
            case R.id.refresh:
                return true;
            case R.id.setUnit:
                Intent unitIntent = new Intent(MainActivity.this, SettingsActivity.class);
                unitIntent.putExtra(M_TIME_REFRESH, String.valueOf(timeRefr));
                unitIntent.putExtra(M_DEGREE_LONG, String.valueOf(degreeLong));
                unitIntent.putExtra(M_DEGREE_LAT, String.valueOf(degreeLat));
                unitIntent.putExtra(M_MINUTE_LONG, String.valueOf(minuteLong));
                unitIntent.putExtra(M_MINUTE_LAT, String.valueOf(minuteLat));
                MainActivity.this.startActivity(unitIntent);
                return true;
            default:
                return false;
        }
    }

    public static double calculateLaitude() {
        sb.delete(0, sb.length());
        sb.append(degreeLat);
        sb.append(".");
        sb.append(minuteLat);
        double latitude = Double.parseDouble(sb.toString());
        return latitude;
    }

    public static double calculateLongitude() {
        sb.delete(0, sb.length());
        sb.append(degreeLong);
        sb.append(".");
        sb.append(minuteLong);
        double longitude = Double.parseDouble(sb.toString());
        return longitude;
    }

}
