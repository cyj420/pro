package com.sbs.cyj.readit.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.AttrDao;
import com.sbs.cyj.readit.dto.Attr;

@Service
public class AttrService {
	@Autowired
	private AttrDao attrDao;

	public Attr get(String name) {
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("relTypeCode", relTypeCode);
		map.put("relId", relId);
		map.put("typeCode", typeCode);
		map.put("type2Code", type2Code);
		
		return attrDao.get(map);
	}

	public int setValue(String name, String value) {
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("relTypeCode", relTypeCode);
		map.put("relId", relId);
		map.put("typeCode", typeCode);
		map.put("type2Code", type2Code);
		map.put("value", value);

		return attrDao.setValue(map);
	}

	public String getValue(String name) {
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("relTypeCode", relTypeCode);
		map.put("relId", relId);
		map.put("typeCode", typeCode);
		map.put("type2Code", type2Code);
		
		return attrDao.getValue(map);
	}

	public int remove(String name) {
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("relTypeCode", relTypeCode);
		map.put("relId", relId);
		map.put("typeCode", typeCode);
		map.put("type2Code", type2Code);
		
		return attrDao.remove(map);
	}
} 