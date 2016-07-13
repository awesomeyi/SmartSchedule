package com.example.yi.smartschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.yi.smartschedule.lib.Util;

/**
 * Created by jackphillips on 7/13/16.
 */
public class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        final TelephonyManager mtelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        mtelephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        // CALL_STATE_RINGING
                        Util.d("I'm in " + state + " and the number is " + incomingNumber);
                        if(incomingNumber.equalsIgnoreCase("9143309136")){

                            unSilencePhone(context);
                        }
                        break;
                    default:
                        break;
                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }
    public void silencePhone(Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Util.d("Phone silenced");
    }
    public void unSilencePhone(Context context){
        AudioManager audio =  (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Util.d("Phone sound Normal");
    }

}
