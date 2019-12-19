package com.lzp.mybatis.session;

import com.lzp.mybatis.executor.MyExecutor;
import com.lzp.mybatis.mapping.MyConfiguration;

/**
 * Created by LiZhanPing on 2019/12/17.
 */
public class MySqlSessionFactory {

    private MyConfiguration myConfiguration;

    public MySqlSessionFactory(MyConfiguration myConfiguration){
        this.myConfiguration = myConfiguration;
    }

    public MySqlSession openSession() {
        MyExecutor myExecutor = new MyExecutor(myConfiguration);
        return new MySqlSession(this.myConfiguration,myExecutor);
    }
}
