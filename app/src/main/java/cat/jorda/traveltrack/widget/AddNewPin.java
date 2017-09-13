package cat.jorda.traveltrack.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.dayDetails.DayDetailsActivity;
import cat.jorda.traveltrack.util.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class AddNewPin extends AppWidgetProvider
{
    private static String TAG = AddNewPin.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        updateWidgetView(context,
                appWidgetManager,
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AddNewPin.class)), null);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        if(manager != null)
        {
            updateWidgetView(context,
                    manager,
                    AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AddNewPin.class)), intent.getBundleExtra(Constants.TRIP_INFO));
        }
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager,
                                  int[] appWidgetIds, Bundle b)
    {
        for (int widgetId : appWidgetIds)
        {
            RemoteViews remoteViews = initViews(context, widgetId);

            if (b == null)
                break;

            Intent intent = new Intent(context, DayDetailsActivity.class);
            intent.putExtra(Constants.TRIP_INFO, b);

            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id.appwidget_layout, configPendingIntent);

            remoteViews.setTextViewText(R.id.appwidget_trip_title, b.getString(Constants.TRIP_NAME));
            remoteViews.setTextViewText(R.id.appwidget_day_title, b.getString(Constants.DAY_NAME));
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context, int widgetId)
    {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.add_new_pin);

        Intent intent = new Intent(context, DayDetailsActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        return remoteViews;
    }

    @Override
    public void onEnabled(Context context)
    {
        Log.d(TAG, "onEnabled");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        Log.d(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }
}

