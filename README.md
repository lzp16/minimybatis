## 阅读框架源码
### 1、指导思想
##### 1).寻找入口
##### 2).断点跟踪
##### 3).先粗后细
##### 4).精略结合
### 2、前期准备
##### 1).构建框架源码工程  可以从github中获取源码
##### 2).构建使用框架源码工程的极简用例工程用于调试 使用框架的用例工程越简单越容易发现框架的核心  可以参考我的usemybatis工程，该工程没有使用源码工程需要改造
##### 3).准备好框架文档  mybatis的官方文档：https://mybatis.org/mybatis-3/zh/index.html
##### 4).想要阅读源码少走弯路的话则最好做到上边的准备
### 3、.构建使用框架源码工程的用例工程
## Mybatis核心原理
### 1、mybatis流程
![](https://github.com/lzp16/minimybatis/blob/master/src/main/resources/images/mybatis-process.png)
##### 1).读取 MyBatis 配置文件：mybatis-config.xml 为 MyBatis 的全局配置文件，配置了 MyBatis 的运行环境等信息，例如数据库连接信息。
##### 2).加载映射文件。映射文件即 SQL 映射文件，该文件中配置了操作数据库的 SQL 语句，需要在 MyBatis 配置文件 mybatis-config.xml 中加载。mybatis-config.xml 文件可以加载多个映射文件，每个文件对应数据库中的一张表。
##### 3).构造会话工厂：通过 MyBatis 的环境等配置信息构建会话工厂 SqlSessionFactory。
##### 4).创建会话对象：由会话工厂创建 SqlSession 对象，该对象中包含了执行 SQL 语句的所有方法。
##### 5).Executor 执行器：MyBatis 底层定义了一个 Executor 接口来操作数据库，它将根据 SqlSession 传递的参数动态地生成需要执行的 SQL 语句，同时负责查询缓存的维护。
##### 6).MappedStatement 对象：在 Executor 接口的执行方法中有一个 MappedStatement 类型的参数，该参数是对映射信息的封装，用于存储要映射的 SQL 语句的 id、参数等信息。
##### 7).输入参数映射：输入参数类型可以是 Map、List 等集合类型，也可以是基本数据类型和 POJO 类型。输入参数映射过程类似于 JDBC 对 preparedStatement 对象设置参数的过程。
##### 8).输出结果映射：输出结果类型可以是 Map、 List 等集合类型，也可以是基本数据类型和 POJO 类型。输出结果映射过程类似于 JDBC 对结果集的解析过程。
### 2、mybatis核心对象
![](https://github.com/lzp16/minimybatis/blob/master/src/main/resources/images/mybatis-component-relation.png)
##### 1).SqlSessionFactoryBuilder（构造器）：它会根据配置或者代码来生成 SqlSessionFactory，采用的是分步构建的 Builder 模式。  
##### 2).SqlSessionFactory（工厂接口）：依靠它来生成 SqlSession，使用的是工厂模式。
##### 3).SqlSession（会话）：一个既可以发送 SQL 执行返回结果，也可以获取 Mapper 的接口。在现有的技术中，一般我们会让其在业务逻辑代码中“消失”，而使用的是 MyBatis 提供的 SQL Mapper 接口编程技术，它能提高代码的可读性和可维护性。
##### 4).SQL Mapper（映射器）:MyBatis 新设计存在的组件，它由一个 Java 接口和 XML 文件（或注解）构成，需要给出对应的 SQL 和映射规则。它负责发送 SQL 去执行，并返回结果。
##### 5).Configuration（配置）：参数处理，结果集处理，SQL与mapper对照关系，数据库连接信息等等进行配置化
##### 6).executor：用于操作底层的jdbc（如释放数据库连接），被sqlsession持有
## 相关文档
* http://c.biancheng.net/mybatis/
