/**
* Copyright 2013-2014 Tiancheng Hu
* 
* Licensed under the GNU Lesser General Public License, version 3.0 (LGPL-3.0, the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://opensource.org/licenses/lgpl-3.0.html
*     
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.aqiservice.android.view;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aqiservice.R;
import com.aqiservice.android.AQIServiceApp;
import com.aqiservice.generic.common.Constant;
import com.aqiservice.generic.domain.AqiInfo;
import com.aqiservice.generic.viewmodel.AQIViewModel;
import com.thinkalike.android.common.UncaughtExceptionHandler;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;

/**
 * This activity has different presentations for handset and tablet-size devices.
 * <p>
 * The activity makes heavy use of fragments. The list of resource is a
 * {@link NodeSelectorFragment} and the work area is a {@link WorkareaFragment}.
 * <p>
 * This activity also implements the required {@link NodeSelectorFragment.FragmentCallbacks}
 * interface to listen for resource drag&drop.
 */
public class MainActivity extends Activity {

	//-- Constants and Enums ----------------------------------------------
	//-- Inner Classes and Structures -------------------------------------
	//-- Delegates and Events ---------------------------------------------
	//-- Instance and Shared Fields ---------------------------------------
	protected UncaughtExceptionHandler _ueh = new UncaughtExceptionHandler(this, Constant.APP_SHORTNAME, Constant.UNCAUGHTEXCEPTION_RECEIVER_MAIL);
	//MVVM
	private AQIViewModel _vm_aqi = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM_aqi = null; 
	private TextView _l_area;
	private TextView _l_aqi_descript;
	//private Rectangle _rct_aqi;
	private TextView _l_aqi;
	private TextView _l_pm2_5;
	private TextView _l_pm2_5_24h;
	private TextView _l_date;

	//-- Properties -------------------------------------------------------
	//-- Constructors -----------------------------------------------------
	//-- Destructors ------------------------------------------------------
	//-- Base Class Overrides ---------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreate", getClass().getSimpleName()));
		AQIServiceApp.getInstance().registerUIContext(this); //Application.getUIContext() will be used as uiContext by ViewModel in some case, which happens before onResume(). 
		super.onCreate(savedInstanceState);
		_ueh.initialize();
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		//0.UI Controls initialization
		_l_area = (TextView)findViewById(R.id.l_area);
		_l_aqi_descript = (TextView)findViewById(R.id.l_aqi_descript);
		//_rct_aqi;
		_l_aqi = (TextView)findViewById(R.id.l_aqi);
		_l_pm2_5 = (TextView)findViewById(R.id.l_pm2_5);
		_l_pm2_5_24h = (TextView)findViewById(R.id.l_pm2_5_24h);
		_l_date = (TextView)findViewById(R.id.l_date);
		
		//1.ViewModel related: 1.IProperty event for UI Update 2.ICommand for dispatching UI Command to ViewModel
		final MainActivity thisInstance = this;
		if(_vm_aqi == null){
			_vm_aqi = AQIViewModel.getInstance();
			//listen to relative ViewModel
			_listenToVM_aqi = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					assert(event.getPropertyName().equals(Constant.PropertyName.AQIData));
					//blank event. acquire substantial data directly from VM in the update routine.
					thisInstance.updateAQI();
				}
			};
			_vm_aqi.addPropertyChangeListener(Constant.PropertyName.AQIData, _listenToVM_aqi);
		}
		_vm_aqi.onRefresh();
	}

	@Override
	protected void onResume() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onResume", getClass().getSimpleName()));
		AQIServiceApp.getInstance().registerUIContext(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onDestroy", getClass().getSimpleName()));
		_ueh.restoreOriginalHandler();
		_ueh = null;
		AQIServiceApp.getInstance().unregisterUIContext(this);
		super.onDestroy();
	}

	//-- Public and internal Methods --------------------------------------
	//-- Private and Protected Methods ------------------------------------
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

	//-- Event Handlers ---------------------------------------------------
	private void updateAQI(){
		if(_vm_aqi == null)
			return;

		AqiInfo aqiInfo = _vm_aqi.getAqiInfo();
		_l_area.setText(aqiInfo.getAqiArea());
		_l_aqi_descript.setText(composeAqiDescript(aqiInfo.getAqiRank()));
		//rct_aqi.setFill(Color.web(String.format("#%x", aqiInfo.getAqiColor())));
		_l_aqi.setText(String.format("%d", aqiInfo.getAqiValue()));
		_l_pm2_5.setText(String.format("%d", aqiInfo.getAqiPM2_5()));
		_l_pm2_5_24h.setText(String.format("%d", aqiInfo.getAqiPM2_5_24h()));
		_l_date.setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(aqiInfo.getAqiDate()));//TODO: UTC->LTC
	}

}
