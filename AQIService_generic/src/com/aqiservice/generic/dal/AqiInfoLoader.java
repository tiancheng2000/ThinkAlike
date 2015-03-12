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

package com.aqiservice.generic.dal;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aqiservice.generic.domain.AqiInfo;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;

public class AqiInfoLoader {
	//-- Constants and Enums -----------------------------------
	public final static String CITY_CODES[] = {"010","021","0512","0571","025","023","028","027","029","020","0755","0756","0592",}; //Shanghai
	public final static String CITY_NAMES[] = {"北京","上海","苏州","杭州","南京","重庆","成都","武汉","西安","广州","深圳","珠海","厦门"}; //Shanghai
	private final static String TOKEN = "********************"; //api key, hidden behind an agent service (Python)
	private final static int TIMEOUT_READ = 10 * 1000;
	private final static int TIMEOUT_CONNECT = 15 * 1000;
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private static HttpURLConnection _conn;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static AqiInfo loadAqiInfo(String cityCode){
		AqiInfo result = null;
		InputStream is = null;
		
		try {
			//1.retrieve AQI data from web service (JSON)
			if(_conn == null){
				String request = String.format("http://tchu.sinaapp.com/?city=%s",cityCode);
				//String request = String.format("http://www.pm25.in/api/querys/pm2_5.json?city=%s&token=%s&stations=no", 
				//							cityCode, AqiInfoLoader.TOKEN);
				URL requestUrl = new URL(request);
				_conn = (HttpURLConnection)requestUrl.openConnection();
				_conn.setReadTimeout(TIMEOUT_READ);
				_conn.setConnectTimeout(TIMEOUT_CONNECT);
				_conn.setRequestMethod("GET");
			}
			_conn.connect();
			is = _conn.getInputStream();
			//String response = "[{\"aqi\":61,\"area\":\"上海\",\"pm2_5\":43,\"pm2_5_24h\":30,\"quality\":\"良\",\"primary_pollutant\":\"颗粒物(PM2.5)\",\"time_point\":\"2014-02-11T10:00:00Z\"}]";
			//is = new ByteArrayInputStream(response.getBytes()); //for local string encoding = UTF16. for HTTP response consider StandardCharsets.UTF_8
		
			//2.data conversion (JSON->AqiInfo)
			AqiInfoParser parser = new JsonAqiInfoParser();
			//NOTE: for compatability with Android (requires JDK1.6), generic module MUST avoid using JDK1.7+ features, e.g.StandardCharsets.UTF_8 
			result = parser.parse(is, "UTF-8");
			
		} catch (Exception e) {
			Util.error(LogTag.NetworkThread, e.getMessage());
			return null;
		} finally {
			//clear up
			try{if(is != null)is.close();}
			catch(Exception e){}
			//keep connection for continuous data retrieving?
			//_conn.disconnect();
			_conn = null; //TEMP
		}
		
		return result;
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

