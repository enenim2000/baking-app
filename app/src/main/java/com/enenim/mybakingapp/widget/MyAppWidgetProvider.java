package com.enenim.mybakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.enenim.mybakingapp.MainActivity;
import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.RecipeStepDescriptionHostActivity;

public class MyAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_title_label, titlePendingIntent);

            Intent intent = new Intent(context, WidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widget_list_view, intent);

            Intent clickIntentTemplate = new Intent(context, RecipeStepDescriptionHostActivity.class);
            PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(context, 0, clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, MyAppWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_list_view);
        }
        super.onReceive(context, intent);
    }
}