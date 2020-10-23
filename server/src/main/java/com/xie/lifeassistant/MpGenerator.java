package com.xie.lifeassistant;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * Comment：代码生成器
 * User: xiucai
 * Date: 2020/4/26 17:52
 */

public class MpGenerator {

    private static String[] tableName = {""};//表名
    protected static String modelCode = "";//模块标识。如果为空，下面自动设置
    private String modelType = "";//模块类型：core或app。如果为空，下面自动设置

    public static void main(String[] args) {
        MpGenerator mg = new MpGenerator();
        mg.run();
    }

    public void run(){
        if(tableName[0] == ""){
            return;
        }
        if(StringUtils.isBlank(modelCode)){
            modelCode = tableName[0].substring(tableName[0].indexOf("_"), tableName[0].length()).replaceAll("_","");//模块标识
        }
        if(StringUtils.isBlank(modelType)){
            modelType = tableName[0].split("_")[0].toLowerCase();//模块类型：core或app
        }
        System.out.println(url);
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        setGlobalConfig(mpg);
        setDataSourceConfig(mpg);
        setStrategyConfig(mpg);
        setPackageConfig(mpg);
        setTemplateConfig(mpg);
        setInjectionConfig(mpg);

        // 执行生成
        mpg.execute();
        // 打印注入设置【可无】
//        System.err.println(mpg.getCfg().getMap().get("abc"));
    }



    // 全局配置
    private void setGlobalConfig(AutoGenerator mpg){
        GlobalConfig gc = new GlobalConfig();
        gc.setOpen(false);
        gc.setAuthor("xie");
        gc.setOutputDir(new File("").getAbsolutePath()+ "/src/main/java");
        gc.setFileOverride(true);// 是否覆盖同名文件，默认是false
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        gc.setEntityName("%sEntity");
        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);
    }

    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/ssm-master?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
    private String username = "root";
    private String password = "root";
    // 数据源配置
    private void setDataSourceConfig(AutoGenerator mpg){
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName(driver);
        dsc.setUrl(url);
        dsc.setUsername(username);
        dsc.setPassword(password);

        // 自定义数据库表字段类型转换【可选】
        dsc.setTypeConvert(new MySqlTypeConvert() {
                    @Override
                    public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        //将数据库中datetime转换成timestamp
                        if ( fieldType.toLowerCase().contains( "datetime" ) ) {
                            System.out.println(fieldType + " 转换类型为 " + DbColumnType.BYTE_ARRAY);
                            return DbColumnType.TIMESTAMP;
                        }else if ( fieldType.toLowerCase().contains( "longblob" ) ) {
                            System.out.println(fieldType + " 转换类型为 " + DbColumnType.BYTE_ARRAY);
                            return DbColumnType.BYTE_ARRAY;
                        }
                        return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
                    }
                });
        mpg.setDataSource(dsc);
    }

    // 策略配置
    private void setStrategyConfig(AutoGenerator mpg){
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setSkipView(true);//跳过视图
//        strategy.setTablePrefix(new String[] { "user_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setEntityBuilderModel(true);//是否为构建者模型
        strategy.setEntityLombokModel(true);//是否为lombok模型
        strategy.setEntityBooleanColumnRemoveIsPrefix(false);//Boolean类型字段是否移除is前缀
        strategy.setRestControllerStyle(true);//生成 @RestController 控制器
        strategy.setEnableSqlFilter(false);//是否激活进行sql模糊表名匹配
        strategy.setLogicDeleteFieldName("deleted");//逻辑删除属性名称
        strategy.setInclude(tableName); // 需要生成的表
        strategy.setControllerMappingHyphenStyle(true);
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
         strategy.setSuperServiceClass("com.xie.lifeassistant.util.datamanage.GenericService");
        // 自定义 service 实现类父类
         strategy.setSuperServiceImplClass("com.xie.lifeassistant.util.datamanage.GenericServiceImpl");
        // 自定义 controller 父类
         strategy.setSuperControllerClass("com.xie.lifeassistant.util.datamanage.GenericController");
        // 【实体】是否生成字段常量（默认 false）
//         public static final String ID = "test_id";
//         strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);
    }

    // 包配置
    private void setPackageConfig(AutoGenerator mpg){
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.xie.lifeassistant");
        //pc.setModuleName(modelCode);
        pc.setController("controller." + modelType + "." + modelCode);
        pc.setXml("model."+ modelType + "." + modelCode +".dao.xml");
        pc.setMapper("model."+ modelType + "." + modelCode +".dao");
        pc.setEntity("model."+ modelType + "." + modelCode +".entity");
        pc.setService("model."+ modelType + "." + modelCode +".service");
        pc.setServiceImpl( "model."+ modelType + "." + modelCode +".service.impl");

        mpg.setPackageInfo(pc);
    }

    private void setTemplateConfig(AutoGenerator mpg){
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
         TemplateConfig tc = new TemplateConfig();
         //tc.setXml(null);
         //tc.setMapper(null);
         //tc.setEntity(null);
         //tc.setService(null);
         //tc.setServiceImpl(null);
         //tc.setController(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
         mpg.setTemplate(tc);

    }

    //配置自定义属性注入
    private void setInjectionConfig(AutoGenerator mpg){
        InjectionConfig injectionConfig = new InjectionConfig() {
            //模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("modelType", modelType);
                map.put("modelCode", modelCode);
                this.setMap(map);
            }
        };
        mpg.setCfg(injectionConfig);
    }
}