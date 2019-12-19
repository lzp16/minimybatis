package com.lzp.mybatis.session;

import com.lzp.mybatis.mapping.MyConfiguration;
import com.lzp.mybatis.parse.XmlConfigBuilder;

import java.io.InputStream;

/**
 * Created by LiZhanPing on 2019/12/17.
 */
public class MySqlSessionFactoryBuilder {
    public MySqlSessionFactory build(InputStream inputStream) {
        //mybatis的配置对象，mybatis的配置信息
        MyConfiguration myConfiguration = new XmlConfigBuilder(inputStream).parse();
        return new MySqlSessionFactory(myConfiguration);
    }
}
