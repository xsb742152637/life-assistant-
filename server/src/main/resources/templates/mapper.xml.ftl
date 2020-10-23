<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
<#if enableCache>

    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>
</#if>

    <!--表名-->
    <sql id="tableName">${table.name}</sql>
<#list table.fields as field>
<#if field.keyFlag>
    <!--主键-->
    <sql id="primaryKey">${field.name}</sql>
</#if>
</#list>



</mapper>
