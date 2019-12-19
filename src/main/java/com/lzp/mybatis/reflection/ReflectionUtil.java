package com.lzp.mybatis.reflection;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by LiZhanPing on 2019/12/19.
 */
public class ReflectionUtil {

    public static void setProToBeanFromResult(Object bean, ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();
        Field[] fields = bean.getClass().getDeclaredFields();
        for(int i=0;i<columnCount;i++){
            String columnName = metaData.getColumnName(i + 1).replace("_", "").toUpperCase();
            for(int j=0;j<fields.length;j++){
                String fieldName = fields[j].getName().toUpperCase();
                if(columnName.equalsIgnoreCase(fieldName)){
                    if(fields[i].getType().getSimpleName().equalsIgnoreCase("Integer")){
                        setProToBean(bean,fields[j].getName(),resultSet.getInt(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("Long")){
                        setProToBean(bean,fields[j].getName(),resultSet.getLong(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("Double")){
                        setProToBean(bean,fields[j].getName(),resultSet.getDouble(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("String")){
                        setProToBean(bean,fields[j].getName(),resultSet.getString(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("Date")){
                        setProToBean(bean,fields[j].getName(),resultSet.getDate(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("Boolean")){
                        setProToBean(bean,fields[j].getName(),resultSet.getBoolean(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("BigDecimal")){
                        setProToBean(bean,fields[j].getName(),resultSet.getBigDecimal(metaData.getColumnName(i+1)));
                    } else if(fields[i].getType().getSimpleName().equalsIgnoreCase("Float")){
                        setProToBean(bean,fields[j].getName(),resultSet.getFloat(metaData.getColumnName(i+1)));
                    }
                    break;
                }
            }

        }
    }

    private static void setProToBean(Object bean,String name,Object value){
        try {
            Field field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
