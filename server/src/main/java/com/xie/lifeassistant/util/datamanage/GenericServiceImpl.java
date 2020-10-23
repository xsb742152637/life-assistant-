package com.xie.lifeassistant.util.datamanage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.lifeassistant.model.core.guidefile.service.impl.CoreGuideFileServiceImpl;
import com.xie.lifeassistant.util.Util;
import com.xie.lifeassistant.util.spring.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements GenericService<T>{
	public static final int BATCH_SIZE = 1000;

	@Override
	public T findById(String primaryId) {
		return getBaseMapper().selectById(primaryId);
	}
	@Override
	public List<T> findAll() {
		QueryWrapper<T> qw = new QueryWrapper<>();
		return getBaseMapper().selectList(qw);
	}

	@Override
	@Transactional
	public void save(List<T> list) {
		if(list == null || list.size() < 1){
			return;
		}
		saveOrUpdateBatch(list, BATCH_SIZE);
	}
	@Override
	@Transactional
	public void deleteById(String primaryId) {
		getBaseMapper().deleteById(primaryId);
	}
	@Override
	@Transactional
	public void deleteAll() {
		QueryWrapper<T> qw = new QueryWrapper<>();
		getBaseMapper().delete(qw);
	}

	public Map<String,Object> entityToMap(Object obj){
		Map<String,Object> map = new HashMap<>();
		Class c = obj.getClass();
		try{
			for(Field field : c.getDeclaredFields()){
				//设置为可访问
				field.setAccessible(true);
				map.put(field.getName().toString(),field.get(obj));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}

	//将list中map的key按照驼峰命名法进行转换,如:MENU_ID > menuId
	public List<Map<String,Object>> underlineToHump(List<Map<String,Object>> list){
		List<Map<String,Object>> listNew = new ArrayList<>();
		for(Map<String,Object> map : list){
			Map<String,Object> mapNew = new HashMap<>();
			for(Map.Entry<String,Object> m : map.entrySet()){
				mapNew.put(Util.LineAndHump.lineToHump(m.getKey()),m.getValue() == null ? "" : m.getValue());
			}
			listNew.add(mapNew);
		}
		return listNew;
	}

	//下划线转驼峰，如:MENU_ID > menuId
	public String underlineToHump(String field){
		return Util.LineAndHump.lineToHump(field);
	}
	//驼峰命名转为下划线命名,如：menuId > MENU_ID
	public String humpToUnderline(String field){
		StringBuilder sb=new StringBuilder(field);
		int temp=0;//定位
		if (!field.contains("_")) {
			for(int i=0;i<field.length();i++){
				if(Character.isUpperCase(field.charAt(i))){
					sb.insert(i+temp, "_");
					temp+=1;
				}
			}
		}
		return sb.toString().toUpperCase();
	}
}
