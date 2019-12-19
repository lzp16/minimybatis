package com.lzp.mybatis.proxy;

import com.lzp.mybatis.session.MySqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * Created by LiZhanPing on 2019/12/18.
 */
public class MapperProxy implements InvocationHandler {

    private MySqlSession mySqlSession;

    public MapperProxy(MySqlSession mySqlSession) {
        this.mySqlSession = mySqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> clazz = method.getReturnType();
        String mapperStatementKey = method.getDeclaringClass().getName() + "." + method.getName();
        if (Collections.class.isAssignableFrom(clazz)) {
            mySqlSession.selectList(mapperStatementKey, args[0]);
        } else if (Map.class.isAssignableFrom(clazz)) {
            mySqlSession.selectMap(mapperStatementKey, args[0]);
        } else {
            return mySqlSession.selectOne(mapperStatementKey, null == args ? null : args[0]);
        }
        return null;
    }
}
