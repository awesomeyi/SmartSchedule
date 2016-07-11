package com.example.yi.smartschedule.lib;

import android.app.Fragment;
import android.util.Log;

/**
 * Created by Yi on 7/11/16.
 */
public class Util {
    public static void d(String message) {
        Log.d(Constants.DEBUG, message);
    }
    public static int pixel_to_dp(Fragment f, int px) {
        final float scale = f.getActivity().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }
}
