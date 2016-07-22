package com.example.yi.smartschedule.lib.triggers;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.yi.smartschedule.lib.Util;

/**
 * Created by jackphillips on 7/13/16.
 */
public class MyPhoneStateListener extends PhoneStateListener {
    public void onCallStateChanged(int state,String incomingNumber){
        switch(state){
            case TelephonyManager.CALL_STATE_IDLE:
                Util.d("IDLE");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Util.d("OFFHOOK");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Util.d("RINGING: " + incomingNumber);
                break;
        }
    }

}