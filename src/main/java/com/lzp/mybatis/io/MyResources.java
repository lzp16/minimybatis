package com.lzp.mybatis.io;

import java.io.InputStream;

/**
 * Created by LiZhanPing on 2020/2/8.
 */
public class MyResources {

    public static InputStream getResourceAsStream(String resource){
        return ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
    }
}
