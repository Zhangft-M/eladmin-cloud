package org.micah.model.query;

import lombok.Data;
import org.micah.mp.annotation.Query;
import org.micah.mp.annotation.type.SelectType;

import java.sql.Timestamp;
import java.util.List;

@Data
public class AppQueryCriteria{

	/**
	 * 模糊
	 */
    @Query(type = SelectType.INNER_LIKE)
    private String name;

	@Query(type = SelectType.BETWEEN)
	private List<Timestamp> createTime;
}