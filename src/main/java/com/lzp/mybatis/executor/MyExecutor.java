package com.lzp.mybatis.executor;

import com.lzp.mybatis.mapping.MapperStatement;
import com.lzp.mybatis.mapping.MyConfiguration;
import com.lzp.mybatis.parse.SqlTokenParser;
import com.lzp.mybatis.pool.MyDataSource;
import com.lzp.mybatis.reflection.ReflectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiZhanPing on 2019/12/18.
 */
public class MyExecutor {

    private MyDataSource dataSource;

    public MyExecutor(MyConfiguration myConfiguration) {
        this.dataSource = MyDataSource.getInstance(myConfiguration.getMyEnvironment());
    }

    public <T> List<T> query(MapperStatement mapperStatement, Object args) {
        List<T> resultList = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SqlTokenParser.parse(mapperStatement.getSql()));
            if (args instanceof String) {
                statement.setString(1, (String) args);
            } else if (args instanceof Integer) {
                statement.setInt(1, (Integer) args);
            } else if (args instanceof Long) {
                statement.setLong(1, (Long) args);
            } else if (args instanceof Float) {
                statement.setFloat(1, (Float) args);
            } else if (args instanceof Double) {
                statement.setDouble(1, (Double) args);
            }
            resultSet = statement.executeQuery();
            handleResultSet(resultSet, resultList, mapperStatement.getResultType());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                dataSource.release(connection);
            }
        }
        return resultList;
    }

    private <T> void handleResultSet(ResultSet resultSet, List<T> resultList, String resultType) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(resultType);
            while (resultSet.next()) {
                Object bean = clazz.newInstance();
                ReflectionUtil.setProToBeanFromResult(bean, resultSet);
                resultList.add((T) bean);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
