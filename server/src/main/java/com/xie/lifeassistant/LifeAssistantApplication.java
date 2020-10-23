package com.xie.lifeassistant;

import com.xie.lifeassistant.model.core.treeinfo.service.CoreTreeInfoService;
import com.xie.lifeassistant.util.spring.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LifeAssistantApplication {

	public static void main(String[] args) {
		org.springframework.context.ApplicationContext context = SpringApplication.run(LifeAssistantApplication.class, args);
		System.out.println("启动项目");
		ApplicationContext.setApplicationContext(context);

		ApplicationContext.getBean(CoreTreeInfoService.class).intoTrees();
	}

}
