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

    /**
     * 在invoke方法中实现mapper接口的增删改查的动作，其本质就是调用MyExcutor执行sql语句
     * 而MyExcutor可以通过MySqlSession进行调用，sql语句就需要根据包名+类名+方法名在MyConfiguration中进行定位
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据包名+类名+方法名生成mapperStatementKey以定位sql语句
        String mapperStatementKey = method.getDeclaringClass().getName() + "." + method.getName();
        Class<?> clazz = method.getReturnType();
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
