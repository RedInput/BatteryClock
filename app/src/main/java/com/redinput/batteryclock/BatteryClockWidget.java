package com.redinput.batteryclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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

        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), this.getClass().getName());
        int[] appWidgetIds = widgetManager.getAppWidgetIds(thisAppWidget);

        Bundle newOptions = widgetManager.getAppWidgetOptions(appWidgetIds[0]);
        float minWidth = Utils.dipToPixels(context, newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));
        float minHeight = Utils.dipToPixels(context, newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
        float maxWidth = Utils.dipToPixels(context, newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH));
        float maxHeight = Utils.dipToPixels(context, newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT));

        float width = (minWidth + maxWidth) / 2;
        float height = (minHeight + maxHeight) / 2;

        SharedPreferences batteryPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor batteryEdit = batteryPrefs.edit();

        batteryEdit.putFloat("width", width);
        batteryEdit.putFloat("height", height);
        batteryEdit.apply();

        context.startService(new Intent(context, UpdateService.class));
    }

    @Override
    public void onDeleted(Context context, int[] widgetIds) {
        super.onDeleted(context, widgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context, UpdateService.class));
    }
}
