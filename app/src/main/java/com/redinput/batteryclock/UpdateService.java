package com.redinput.batteryclock;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class UpdateService extends Service {

    private BatteryReceiver mBatteryReceiver = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mBatteryReceiver == null) {
            registerNewReceiver();
        }

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(new ComponentName(this, BatteryClockWidget.class), getWidgetRemoteView());

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatteryReceiver);
    }

    private void registerNewReceiver() {
        mBatteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        intentFilter.addAction(BatteryReceiver.COVER_EVENT);

        registerReceiver(mBatteryReceiver, intentFilter);
    }

    private RemoteViews getWidgetRemoteView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = 0;
        int plugged = 0;
        int scale = 100;
        if (batteryIntent != null) {
            level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            plugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
        }

        float batteryPct = level / (float) scale;

        int size = prefs.getInt("size", 500);
        float strokeWidth = Utils.dpToPx(4);

        if (size == 0) {
            size = 500;
        }

        int color;
        if (plugged > 0) {
            color = Color.GREEN;
        } else {
            if (batteryPct < 0.15) {
                color = Color.RED;
                strokeWidth *= 3;

            } else if (batteryPct < 0.3) {
                color = Color.YELLOW;
                strokeWidth *= 2;

            } else {
                color = Color.BLUE;
            }
        }

        float radius = (size - strokeWidth) / 2;
        float center = size / 2;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);

        Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        RectF oval = new RectF();
        oval.set(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 270 - (batteryPct * 360), batteryPct * 360, false, paint);

        RemoteViews widgetView = new RemoteViews(this.getPackageName(), R.layout.widget);
        widgetView.setImageViewBitmap(R.id.imgBattery, bmp);

        return widgetView;
    }

}
