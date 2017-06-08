package layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.przemyslaw.astroweather.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class SunFragmentF extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    TextView eastTime;
    TextView eastAzimuth;
    TextView westTime;
    TextView westAzimuth;
    TextView civilEvening;
    TextView civilMorning;

    Calendar calendar;
    int i = 0;

    public SunFragmentF() {
    }

    public static SunFragmentF newInstance(String param1, String param2, String param3) {
        SunFragmentF fragment = new SunFragmentF();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);//lat
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String s1 =(String) getArguments().getSerializable(ARG_PARAM1);
        final String s2 =(String) getArguments().getSerializable(ARG_PARAM2);
        final String s3 =(String) getArguments().getSerializable(ARG_PARAM3);

        View view = inflater.inflate(R.layout.fragment_sun2, container, false);
        eastTime = (TextView) view.findViewById(R.id.SunFargtextViewEastTime);
        eastAzimuth = (TextView) view.findViewById(R.id.SunFargtextViewEastAzimuth);
        civilEvening = (TextView) view.findViewById(R.id.SunFargtextViewCivilEvening);
        civilMorning = (TextView) view.findViewById(R.id.SunFargtextViewCivilDaylight);
        westTime = (TextView) view.findViewById(R.id.SunFargtextViewWestTime);
        westAzimuth = (TextView) view.findViewById(R.id.SunFargtextViewWestAzimuth);

        refreshData(s1, s2);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000 * Integer.parseInt(s3));
                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                refreshData(s1, s2);
                                                            }
                                                        }
                            );
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();


        return view;
    }

    public void refreshData(String s1, String s2) {
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(Double.parseDouble(s1), Double.parseDouble(s2));
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        TimeZone timeZone = TimeZone.getDefault();
        int offset = 2;
        AstroDateTime astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), offset, timeZone.useDaylightTime());

        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, astroLocation);
        NumberFormat formatter = new DecimalFormat("#0.00");

        AstroDateTime sunRise = astroCalculator.getSunInfo().getSunrise();
        eastTime.setText(sunRise.getHour() + " : " + sunRise.getMinute() + " : " + sunRise.getSecond());
        eastAzimuth.setText(formatter.format(astroCalculator.getSunInfo().getAzimuthRise()));

        AstroDateTime sunSet = astroCalculator.getSunInfo().getSunset();
        westTime.setText(sunSet.getHour() + " : " + sunSet.getMinute() + " : " + sunSet.getSecond());
        westAzimuth.setText(formatter.format(astroCalculator.getSunInfo().getAzimuthSet()));

        i += 1;
        westAzimuth.setText(String.valueOf(i));

        AstroDateTime sunTwilightEvening = astroCalculator.getSunInfo().getTwilightEvening();
        AstroDateTime sunTwilightMorning = astroCalculator.getSunInfo().getTwilightMorning();
        civilEvening.setText(sunTwilightEvening.getHour() + " : " + sunTwilightEvening.getMinute() + " : " + sunTwilightEvening.getSecond());
        civilMorning.setText(sunTwilightMorning.getHour() + " : " + sunTwilightMorning.getMinute() + " : " + sunTwilightMorning.getSecond());
    }

}
