package com.sbs.cyj.readit.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chapter {
	private int id;
	private String title;
	private String body;
	private String regDate;
	private String updateDate;
	private String delDate;
	private boolean delStatus;
	private boolean displayStatus;
	private int memberId;
	private int novelId;
	private Map<String, Object> extra;
}