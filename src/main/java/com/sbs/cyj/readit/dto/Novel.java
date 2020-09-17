package com.sbs.cyj.readit.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Novel {
	private int id;
	private String name;
	private String regDate;
	private String updateDate;
	private String delDate;
	private boolean delStatus;
	private boolean displayStatus;
	private int cateId;
	private int memberId;
	private boolean seriesStatus;
	private int totalHit;
	private int totalCh;
	private Map<String, Object> extra;
}