package com.lzp;

import com.lzp.dao.UserMapper;
import com.lzp.entity.User;
import com.lzp.mybatis.io.MyResources;
import com.lzp.mybatis.session.MySqlSession;
import com.lzp.mybatis.session.MySqlSessionFactory;
import com.lzp.mybatis.session.MySqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 该minimybatis是仿照mybatis3.5.1版本的逻辑实现的，只是实现了部分主要逻辑以便于理解mybatis的运行机理
 *
 * Created by LiZhanPing on 2019/12/16.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        //1、读取SqlMapConfig.xml配置文件,实现方式是将文件转换成流
        InputStream inputStream = MyResources.getResourceAsStream("SqlMapConfig.xml");

        //2、构建MySqlSessionFactory，由于根据配置文件构建MySqlSessionFactory是一个复杂的操作，这里采用了构造者模式，
        //   使用MySqlSessionFactoryBuilder辅助构建MySqlSessionFactory,而从文件中读取信息是一个耗时的工作，将文件
        //   转换为一个对象存储到内存无疑是一个更好的方式，最终MySqlSessionFactory包含了一个指向MyConfiguration对象的引用。
        //
        //   配置文件包含了很多内容，讲一个配置文件解析为对象也是一个复杂的操作，故这里也采用构造者模式，
        //   使用MyXmlConfigBuilder辅助构建MyConfiguration对象，其实MyXmlConfigBuilder只是固定了配置文件的解析流程，
        //   解析工作还要交给下一层MyXPathParser类，而MyXPathParser真正做的事情其实是将字符串结果转换为常用的数据类型，
        //   真正从读取xml配置文件中读取数据内容的操作是MyXPathParser调用jdk底层的xpath负责的，不过需要先将流转换为Document。
        //
        //   这里采用了构造者模式，构造者模式的作用就是固话创建对象步骤从而简化复杂对象的构建。
        MySqlSessionFactory sqlSessionFactory = new MySqlSessionFactoryBuilder().build(inputStream);

        //3、打开MySqlSession，一个会话就是一个系统对一个数据库的一系列的操作的过程，很显然一个系统不可能只有一个会话，故这里使用了工厂模式
        //   首先通过上边的解释，我们可以很明显的意识到MySqlSession需要一个操作数据库的工具，这里我们将它命名为MyExecutor，
        //   其次操作数据库需要知道数据库的连接信息和需要执行的sql语句，这些信息封装在MyConfiguration的MyEnvironment和MapperStatement中，所以它还包含了一个指向MyConfiguration对象的引用
        //   接着会话还要为每一个操作数据库的动作指定接口以供使用者调用，无疑mapper接口就是做这个工作的，故MySqlSession需要给外界提供一个获取Mapper的方法
        //   mapper是如何会话产生关联的呢？，这个接口通过动态代理将MyExecutor关联到Mapper接口上，同时通过使用泛型实现了接口的通用性
        //   最后MySqlSession还需要获取mapper的方法、执行数据的方法和管理事务的方法，如getmapper，增，删，改，查，提交，回滚等
        //
        //   MyExecutor是一个操作数据库的工具，所以他应该包含一个指向连接池对象的引用以便获取connection，
        //   这个连接池就是MyDataSource，它实现了DataSource接口，主要是提供和回收连接
        //   MyExecutor封装了jdbc操作，提供query和update操作，这些操作主要包含步骤：获取连接、预编译、设置参数（包含调用参数类型处理器）、执行sql、获取结果、使用反射将resultset转换为java对象（包含调用结果类型处理器）
        //
        //   MapperProxy生成Mapper接口的实现类，实现类的本质工作是通过jdbc执行sql查询数据库返回结果，但这里对sql的执行做了多层包装，
        //   1、使用执行sql的包装对象的key代替sql语句，2、调用打开MySqlSession执行数据库操作，而它的底层是通过MyExecutor实现的，MyExecutor的底层又是通过jdbc实现的
        //
        //   这里为什么还要MySqlSession对MyExecutor的数据库操作再做一层包装呢
        //   MapperProxy主要工作是将请求参数数组转换为map类型，其次是将增删改操作的返回结果按照要求的返回类型进行处理，查询操作不对返回结果进行处理，但会判断是否进行返回行数限定处理。
        //   然后把通过key获取MapperStatement的工作和数据库操作交给了MySqlSession处理，而MySqlSession对MyExecutor的数据库操作细分为了增删改查操作，同时也对MyExecutor的commit操作做了进一步包装,
        //   这都是为了业务逻辑更清晰方便开发人员操作接口，所以mybatis支持两种开发模式：原始dao开发和mapper代理开发
        MySqlSession session = sqlSessionFactory.openSession();

        //4、获取Mapper接口对象，通过MySqlSession动态代理生成Mapper接口的实现类
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //5、操作Mapper接口对象的方法操作数据库
        for (int i = 0; i < 20; i++) {
            User user = userMapper.findById(1);
            //6、业务处理
            System.out.println("name:" + user.getName() + ",age:" + user.getAge());
        }
    }



}
