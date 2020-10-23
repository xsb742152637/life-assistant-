package com.xie.lifeassistant;

import com.xie.lifeassistant.model.core.menutree.entity.CoreMenuTreeInfoEntity;
import com.xie.lifeassistant.model.core.menutree.service.CoreMenuTreeInfoService;
import com.xie.lifeassistant.model.core.menuurl.entity.CoreMenuUrlInfoEntity;
import com.xie.lifeassistant.model.core.menuurl.service.CoreMenuUrlInfoService;
import com.xie.lifeassistant.model.core.treeinfo.service.CoreTreeInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class LifeAssistantApplicationTests {
	@Autowired
	private CoreMenuUrlInfoService mainService;
	@Autowired
	private CoreTreeInfoService treeInfoService;
	@Autowired
	private CoreMenuTreeInfoService menuTreeInfoService;
	@Test
	void contextLoads() {
		//ApplicationContext.getBean(CoreTreeInfoService.class).findRoots();
		//List<CoreTreeInfoEntity> list = treeInfoService.findRoots();
		CoreMenuTreeInfoEntity entity = menuTreeInfoService.findByCode("meterial_bill");
		List<Map<String,Object>> list = menuTreeInfoService.getMenuTree(false, true,true);
		int page = 1;
		int rows = 10;

//		deleteOne();
		//saveOne();
        //
		//String searchKey = "l";
		//QueryWrapper<CoreMenuUrlInfoEntity> qw = new QueryWrapper<>();
		//qw.likeRight("URL_ID","b7");
		//qw.and(i -> i.like("TITLE", searchKey).or().like("CODE", searchKey).or().like("URL",searchKey));
        //
		//IPage<CoreMenuUrlInfoEntity> p = new Page<>(1,25);
		//p = mainService.page(p,qw);
		//System.out.println(p.getRecords().size());
		//List<CoreMenuUrlInfoEntity> list = mainService.getMainInfo(primaryId,searchKey,page,rows);
		//Integer count = mainService.getMainCount(primaryId,searchKey);
		//mainService.delete("69fb54f0-64ff-4dd0-b31d-3c12b14ba568");

	}

	public void deleteOne(){
		CoreMenuUrlInfoEntity entity = new CoreMenuUrlInfoEntity();
		//entity.setUrlId("aa");
		entity.deleteById();
	}

	public void saveOne(){
		CoreMenuUrlInfoEntity entity = new CoreMenuUrlInfoEntity();
		//entity.setUrlId(UUID.randomUUID().toString());
		//entity.setTitle("ccc");
		//entity.setCode("bbb");
//		entity.setSysTime(new LocalDateTime())
		mainService.saveOrUpdate(entity);
		System.out.println("保存一条数据");
	}

	public CoreMenuUrlInfoEntity findOne(String id){
		System.out.println("查询数据");
		return mainService.getById(id);
	}
}
