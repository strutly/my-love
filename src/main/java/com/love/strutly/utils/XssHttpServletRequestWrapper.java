package com.love.strutly.utils;

import com.alibaba.fastjson.JSON;
import com.love.strutly.filter.SensitiveFilter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONTokener;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * XSS过滤处理
 *
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    @Override
    public String getHeader(String name) {
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));
    }
    @Override
    public String getQueryString() {
        return StringEscapeUtils.escapeHtml4(super.getQueryString());
    }
    @Override
    public String getParameter(String name) {
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));
    }
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for(int i = 0; i < length; i++){
                escapseValues[i] = StringEscapeUtils.escapeHtml4(values[i]);
            }
            return escapseValues;
        }
        return values;
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        String str=getRequestBody(super.getInputStream());

        Object json = null;
        try {
            json = new JSONTokener(str).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();

        }
        if (json instanceof JSONArray) {
            str = json.toString();
        } else if (json instanceof JSONObject) {

            Map<String,Object> map= JSON.parseObject(str,Map.class);
            Map<String,Object> resultMap = new HashMap<>(map.size());
            map.forEach((k,v)->{
                if(v instanceof String){
                    String val = StringEscapeUtils.escapeHtml4(v.toString());
                    resultMap.put(k,val);
                }else{
                    resultMap.put(k,v);
                }
            });
            str = JSON.toJSONString(resultMap);
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }
    private String getRequestBody(InputStream stream) {
        System.out.println("提交进来了~~");
        String line = "";
        StringBuilder body = new StringBuilder();
        int counter = 0;
        // 读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        try {
            while ((line = reader.readLine()) != null) {
                body.append(line);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }
}