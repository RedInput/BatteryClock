package com.redinput.batteryclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class BatteryClockWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(context, UpdateService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds) {
        super.onUpdate(context, widgetManager, widgetIds);

        Bundle newOptions = widgetManager.getAppWidgetOptions(widgetIds[0]);
        float minWidth = Utils.dpToPx(newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));
        float minHeight = Utils.dpToPx(newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
        float maxWidth = Utils.dpToPx(newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH));
        float maxHeight = Utils.dpToPx(newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT));

        float width = (minWidth + maxWidth) / 2;
        float height = (minHeight + maxHeight) / 2;

        int size = (int) width;
        if (height < width) {
            size = (int) height;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editPrefs = prefs.edit();

        editPrefs.putInt("size", size);
        editPrefs.apply();

        context.startService(new Intent(context, UpdateService.class));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context, UpdateService.class));
    }
}
