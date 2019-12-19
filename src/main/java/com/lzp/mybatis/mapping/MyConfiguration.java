package com.lzp.mybatis.mapping;

import java.util.Map;

/**
 * Created by LiZhanPing on 2019/12/17.
 */
public class MyConfiguration {
    //mybatis-config.xml
    private MyEnvironment myEnvironment;
    //xxMapper.xml
    private Map<String,MapperStatement> mapperStatementMap;

    public MyEnvironment getMyEnvironment() {
        return myEnvironment;
    }

    public void setMyEnvironment(MyEnvironment myEnvironment) {
        this.myEnvironment = myEnvironment;
    }

    public Map<String, MapperStatement> getMapperStatementMap() {
        return mapperStatementMap;
    }

    public void setMapperStatementMap(Map<String, MapperStatement> mapperStatementMap) {
        this.mapperStatementMap = mapperStatementMap;
    }
}
