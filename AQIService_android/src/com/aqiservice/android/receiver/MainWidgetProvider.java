package com.aqiservice.android.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aqiservice.R;

public class MainWidgetProvider extends AppWidgetProvider {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = MainWidgetProvider.class.getSimpleName();
    public static String CLICK_ACTION = MainWidgetProvider.class.getName() + ".CLICK";
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
        // Update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
        	//IMPROVE: record layout size (large or not) for each instance of appWidget
        	RemoteViews layout = buildLayout(context, appWidgetIds[i], true);
        	appWidgetManager.updateAppWidget(appWidgetIds[i], layout);
        }
        
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
        if(intent.getAction().equals(CLICK_ACTION)){
            Toast.makeText(context, "点击了widget日历", Toast.LENGTH_SHORT).show();
        }  
    }

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

        int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        boolean isLargeLayout = (minHeight>100)||(minHeight<=0);
        RemoteViews layout = buildLayout(context, appWidgetId, isLargeLayout);
        appWidgetManager.updateAppWidget(appWidgetId, layout);

        //super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
    private RemoteViews buildLayout(Context context, int appWidgetId, boolean largeLayout) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), 
        		largeLayout ? R.layout.widget_main : R.layout.widget_main_small);  
		Intent intent=new Intent(CLICK_ACTION);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
        return remoteViews;
    }

	//-- Event Handlers ----------------------------------------
}
