package com.redinput.batteryclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceiver extends BroadcastReceiver {

     public static final String COVER_EVENT = "com.lge.android.intent.action.ACCESSORY_COVER_EVENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            context.startService(new Intent(context, UpdateService.class));
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
            context.startService(new Intent(context, UpdateService.class));

        } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            context.startService(new Intent(context, UpdateService.class));
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            context.startService(new Intent(context, UpdateService.class));

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            context.startService(new Intent(context, UpdateService.class));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            context.startService(new Intent(context, UpdateService.class));

        } else if (intent.getAction().equals(BatteryReceiver.COVER_EVENT)) {
            context.startService(new Intent(context, UpdateService.class));
        }
    }

}
