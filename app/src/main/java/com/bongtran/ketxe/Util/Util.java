package com.bongtran.ketxe.Util;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Shazam_ORG on 11/15/2017.
 */

public class Util {
    public static String covertDistance(long distance) {
        String dist = "";
        if (distance < 0) {
            return dist;
        }

        if (distance >= 10000) {
            double temp = distance / 1000.0000;

            if (distance < 100000) {
                dist = new DecimalFormat("###,###.00").format(temp) + " km";
            } else if (distance < 1000000) {
                dist = new DecimalFormat("###,###.0").format(temp) + " km";
            } else {
                dist = new DecimalFormat("###,###").format(temp) + " km";
            }
        } else {
            dist = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.US)).format(distance) + " m";
//            dist = new DecimalFormat("###,###").format(distance) + " m";
        }
        Log.d("dist","dist:"+dist);
        return dist;
    }
}
