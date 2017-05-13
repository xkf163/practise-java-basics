package cn.xukangfeng.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 57257 on 2017/5/13.
 */
public interface RedisDao {


    public <T> String addByKey(String key, T object)  throws IOException;

    public <T> String add(T object)  throws IOException ;

    public String getObject(String key)  throws IOException;

    public <T> List<String> addList(List<T> list)  throws IOException;

    public <T> String addListKey(List<String> strList, List<T> list)  throws IOException;

    public <T> Long addListKey(Map<String, T> map)  throws IOException;

    public Long deleteByKey(String key)  throws IOException;

    public Long batchDelete(List<String> strList)  throws IOException;



}
