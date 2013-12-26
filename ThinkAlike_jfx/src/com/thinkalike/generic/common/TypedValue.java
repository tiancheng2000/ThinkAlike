package com.thinkalike.generic.common;

public class TypedValue{
	//-- Constants and Enums ----------------------------------------------
	public static final int UNIT_FRACTION_PARENT = 1;
	public static final int UNIT_SP = 2;
	public static final int UNIT_PX = 3;
	
	//-- Inner Classes and Structures -------------------------------------
	//-- Delegates and Events ---------------------------------------------
	//-- Instance and Shared Fields ---------------------------------------
	private Object _value;
	private String _unit;
	
	//-- Properties -------------------------------------------------------
	public Object getValue() { return _value; }
	public void setValue(Object value) { _value = value; }
	
	public String getUnit() { return _unit; }
	public void setUnit(String unit) { _unit = unit; }

	//-- Constructors -----------------------------------------------------
//  Use .valueOf() instead
//	public TypedValue(String tvalue){
//		this(Util.splitNumberUnit(tvalue));        
//	}
//	private TypedValue(String[] valueWithUnit){
//		this(valueWithUnit[0], valueWithUnit[1]);
//	}
	public TypedValue(Object value, String unit){
		_unit = (unit==null)?"":unit;
		
		//IMPROVE: allow float value, e.g. "1.2em"
		try{
			_value = Float.valueOf(value.toString());
		}
		catch(Exception e){}//invalid typed value
		if(_value==null)
			_value = Integer.valueOf(-1);
	}
	
	//-- Destructors ------------------------------------------------------
	//-- Base Class Overrides ---------------------------------------------
	@Override
	public String toString() {
		return String.format("%s%s", _value.toString(), _unit);
	}
	
	//-- Public and internal Methods --------------------------------------
	public static TypedValue valueOf(String tvalue){
		String[] valueWithUnit = Util.splitNumberUnit(tvalue);
		return new TypedValue(valueWithUnit[0], valueWithUnit[1]);
	}
	//-- Private and Protected Methods ------------------------------------
	//-- Event Handlers ---------------------------------------------------
	
}
