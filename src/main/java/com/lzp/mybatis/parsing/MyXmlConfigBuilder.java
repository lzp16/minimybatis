package com.lzp.mybatis.parsing;

import com.lzp.mybatis.io.MyResources;
import com.lzp.mybatis.mapping.MapperStatement;
import com.lzp.mybatis.mapping.MyConfiguration;
import com.lzp.mybatis.mapping.MyEnvironment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by LiZhanPing on 2019/12/17.
 */
public class MyXmlConfigBuilder {

    private MyXPathParser myXPathParser;

    public MyXmlConfigBuilder(InputStream inputStream){
        this.myXPathParser = new MyXPathParser(inputStream);
    }

    public MyConfiguration parse(){

        //环境信息
        Properties properties = new Properties();
        Node dataSourceNode = myXPathParser.evalNode("/configuration/environment/dataSource");
        NodeList propertyNodeList = dataSourceNode.getChildNodes();
        for (int i=0;i<propertyNodeList.getLength();i++){
            Node propertyNode = propertyNodeList.item(i);
            if(propertyNode.getNodeType()== Node.ELEMENT_NODE){
                properties.setProperty(propertyNode.getAttributes().getNamedItem("name").getNodeValue(),propertyNode.getAttributes().getNamedItem("value").getNodeValue());
            }
        }

        //Mapper映射文件配置信息
        Map<String,MapperStatement> mapperStatementMap = new HashMap<String, MapperStatement>();
        Node mappersNode = myXPathParser.evalNode("/configuration/mappers");
        NodeList mapperNodeList = mappersNode.getChildNodes();
        for(int i=0;i<mapperNodeList.getLength();i++){
            Node mapperNode = mapperNodeList.item(i);
            if(mapperNode.getNodeType() == Node.ELEMENT_NODE) {
                //mapper.xml文件的位置
                String resource = mapperNode.getAttributes().getNamedItem("resource").getNodeValue();
                //解析该mapper.xml文件
                InputStream inputStream = MyResources.getResourceAsStream(resource);
                this.myXPathParser = new MyXPathParser(inputStream);
                Element element = this.myXPathParser.getDocument().getDocumentElement();
                String namespace = element.getAttribute("namespace");

                NodeList sqlNodeList = element.getChildNodes();
                for (int j = 0; j < sqlNodeList.getLength(); j++) {
                    Node sqlNode = sqlNodeList.item(j);
                    if (sqlNode.getNodeType() == Node.ELEMENT_NODE){
                        String id = "";
                        String parameterType = "";
                        String resultType = "";
                        Node idNode = sqlNode.getAttributes().getNamedItem("id");
                        if(idNode==null){
                            throw new RuntimeException("sql id is null.");
                        } else {
                            id = idNode.getNodeValue();
                        }
                        Node parameterTypeNode = sqlNode.getAttributes().getNamedItem("parameterType");
                        if(parameterTypeNode!=null){
                            parameterType = parameterTypeNode.getNodeValue();
                        }
                        Node resultTypeNode = sqlNode.getAttributes().getNamedItem("resultType");
                        if(resultTypeNode!=null){
                            resultType = resultTypeNode.getNodeValue();
                        }
                        String sql = sqlNode.getTextContent();
                        MapperStatement mapperStatement = new MapperStatement();
                        mapperStatement.setNamespace(namespace);
                        mapperStatement.setId(id);
                        mapperStatement.setParameterType(parameterType);
                        mapperStatement.setResultType(resultType);
                        mapperStatement.setSql(sql);
                        mapperStatementMap.put(namespace+"."+id,mapperStatement);
                    }
                }
            }
        }
        MyConfiguration myConfiguration = new MyConfiguration();

        MyEnvironment myEnvironment = new MyEnvironment();
        myEnvironment.setDriver(properties.getProperty("driver"));
        myEnvironment.setUrl(properties.getProperty("url"));
        myEnvironment.setUsername(properties.getProperty("username"));
        myEnvironment.setPassword(properties.getProperty("password"));

        myConfiguration.setMyEnvironment(myEnvironment);
        myConfiguration.setMapperStatementMap(mapperStatementMap);

        return myConfiguration;
    }
}
