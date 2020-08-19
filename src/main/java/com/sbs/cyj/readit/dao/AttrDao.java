package com.sbs.cyj.readit.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Attr;

@Mapper
public interface AttrDao {
	int setValue(Map<String, Object> map);

//		INSERT INTO attr (regDate, updateDate, `relTypeCode`, `relId`, `typeCode`, `type2Code`, `value`)
//		VALUES (NOW(), NOW(), relTypeCode, relId, typeCode, type2Code, value)
//		ON DUPLICATE KEY UPDATE
//		updateDate = NOW()
//		, `value` = value

	Attr get(Map<String, Object> map);

//		SELECT *
//		FROM attr
//		WHERE 1
//		AND `relTypeCode` = relTypeCode
//		AND `relId` = relId
//		AND `typeCode` = typeCode
//		AND `type2Code` = type2Code

	String getValue(Map<String, Object> map);

//		SELECT `value`
//		FROM attr
//		WHERE 1
//		AND `relTypeCode` = relTypeCode
//		AND `relId` = relId
//		AND `typeCode` = typeCode
//		AND `type2Code` = type2Code

	int remove(Map<String, Object> map);

//		DELETE FROM attr
//		WHERE 1
//		AND `relTypeCode` = relTypeCode
//		AND `relId` = relId
//		AND `typeCode` = typeCode
//		AND `type2Code` = type2Code
}
