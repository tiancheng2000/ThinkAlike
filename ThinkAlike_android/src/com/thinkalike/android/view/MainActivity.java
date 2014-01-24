package com.thinkalike.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.thinkalike.R;
import com.thinkalike.android.ThinkAlikeApp;
import com.thinkalike.android.common.UncaughtExceptionHandler;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;

/**
 * This activity has different presentations for handset and tablet-size devices.
 * <p>
 * The activity makes heavy use of fragments. The list of resource is a
 * {@link NodeSelectorFragment} and the work area is a {@link WorkareaFragment}.
 * <p>
 * This activity also implements the required {@link NodeSelectorFragment.FragmentCallbacks}
 * interface to listen for resource drag&drop.
 */
public class MainActivity extends FragmentActivity implements
		NodeSelectorFragment.FragmentCallbacks {

	//-- Constants and Enums ----------------------------------------------
	//-- Inner Classes and Structures -------------------------------------
	//-- Delegates and Events ---------------------------------------------
	//-- Instance and Shared Fields ---------------------------------------
	protected UncaughtExceptionHandler _ueh = new UncaughtExceptionHandler(this);
	/**
	 * Whether or not the screen size is large enough to contain a two-pane mode activity. i.e. when running on a tablet device.
	 */
	private boolean _isLargeScreen;

	//-- Properties -------------------------------------------------------
	//-- Constructors -----------------------------------------------------
	//-- Destructors ------------------------------------------------------
	//-- Base Class Overrides ---------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreate", getClass().getSimpleName()));
		ThinkAlikeApp.getInstance().registerUIContext(this); //Application.getUIContext() will be used as uiContext by ViewModel in some case, which happens before onResume(). 
		super.onCreate(savedInstanceState);
		_ueh.initialize();
		setContentView(R.layout.activity_main);

		if (findViewById(R.id.ll_twopane) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			_isLargeScreen = true;
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	protected void onResume() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onResume", getClass().getSimpleName()));
		ThinkAlikeApp.getInstance().registerUIContext(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onDestroy", getClass().getSimpleName()));
		_ueh.restoreOriginalHandler();
		_ueh = null;
		ThinkAlikeApp.getInstance().unregisterUIContext(this);
		super.onDestroy();
	}

	//-- Public and internal Methods --------------------------------------
	//-- Private and Protected Methods ------------------------------------
	//-- Event Handlers ---------------------------------------------------
	/**
	 * Callback method from {@link NodeSelectorFragment.FragmentCallbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onNodeSelected(String id) {
		//TODO: FragmentManager，能设置“进出动画”(FragmentTransaction.setCustomAnimations())，可用于Canvas上的多页切换!
		//TODO: instead of Clicking operation, we should handle Drag&Drop.  
		if (_isLargeScreen) {
//			// In two-pane mode, show the detail view in this activity by
//			// adding or replacing the detail fragment using a
//			// fragment transaction.
//			Bundle arguments = new Bundle();
//			arguments.putString(CanvasFragment.ARG_ITEM_ID, id);
//			CanvasFragment fragment = new CanvasFragment();
//			fragment.setArguments(arguments);
//			getSupportFragmentManager().beginTransaction()
//					.replace(R.id.rl_canvas, fragment).commit();
		} else {
//			// In single-pane mode, simply start the detail activity
//			// for the selected item ID.
//			Intent detailIntent = new Intent(this, EditorActivity.class);
//			detailIntent.putExtra(CanvasFragment.ARG_ITEM_ID, id);
//			startActivity(detailIntent);     
		}
	}

}
