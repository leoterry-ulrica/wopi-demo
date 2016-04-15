package com.dist.common;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

/**
 * json转换的工具类
 * @author heshun
 * @version V1.0, 2014-2-21
 */
public class JSONUtil {
    /** 
     * 设置日期转换格式 
     */
    static {
        //注册器  
        MorpherRegistry mr = JSONUtils.getMorpherRegistry();

        //可转换的日期格式，即Json串中可以出现以下格式的日期与时间  
        DateMorpher dm = new DateMorpher(new String[] { DateUtil.DATE_FORMAT_STR, DateUtil.DATETIME_FORMAT_STR });
        mr.registerMorpher(dm);
    }


    public static String getString(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return null;
        }

    }
    
    public static String toString(Object obj){
        return JSONObject.fromObject(obj).toString();
    }
    

    /**
     * 添加时间类型到JSON字符串
     * @param str
     * @param key 
     * @param date
     */
    public static String addTime(String str, String key, String date) {
        JSONObject jsonObj = JSONObject.fromObject(str);
        jsonObj.element(key, date);
        str = jsonObj.toString();
        return str;
    }

    /** 
     * 把实体Bean、Map对象、数组、列表集合转换成Json串 
     * @param obj  
     * @return 
     * @throws Exception String 
     */
    public static String getJsonStr(Object obj) {
        String jsonStr = null;
        //Json配置      
        JsonConfig jsonCfg = new JsonConfig();

        //注册日期处理器  
        jsonCfg.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor(DateUtil.DATETIME_FORMAT_STR));
        jsonCfg.setIgnoreDefaultExcludes(false); //设置默认忽略
        jsonCfg.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);   //此处是亮点，不过经过测试，第2种方法有些
        
        if (obj == null) {
            return "{}";
        }

        if (obj instanceof Collection || obj instanceof Object[]) {
            jsonStr = JSONArray.fromObject(obj, jsonCfg).toString();
        } else {
            jsonStr = JSONObject.fromObject(obj, jsonCfg).toString();
        }

        return jsonStr;
    }

    /**
     * 判断json字符串中，某个键是否有值。
     * @param jsonStr json字符串
     * @param key 键 
     * @return
     */
    public static boolean valueNotNull(String jsonStr, String key) {
        JSONObject jsonObj = JSONObject.fromObject(jsonStr);
        String value = jsonObj.getString(key);
        if(StringUtil.isNullOrEmpty(value)){
            return false; 
        }
        return true;
    }
   
}

/** 
 * json日期值处理器实现   
 * (C) 2009-9-11, jzj 
 */  
class JsonDateValueProcessor implements JsonValueProcessor {    
  
    private String format = DateUtil.DATETIME_FORMAT_STR;  
  
    public JsonDateValueProcessor() {  
  
    }  
  
    public JsonDateValueProcessor(String format) {  
        this.format = format;  
    }  
  
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {  
        return process(value, jsonConfig);  
    }  
  
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {  
        return process(value, jsonConfig);  
    }  
  
    private Object process(Object value, JsonConfig jsonConfig) {  
        if (value instanceof Date) {  
            String str = new SimpleDateFormat(format).format((Date) value);  
            return str;  
        }  
        return value == null ? null : value.toString();  
    }  
  
    public String getFormat() {  
        return format;  
    }  
  
    public void setFormat(String format) {  
        this.format = format;  
    }  
}
