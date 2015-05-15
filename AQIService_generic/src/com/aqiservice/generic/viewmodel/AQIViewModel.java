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

package com.aqiservice.generic.viewmodel;

import com.aqiservice.generic.common.Constant;
import com.aqiservice.generic.dal.AqiInfoLoader;
import com.aqiservice.generic.domain.AqiInfo;
import com.thinkalike.generic.Loader;
import com.thinkalike.generic.concurrent.Executor;
import com.thinkalike.generic.concurrent.FutureTask;
import com.thinkalike.generic.viewmodel.ViewModelBase;


/**
 * managed Constant.PropertyName: AQIData
 */
public class AQIViewModel extends ViewModelBase{
	//-- Constants and Enums --------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	private static AQIViewModel _this;
	private static String _aqiAreaCode;
	private static AqiInfo _aqiInfo;
	private static AqiInfo _aqiInfo_prev;

	//-- Properties --------------------------
	public String getAqiAreaCode(){return _aqiAreaCode;}
	public AqiInfo getAqiInfo(){return _aqiInfo;}
	
	//-- Constructors --------------------------
	private AQIViewModel()
	{
		_aqiAreaCode = AqiInfo.DEFAULT_CITY_CODE;
		//_aqiInfo = new AqiInfo(0, "", new Date(), 0, 0);
	}
	
	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------
	public static AQIViewModel getInstance() {
		if(_this == null)
			_this = new AQIViewModel();
		return _this;
	}

	//-- Private and Protected Methods --------------------------
	private void updateAqiInfo(){
		//IMPROVE: check latest update time to prevent requesting update too frequently
		
		Executor<AqiInfo> asynExec = Loader.getInstance().getPlatform().getFactory().createAsyncExecutor(_aqiInfo);
		if(asynExec==null){
			AqiInfo result = AqiInfoLoader.loadAqiInfo(_aqiAreaCode);
			if(result!=null && !result.equals(_aqiInfo)){
				_aqiInfo_prev = _aqiInfo;
				_aqiInfo = result;
				this.firePropertyChange(Constant.PropertyName.AQIData, _aqiInfo_prev, _aqiInfo);
			}
		}
		else{
			final AQIViewModel thisInstance = this;
			FutureTask<String, AqiInfo> task = new FutureTask<String, AqiInfo>(_aqiAreaCode){
				@Override
				protected AqiInfo call(String... params) {
					if(params==null || params.length<1)
						throw new NullPointerException();
					String aqiAreaCode = params[0];
					return AqiInfoLoader.loadAqiInfo(aqiAreaCode);
				}

				@Override
				public void onSuccess(AqiInfo result) {
					super.onSuccess(result);
					if(result!=null && !result.equals(_aqiInfo)){
						_aqiInfo_prev = _aqiInfo;
						_aqiInfo = result;
						thisInstance.firePropertyChange(Constant.PropertyName.AQIData, _aqiInfo_prev, _aqiInfo);
					}
				}
			};
			asynExec.execute(task);
		}
	}
	
	//-- Event Handlers --------------------------
	public void onAreaChanged(String aqiAreaCode){
		//IMPROVE: check validity
		_aqiAreaCode = aqiAreaCode;
		updateAqiInfo();
	}
	public void onRefreshIntervalChanged(int timeInSecond){
		//TODO
	}
	public void onRefresh(){
		updateAqiInfo(); //async or sync calling
	}

}

