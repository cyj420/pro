package com.sbs.cyj.readit.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {
	private int id;
	private String name;
	private String regDate;
	private String updateDate;
	private String delDate;
	private boolean delStatus;
	private int boardId;
	private Map<String, Object> extra;
}