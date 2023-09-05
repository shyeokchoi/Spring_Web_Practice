/***************************************************
 * Copyright(c) 2022-2023 WEMADE right reserved.
 *
 * Revision History
 * Author : hubert
 * Date : Fri Sep 23 2022  
 * Description : 
 *
 ****************************************************/
package com.board.constant;

public class FrkConstants {

	private FrkConstants() {
		throw new IllegalStateException("FrkConstants class");
	}

	// Yes/No
	public final static String YES = "Y";
	public final static String NO = "N";
	public final static String RESERVED = "R";

	// On/Off
	public final static String ON = "ON";
	public final static String OFF = "OFF";

	// OK/NOK/Success/Fail
	public final static String OK = "OK";
	public final static String NOK = "NOK";
	public final static String SUCCESS = "Success";
	public final static String FAIL = "Fail";

	public final static String FAIL_CD = "F";
	public final static String SUCCESS_CD = "S";

	public final static int CD_OK = 0;
	public final static int CD_NOK = 666;
	public final static int CD_PARAM_ERR = 400;
	public final static int CD_ACCESS_DENIED = 403;
	public final static int CD_NOT_FOUND = 404;
	public final static int CD_DUPLICATED = 405;
	public final static int CD_INTERNAL_ERR = 500;
	public final static int CD_UNKNOWN = 999;

	// All
	public final static String ALL = "ALL";

	public final static String UTF8_STRING = "UTF-8";

	// RESP
	public static final int RESP_CD_OK = CD_OK;
	public static final int RESP_CD_NOK = CD_NOK;
	public static final String RESP_MSG_OK = OK;
	public static final String RESP_MSG_NOK = FAIL;

	public final static String DOT = ".";

	// ---------------------------------------------------------------------
	// Default User ID
	// ---------------------------------------------------------------------
	public static final String DEFAULT_USER_ID = "SYS"; // SYSTEM
	public static final String DEFAULT_PROGM_ID = "DBI"; // DBI

}
