package com.sbs.cyj.readit.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Attr {
	private int id;
	private String regDate;
	private String updateDate;
	private String name;
	private String value;
	private Map<String, Object> extra;
}