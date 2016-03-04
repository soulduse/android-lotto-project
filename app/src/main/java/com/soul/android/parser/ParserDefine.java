package com.soul.android.parser;

/**
 * Created by sould on 2016-03-03.
 */
public interface ParserDefine {

	public static final String BASE_URL   = "http://www.nlotto.co.kr/common.do?method=getLottoNumber";
	public static final String APPOINT_URL  = BASE_URL+"&drwNo=";

	public static final class LOTTODATE{
		public static final String  BNUSNO          = "bnusNo";
		public static final String  FIRSTWINAMNT    = "firstWinamnt";
		public static final String  TOTSELLAMNT     = "totSellamnt";
		public static final String  DRWTNO1         = "drwtNo1";
		public static final String  DRWTNO2         = "drwtNo2";
		public static final String  DRWTNO3         = "drwtNo3";
		public static final String  DRWTNO4         = "drwtNo4";
		public static final String  DRWTNO5         = "drwtNo5";
		public static final String  DRWTNO6         = "drwtNo6";
		public static final String  DRWNO           = "drwNo";
		public static final String  FIRSTPRZWNERCO  = "firstPrzwnerCo";
		public static final String  DRWNODATE       = "drwNoDate";
	}
}