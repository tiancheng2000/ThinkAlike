package com.aqiservice.generic.domain;

import java.util.Date;

public class AqiInfo {
	//-- Constants and Enums -----------------------------------
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
	private int _aqiValue;
	private int _aqiRank = AQI_RANK_INVALID;  //#depends on _aqiValue
	private int _aqiColor = AQI_COLOR_INVALID;  //#depends on _aqiRank
	private String _aqiArea;  //IMPROVE: support Location enum
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
	public Date getAqiDate() {return _aqiDate;}
	public int getAqiPM2_5() {return _aqiPM2_5;}
	public int getAqiPM2_5_24h() {return _aqiPM2_5_24h;}

	//-- Constructors ------------------------------------------
	public AqiInfo(int aqiValue, String aqiArea, Date aqiDate, int aqiPM2_5, int aqiPM2_5_24h){
		_aqiValue = aqiValue;
		_aqiArea = aqiArea;
		_aqiDate = aqiDate;
		_aqiPM2_5 = aqiPM2_5;
		_aqiPM2_5_24h = aqiPM2_5_24h;
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
