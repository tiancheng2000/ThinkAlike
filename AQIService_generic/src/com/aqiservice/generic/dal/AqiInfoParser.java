package com.aqiservice.generic.dal;

import java.io.InputStream;

import com.aqiservice.generic.domain.AqiInfo;

public interface AqiInfoParser {
	AqiInfo parse(InputStream is, String charsetName);
}
