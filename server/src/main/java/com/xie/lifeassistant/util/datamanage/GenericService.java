package com.xie.lifeassistant.util.datamanage;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GenericService<T> extends IService<T> {
	T findById(String primaryId);
	List<T> findAll();

	void save(List<T> list);

	//根据主键删除一条数据
	void deleteById(String primaryId);
	void deleteAll();

}
