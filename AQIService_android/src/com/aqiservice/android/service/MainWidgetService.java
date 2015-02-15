package com.aqiservice.android.service;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aqiservice.R;
import com.aqiservice.android.receiver.MainWidgetProvider;
import com.aqiservice.generic.common.Constant;
import com.aqiservice.generic.domain.AqiInfo;
import com.aqiservice.generic.viewmodel.AQIViewModel;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;

public class MainWidgetService extends Service {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = MainWidgetProvider.class.getSimpleName();
    public final static String EXTRA_LARGELAYOUT = "isLargeLayout";
	
	//-- Inner Classes and Structures --------------------------
    //-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//MVVM
	private AQIViewModel _vm_aqi = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM_aqi = null;
	private Set<Integer> _listenToVM_widgetIds = new HashSet<Integer>();

	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	refreshViewModel(intent);
    	//return START_STICKY;  // We want this service to continue running until it is explicitly stopped
    	return START_NOT_STICKY;  //this service will be started by alarm service
    }

	@Override
	public void onCreate() {
		super.onCreate();

		//1.ViewModel related: 1.IProperty event for UI Update 2.ICommand for dispatching UI Command to ViewModel
		final MainWidgetService thisInstance = this;
		if(_vm_aqi == null){
			_vm_aqi = AQIViewModel.getInstance();
			//listen to relative ViewModel
			_listenToVM_aqi = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					assert(event.getPropertyName().equals(Constant.PropertyName.AQIData));
					synchronized(_listenToVM_widgetIds){
						for(Integer widgetId : _listenToVM_widgetIds){
							//blank event. acquire substantial data directly from VM in the update routine.
							thisInstance.updateWidgetUI(widgetId.intValue());
						}
						_listenToVM_widgetIds.clear();
					}
				}
			};
			_vm_aqi.addPropertyChangeListener(Constant.PropertyName.AQIData, _listenToVM_aqi);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
    
    //-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
    private void refreshViewModel(Intent intent) 
    {
    	assert(_vm_aqi != null);
		final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID);
		//largeLayout will be ignored and retrieved from AppWidgetManager
		//final boolean largeLayout = intent.getBooleanExtra(MainWidgetService.EXTRA_LARGELAYOUT, true);

    	//update layout directly (e.g.resize), even if data not changed yet.
    	updateWidgetUI(appWidgetId);

    	//check if data changed. if changed, the UI will be updated again..
		synchronized(_listenToVM_widgetIds){
			_listenToVM_widgetIds.add(Integer.valueOf(appWidgetId));
		}
		_vm_aqi.onRefresh();

    }
	private String composeAqiDescript(int aqiRank){
		//IMPROVE: use Platform.getString(), with improved I18N solution!~
		int resid = 0;
		switch(aqiRank){
		case AqiInfo.AQI_RANK_EXCELLENT:
			resid = R.string.aqi_rank_excellent;
			break;
		case AqiInfo.AQI_RANK_FINE:
			resid = R.string.aqi_rank_fine;
			break;
		case AqiInfo.AQI_RANK_P_SLIGHT:
			resid = R.string.aqi_rank_p_slight;
			break;
		case AqiInfo.AQI_RANK_P_MODERATE:
			resid = R.string.aqi_rank_p_mid;
			break;
		case AqiInfo.AQI_RANK_P_SEVERE:
			resid = R.string.aqi_rank_p_severe;
			break;
		case AqiInfo.AQI_RANK_P_MOSTSEVERE:
			resid = R.string.aqi_rank_p_mostsevere;
			break;
		default:
			break;
		}
		return this.getResources().getString(resid);
	}

	//-- Event Handlers ----------------------------------------
    private void updateWidgetUI(int appWidgetId) 
    {
		AqiInfo aqiInfo = _vm_aqi.getAqiInfo();
		if(aqiInfo!=null){
			
	    	final Context context = this.getApplicationContext();
			final AppWidgetManager awm = AppWidgetManager.getInstance(context);

			Bundle options = awm.getAppWidgetOptions (appWidgetId);
			int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            boolean largeLayout = MainWidgetProvider.isLargeLayout(minHeight);
	        Util.trace(TAG, "updateWidgetUI: largeLayout=" + largeLayout);
		    int category = options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);
		    boolean keyguardLayout = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;
		    int layoutId = largeLayout ? R.layout.widget_main : (!keyguardLayout ? R.layout.widget_main_small : R.layout.widget_keyguard);
			
			//1.Create AppWidget's RemoteViews
	        RemoteViews rv = new RemoteViews(context.getPackageName(), layoutId);  

	        //2.Set content
	        rv.setTextViewText(R.id.l_area, aqiInfo.getAqiArea());
	        rv.setTextViewText(R.id.l_aqi_descript, composeAqiDescript(aqiInfo.getAqiRank()));
			//rct_aqi.setFill(Color.web(String.format("#%x", aqiInfo.getAqiColor())));
	        rv.setTextViewText(R.id.l_aqi, String.format("%d", aqiInfo.getAqiValue()));
	        rv.setTextViewText(R.id.l_pm2_5, String.format("%d", aqiInfo.getAqiPM2_5()));
	        rv.setTextViewText(R.id.l_pm2_5_24h, String.format("%d", aqiInfo.getAqiPM2_5_24h()));
	        rv.setTextViewText(R.id.l_date, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(aqiInfo.getAqiDate())); //TODO: UTC->LTC

			//3.Append callback event handlers (=PendingIntents)
	    	//NOTE: It's possible the main UI is in small layout, in which case some UI components are not available.
			Intent intentClick = new Intent(MainWidgetProvider.CLICK_ACTION);
	        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(context, 0, intentClick, 0);
	        rv.setOnClickPendingIntent(R.id.ll_root, pendingIntentClick);
	        
	        if(largeLayout){
	    		Intent intentRefresh = new Intent(MainWidgetProvider.REFRESH_ACTION);
	    		intentRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	    		intentRefresh.putExtra(MainWidgetService.EXTRA_LARGELAYOUT, largeLayout);
	            PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(context, 0, intentRefresh, 0);
	            rv.setOnClickPendingIntent(R.id.l_date, pendingIntentRefresh);
	        }
	        
	        //4.Update through AppWidgetManager
	        awm.updateAppWidget(appWidgetId, rv);
	        Toast.makeText(context, "AQI refreshed", Toast.LENGTH_SHORT).show();
		}
    }
}
