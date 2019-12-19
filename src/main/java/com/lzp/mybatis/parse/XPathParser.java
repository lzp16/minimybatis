package com.lzp.mybatis.parse;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiZhanPing on 2019/12/17.
 */
public class XPathParser {
    private XPath xPath;
    private final Document document;

    public XPath getxPath() {
        return xPath;
    }

    public Document getDocument() {
        return document;
    }

    public XPathParser(InputStream inputStream) {
        this.xPath = createXpath();
        this.document = createDocument(new InputSource(inputStream));
    }

    private XPath createXpath() {
        //共通构造函数，除了把参数都设置到实例变量里面去以外，还初始化了XPath
        XPathFactory factory = XPathFactory.newInstance();
        return factory.newXPath();
    }

    private Document createDocument(InputSource inputSource) {
        // important: this must only be called AFTER common constructor
        try {
            //这个是DOM解析方式
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            factory.setValidating(validation);

            //名称空间
            factory.setNamespaceAware(false);
            //忽略注释
            factory.setIgnoringComments(true);
            //忽略空白
            factory.setIgnoringElementContentWhitespace(false);
            //把 CDATA 节点转换为 Text 节点
            factory.setCoalescing(false);
            //扩展实体引用
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            //需要注意的就是定义了EntityResolver(XMLMapperEntityResolver)，这样不用联网去获取DTD，
            //将DTD放在org\apache\ibatis\builder\xml\mybatis-3-config.dtd,来达到验证xml合法性的目的
//            builder.setEntityResolver(entityResolver);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new RuntimeException("Error creating document instance.  Cause: " + e, e);
        }
    }


    public String evalString(String expression) {
        return (String) evaluate(expression, document, XPathConstants.STRING);
    }


    public Boolean evalBoolean(String expression) {
        return (Boolean) evaluate(expression, document, XPathConstants.BOOLEAN);
    }

    //返回节点
    public Node evalNode(String expression) {
        return (Node) evaluate(expression, document, XPathConstants.NODE);
    }

    private Object evaluate(String expression, Object root, QName returnType) {
        try {
            //最终合流到这儿，直接调用XPath.evaluate
            return xPath.evaluate(expression, root, returnType);
        } catch (Exception e) {
            throw new RuntimeException("Error evaluating XPath.  Cause: " + e, e);
        }
    }

}
