package com.xie.lifeassistant.util.context;

import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class AppendParameterRequest extends HttpServletRequestWrapper {
    public AppendParameterRequest(HttpServletRequest request) {
        super(request);
    }

    private final Map<String, String[]> parameterMap = new HashMap<String, String[]>();

    public void setParameter(String name, String... value) {
        this.parameterMap.put(name, value);
    }

    public void setParameter(Map<String, String[]> parameterMap) {
        this.parameterMap.putAll(parameterMap);
    }

    @Override
    public String getParameter(String name) {
        if (this.parameterMap.containsKey(name)) {
            return this.parameterMap.get(name)[0];
        }
        return super.getParameter(name);
    }


    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.getParameterMap().keySet());
    }

    public String[] getParameterValues(java.lang.String name) {
        if (this.parameterMap.containsKey(name)) {
            return this.parameterMap.get(name);
        }
        return super.getParameterValues(name);
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = Maps.newHashMap(super.getParameterMap());
        result.putAll(this.parameterMap);
        return result;
    }

    Map<String, Object> attributeMap = new HashMap<String, Object>();

    public Object getAttribute(String name) {
        if (this.attributeMap.containsKey(name)) {
            return this.attributeMap.get(name);
        }
        return super.getAttribute(name);
    }

    public void setAttribute(String name, Object o) {
        this.attributeMap.put(name, o);
    }

    public void removeAttribute(String name) {
        if (this.attributeMap.containsKey(name)) {
            this.attributeMap.remove(name);
        } else {
            super.removeAttribute(name);
        }
    }

}
