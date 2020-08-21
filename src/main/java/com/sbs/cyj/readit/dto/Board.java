package com.sbs.cyj.readit.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Board {
	private int id;
	private String code;
	private String name;
	private int memberId;
	private String regDate;
	private String updateDate;
	private String delDate;
	private boolean delStatus;
	private Map<String, Object> extra;
}