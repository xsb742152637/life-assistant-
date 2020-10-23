package ${package.Controller};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${table.entityName};
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @comment ${table.comment!}
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/${cfg.modelType}/${cfg.modelCode}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass}<${entity}> {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.serviceName} service;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(@ApiParam(name="searchKey",value="关键字") @RequestParam String searchKey,
            @ApiParam(name="page",value="当前页数",required=true,defaultValue="1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name="rows",value="每页条数",required=true,defaultValue="10") @RequestParam(defaultValue = "10") Integer rows){

        QueryWrapper<${table.entityName}> qw = new QueryWrapper<>();
        qw.and(i -> i
                .like("", searchKey).or()
                .like("",searchKey)
        );
        return getTable(service.page(new Page<>(page,rows), qw));
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(${table.entityName} entity){
        try {

            service.saveOrUpdate(entity);
            return returnSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }

    @RequestMapping("delete")
    @ApiOperation(value = "删除", httpMethod = "POST")
    public String delete(@ApiParam(name="primaryId",value="主键",required=true)  @RequestParam String primaryId){

        try {
            service.deleteById(primaryId);
            return returnSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }

}
</#if>
