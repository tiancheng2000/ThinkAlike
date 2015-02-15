package com.aqiservice.android.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.Toast;

import com.aqiservice.android.service.MainWidgetService;
import com.thinkalike.generic.common.Util;

public class MainWidgetProvider extends AppWidgetProvider {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = MainWidgetProvider.class.getSimpleName();
    public final static String CLICK_ACTION = MainWidgetProvider.class.getName() + ".CLICK";
    public final static String REFRESH_ACTION = MainWidgetProvider.class.getName() + ".REFRESH";
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
    private boolean _firstLoad = true;
    
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
        // Update each of the widgets with the remote adapter
		if(_firstLoad){
	        for (int i = 0; i < appWidgetIds.length; ++i) {
	        	//IMPROVE: record layout size (large or not) for each instance of appWidget
	        	Bundle options = appWidgetManager.getAppWidgetOptions (appWidgetIds[i]);
	            int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
	            boolean largeLayout = isLargeLayout(minHeight);
	        	updateLayout(context, appWidgetIds[i], largeLayout);
	        }
	        _firstLoad = false;
		}
        
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
        if(intent.getAction().equals(CLICK_ACTION)){
            Toast.makeText(context, "ThinkAlike AQIService", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals(REFRESH_ACTION)){
    		final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    		final boolean largeLayout = intent.getBooleanExtra(MainWidgetService.EXTRA_LARGELAYOUT, true);
        	updateLayout(context, appWidgetId, largeLayout);
        }
        else  
            super.onReceive(context, intent);
    }

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

        int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        Util.trace(TAG, "onAppWidgetOptionsChanged: minHeight=" + minHeight);
        boolean largeLayout = isLargeLayout(minHeight);
        updateLayout(context, appWidgetId, largeLayout);

        //super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	//-- Public and internal Methods ---------------------------
    public static boolean isLargeLayout(int height){
    	return (height>=70)||(height<=0);
    }

	//-- Private and Protected Methods -------------------------
    private void updateLayout(Context context, int appWidgetId, boolean largeLayout) {
    	
		//Activate Service to update content or layout
		Intent intent = new Intent(context, MainWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.putExtra(MainWidgetService.EXTRA_LARGELAYOUT, largeLayout);
		
		if(_firstLoad){
			//Setup Alarm to automatically update content
			PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 
					PendingIntent.FLAG_UPDATE_CURRENT); //NOTE: override the previous unhandled PendingIntent
			Time time = new Time();
			time.setToNow();
			AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarm.setRepeating(AlarmManager.RTC, time.toMillis(true), 60*30*1000, pendingIntent);
		}
		else{
			context.startService(intent); //directly activate service for layout resize
		}
    }
    
	//-- Event Handlers ----------------------------------------
}

