package com.lzp.mybatis.pool;

import com.lzp.mybatis.mapping.MyEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiZhanPing on 2019/12/18.
 */
public class MyDataSource implements MyDataSourceInterface {

    private MyEnvironment myEnvironment;

    private List<Connection> pool;

    private Connection connection = null;

    private static MyDataSource instance = null;

    private static final int POOL_SIZE = 15;

    /**
     * 单例模式
     */
    private MyDataSource(MyEnvironment myEnvironment) {
        this.myEnvironment = myEnvironment;
        pool = new ArrayList<>(POOL_SIZE);
        this.createConnection();
    }

    public static MyDataSource getInstance(MyEnvironment myEnvironment) {
        if (instance == null) {
            instance = new MyDataSource(myEnvironment);
        }
        return instance;
    }

    private void createConnection() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                Class.forName(myEnvironment.getDriver());
                connection = DriverManager.getConnection(myEnvironment.getUrl(), myEnvironment.getUsername(), myEnvironment.getPassword());
                pool.add(connection);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void closePool() {
        for (int i = 0; i < pool.size(); i++) {
            try {
                connection = pool.get(i);
                connection.close();
                pool.remove(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从连接池中获取一个连接
     *
     * @return
     */
    public synchronized Connection getConnection() {
        if (pool.size() > 0) {
            Connection connection = pool.get(0);
            pool.remove(connection);
            return connection;
        } else {
            return null;
        }
    }

    public synchronized void release(Connection connection) {
        pool.add(connection);
    }
}
