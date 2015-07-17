/**
* Copyright 2013-2015 Tiancheng Hu
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

package com.aqiservice.jfx.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.aqiservice.generic.common.Constant;
import com.aqiservice.generic.domain.AqiInfo;
import com.aqiservice.generic.viewmodel.AQIViewModel;
import com.aqiservice.jfx.res.Res;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;

public class MainController implements Initializable {

	//-- Constants and Enums --------------------------
	public static final String TAG = MainController.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------
	//-- Instance and Shared Fields --------------------------
	//kw: it seems inconvenient to keep the "_" convention for FXML field variables, since the names must be same with those in FXML files (fx:id).
	@FXML
	Label l_area;
	@FXML
	Label l_aqi_descript;
	@FXML
	Rectangle rct_aqi;
	@FXML
	Label l_aqi;
	@FXML
	Label l_pm2_5;
	@FXML
	Label l_pm2_5_24h;
	@FXML
	Label l_date;

	//MVVM
	private AQIViewModel _vm_aqi = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM_aqi = null; 

	//Sample of another kind of data-binding (by using JavaFX's ObservableList)
	//private ItemListViewModel _vm_itemList = new ItemListViewModel();
    //private ItemDetailViewModel _vm_itemDetail = new ItemDetailViewModel();
    //final ObservableList<Item> _listItems = FXCollections.observableArrayList(_vm_aqi.getInstance().getItemList());        

	Timer _timer = new Timer(true); //NOTE: auto stop on app termination
	
	//-- Properties --------------------------
	//-- Constructors --------------------------
	public MainController(){
		//NOTE: UI elements cannot be manipulated here. They are not mapped and initialized yet.
	}
	
	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: initialize", getClass().getSimpleName()));

		//0.UI Controls initialization
		//IMPROVE: if necessary, make gridline visible with specified color. https://community.oracle.com/message/10320543

		//1.ViewModel related: 1.IProperty event for UI Update 2.ICommand for dispatching UI Command to ViewModel
		final MainController thisInstance = this;
		if(_vm_aqi == null){
			_vm_aqi = AQIViewModel.getInstance();
			//listen to relative ViewModel
			_listenToVM_aqi = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					assert(event.getPropertyName().equals(Constant.PropertyName.AQIData));
					//blank event. acquire substantial data directly from VM in the update routine.
					//thisInstance.updateAQI(); //it's not safe to directly update UI in callback
					//new Thread(updateUiTask).start(); //needn't to use Task<> to update UI
					Platform.runLater(new Runnable() {
	                     @Override public void run() {
	                    	 thisInstance.updateAQI();
	                     }
	                });
				}
			};
			_vm_aqi.addPropertyChangeListener(Constant.PropertyName.AQIData, _listenToVM_aqi);
		}
		//set Timer
		//_vm_aqi.onRefresh();
		_timer.schedule(new TimerTask(){
				@Override
				public void run() {
					_vm_aqi.onRefresh();
				}
			}, 0, 30*60*1000);
	}

	//-- Public and internal Methods --------------------------
    //-- Private and Protected Methods --------------------------
	private String composeAqiDescript(int aqiRank){
		//IMPROVE: use Platform.getString(), with improved I18N solution!~
		String result = "";
		ResourceBundle bundle = ResourceBundle.getBundle(Res.getValues("bundle"));
		switch(aqiRank){
		case AqiInfo.AQI_RANK_EXCELLENT:
			result = bundle.getString("aqi_rank_excellent");
			break;
		case AqiInfo.AQI_RANK_FINE:
			result = bundle.getString("aqi_rank_fine");
			break;
		case AqiInfo.AQI_RANK_P_SLIGHT:
			result = bundle.getString("aqi_rank_p_slight");
			break;
		case AqiInfo.AQI_RANK_P_MODERATE:
			result = bundle.getString("aqi_rank_p_moderate");
			break;
		case AqiInfo.AQI_RANK_P_SEVERE:
			result = bundle.getString("aqi_rank_p_severe");
			break;
		case AqiInfo.AQI_RANK_P_MOSTSEVERE:
			result = bundle.getString("aqi_rank_p_mostsevere");
			break;
		default:
			break;
		}
		return result;
	}
	
	//-- Event Handlers --------------------------
	private void updateAQI(){
		if(_vm_aqi == null)
			return;

		AqiInfo aqiInfo = _vm_aqi.getAqiInfo();
		l_area.setText(aqiInfo.getAqiArea());
		l_aqi_descript.setText(composeAqiDescript(aqiInfo.getAqiRank()));
		//rct_aqi.setFill(Color.web(String.format("#%x", aqiInfo.getAqiColor()))); //IMPROVE: apply gradient
		l_aqi.setText(String.format("%d", aqiInfo.getAqiValue()));
		l_pm2_5.setText(String.format("%d", aqiInfo.getAqiPM2_5()));
		l_pm2_5_24h.setText(String.format("%d", aqiInfo.getAqiPM2_5_24h()));
		l_date.setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(aqiInfo.getAqiDate()));//TODO: UTC->LTC
	}
}
