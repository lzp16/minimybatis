package com.lzp.mybatis.proxy;

import com.lzp.mybatis.session.MySqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
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
     * 找到sql，通过jdbc执行sql语句，并返回结果
     * 使用代理模式对mapper中方法进行代理，从而实现方法中的逻辑，具体的代理逻辑在mapperproxy类的invoke方法中。
     * 代理逻辑：根据包名+类名+方法名在MyConfiguration中找到对应的sql语句，然后调用MyExcutor执行sql语句，最后通过TypeHandler将结果封装
     *
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
        if (Collection.class.isAssignableFrom(clazz)) {
            mySqlSession.selectList(mapperStatementKey, args[0]);
        } else if (Map.class.isAssignableFrom(clazz)) {
            mySqlSession.selectMap(mapperStatementKey, args[0]);
        } else {
            return mySqlSession.selectOne(mapperStatementKey, null == args ? null : args[0]);
        }
        return null;
    }
}
