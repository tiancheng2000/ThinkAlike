package com.aqiservice.generic.domain;

import java.util.Date;
import java.util.Locale;

public class AqiInfo {
	//-- Constants and Enums -----------------------------------
	 //default=Shanghai(上海)
	public final static String CITY_CODES[] = {"010","021","0512","0571","025","023","028","027","029","020","0755","0756","0592",};
	private final static int DEFAULT_CITY_CODE_INDEX = 1;
	public final static String DEFAULT_CITY_CODE = CITY_CODES[DEFAULT_CITY_CODE_INDEX];
	public final static int AQI_RANK_EXCELLENT = 0;
	public final static int AQI_RANK_FINE = 1;
	public final static int AQI_RANK_P_SLIGHT = 2;
	public final static int AQI_RANK_P_MODERATE = 3;
	public final static int AQI_RANK_P_SEVERE = 4;
	public final static int AQI_RANK_P_MOSTSEVERE = 5;
	private final static int AQI_RANK_INVALID = -1;
	private final static int AQI_RANKS[] = new int[]{50, 100, 150, 200, 300, Integer.MAX_VALUE};
	private final static int AQI_COLOR_ONERROR = 0x000000;
	private final static int AQI_COLOR_INVALID = -1;
	private final static int AQI_COLORS[] = new int[]{0x00E400, 0xFFFF00, 0xFF7E00, 0xFF0000, 0x99004C, 0x7E0023};
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	public static String CITY_NAMES[] = {"Beijing","Shanghai","Suzhou","Hangzhou","Nanjing","Chongqing","Chengdu","Wuahan","Xian","Guangzhou","Shenzhen","Zhuhai","Xiamen"};
	public final static String CITY_NAMES_zh_CN[] = {"北京","上海","苏州","杭州","南京","重庆","成都","武汉","西安","广州","深圳","珠海","厦门"};
	static {
		//IMPROVE: retrieve String array from I18N Resouce file. 
		if(!Locale.getDefault().getLanguage().equals("en")){
			CITY_NAMES = CITY_NAMES_zh_CN;
		}
	}
	private int _aqiValue;
	private int _aqiRank = AQI_RANK_INVALID;  //#depends on _aqiValue
	private int _aqiColor = AQI_COLOR_INVALID;  //#depends on _aqiRank
	private String _aqiArea;  //IMPROVE: support Location enum
	private String _aqiAreaCode;
	private Date _aqiDate;
	private int _aqiPM2_5;
	private int _aqiPM2_5_24h;
	
	//-- Properties --------------------------------------------
	public int getAqiRank(){
		if(_aqiRank == AQI_RANK_INVALID){
			_aqiRank=AqiInfo.getAqiRank(_aqiValue);
		}
		return _aqiRank;
	}
	public int getAqiColor(){
		if(_aqiColor == AQI_RANK_INVALID){
			_aqiColor=AqiInfo.getAqiColor(this.getAqiRank());
		}
		return _aqiColor;
	}
	public int getAqiValue() {return _aqiValue;}
	public String getAqiArea() {return _aqiArea;}
	public String getAqiAreaCode() {return _aqiAreaCode;}
	public Date getAqiDate() {return _aqiDate;}
	public int getAqiPM2_5() {return _aqiPM2_5;}
	public int getAqiPM2_5_24h() {return _aqiPM2_5_24h;}

	//-- Constructors ------------------------------------------
	public AqiInfo(int aqiValue, String aqiArea, String aqiAreaCode, Date aqiDate, int aqiPM2_5, int aqiPM2_5_24h){
		_aqiValue = aqiValue;
		_aqiArea = (_aqiArea==null) ? "" : aqiArea;
		_aqiAreaCode = (aqiAreaCode==null) ? "" : aqiAreaCode;
		_aqiDate = aqiDate;
		_aqiPM2_5 = aqiPM2_5;
		_aqiPM2_5_24h = aqiPM2_5_24h;
		
		//areaCode has higher priority 
		if(_aqiAreaCode.length()!=0){
			_aqiArea = getCityName(_aqiAreaCode);
		}
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AqiInfo)
	    {
			AqiInfo other = (AqiInfo)obj;
	        return other.getAqiValue() == getAqiValue() &&
	                other.getAqiArea().equals(getAqiArea()) &&
	                other.getAqiDate().equals(getAqiDate()) &&
	                other.getAqiPM2_5() == getAqiPM2_5() &&
	                other.getAqiPM2_5_24h() == getAqiPM2_5_24h();

	    }
        return false;
	}

	//-- Public and internal Methods ---------------------------
	public static String getCityName(String cityCode){
		for(int i=0; i<CITY_CODES.length; i++){
			if (cityCode.equals(CITY_CODES[i]))
				return CITY_NAMES[i];
		}
		return (CITY_NAMES.length > DEFAULT_CITY_CODE_INDEX) ? CITY_NAMES[DEFAULT_CITY_CODE_INDEX] : "NO_NAME";
	}

	public static int getAqiRank(int aqiValue){
		for(int i=0; i<AQI_RANKS.length; i++){
			if (aqiValue<=AQI_RANKS[i])
				return i;
		}
		return AQI_RANK_P_MOSTSEVERE;
	}
	
	public static int getAqiColor(int aqiRank){
		if(aqiRank>=0 && aqiRank<AQI_COLORS.length){
			return AQI_COLORS[aqiRank];
		}
		return AQI_COLOR_ONERROR;
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}
