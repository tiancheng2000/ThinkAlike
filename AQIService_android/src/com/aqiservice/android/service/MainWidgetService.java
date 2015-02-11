package com.aqiservice.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainWidgetService extends Service {
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	UpdateWidget(intent);
        //return START_STICKY; // We want this service to continue running until it is explicitly stopped
        return START_NOT_STICKY;  //this service will be started by alarm service
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
    
    
    //-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
    private void UpdateWidget(Intent intent) 
    {   
    	//TODO: communication between AppWidget's RemoteViews and ViewModel?
    	
    	//NOTE: It's possible the main UI is in small layout, in which case UI components are not complete.
    	
    	
    }

	//-- Event Handlers ----------------------------------------
}
