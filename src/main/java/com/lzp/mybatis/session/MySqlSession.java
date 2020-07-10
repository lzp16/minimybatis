package com.lzp.mybatis.session;

import com.lzp.mybatis.executor.MyExecutor;
import com.lzp.mybatis.mapping.MapperStatement;
import com.lzp.mybatis.mapping.MyConfiguration;
import com.lzp.mybatis.proxy.MapperProxy;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 使用了代理模式
 * Created by LiZhanPing on 2019/12/18.
 */
public class MySqlSession {

    private MyConfiguration myConfiguration;

    private MyExecutor myExecutor;

    public MySqlSession(MyConfiguration myConfiguration, MyExecutor myExecutor) {
        this.myConfiguration = myConfiguration;
        this.myExecutor = myExecutor;
    }

    public <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new MapperProxy(this));
    }

    /**
     * 只是简单实现，所以只实现一个参数的情况
     *
     * @param mapperStatementKey
     * @param args
     * @param <T>
     * @return
     */
    public <T> T selectOne(String mapperStatementKey, Object args) {
        MapperStatement mapperStatement = myConfiguration.getMapperStatementMap().get(mapperStatementKey);
        List<T> resultList = myExecutor.query(mapperStatement, args);
        if (resultList != null && resultList.size() > 1) {
            throw new RuntimeException("too many result.");
        } else {
            return resultList.get(0);
        }
    }

    public <T> T selectList(String mapperStatementKey, Object args) {
        return null;
    }

    public <T> T selectMap(String mapperStatementKey, Object args) {
        return null;
    }
}
