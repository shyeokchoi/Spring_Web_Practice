/***************************************************
 * Copyright(c) 2023-2024 WEMADE right reserved.
 *
 * Revision History
 * Author : hubert
 * Date : Mon Jun 19 2023
 * Description :
 *
 ****************************************************/
package com.wemade.board.framework.aop.requestLogging;

import com.wemade.board.framework.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CmLogHistDTO extends BaseDTO {

	private String userId;			// 

	private HistoryEnum type;			// 

	private String path;			// 

	private String history;			// 

	private String ip;			// 

	private long lastLoginDt;			// 

}