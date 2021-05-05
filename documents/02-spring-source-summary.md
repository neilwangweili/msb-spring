# 02-spring-source-summary
### 什么是spring？
1. 框架
1. 生态
    - 扩展性，最终达到目的：**为所欲为**
### spring组成部分：
1. ioc[必须搞清楚]
    1. 控制反转
    1. DI
1. ioc容器
    1. 过程
        1. 加载xml
        1. 解析xml
        1. 封装BeanDefinition
        1. 实例化
        1. 放入容器中
        1. 从容器中获取
    1. 概述
        1. 存储Bean的方式:Map，相当于使用key取出value
            1. key:string value:Object(by name)
            1. key:Class  value:Object(by type)
            1. key:string value:ObjectFactory(静态工厂/对象工厂)
            1. key:string value:BeanDefinition
            > BeanDefinition:Bean定义信息，存放一个对象创建的所有信息。
              xml、properties、yaml、注解-> BeanDefinition。
              需要一个抽象层提供统一的解析处理 （定义规范，方便扩展 BeanDefinitionReader）
        1. BeanDefinitionReader:
            1. 统一的解析处理
            1. XmlBeanDefinitionReader、PropertiesBeanDefinitionReader、GroovyBeanDefinitionReader
        1. 实例化
            1. 构建方式：**反射**          好处：可以获得当前类中的所有注解、属性、方法、构造器
            1. scope有singleton、protocol、request、session.默认使用singleton
                1. 获取Class
                    1. 获取Class  Class.forName("完全限定名")
                    1. Object.getClass()
                    1. 类名.class()
                1. Object obj = Clazz.getConstructor().getInstance()
        1. BeanFactory
            1. Bean工厂、整个容器的根接口、整个容器的入口
            1. 详见BeanFactory
        1. aware接口到底是什么？
            1. 标记超级接口，指示Spring容器可以通过回调样式方法将bean通知给特定的框架对象。 实际的方法签名是由各个子接口确定的，但通常应仅由一个接受单个参数的void返回方法组成
            1. 由invokeAwareMethods进行属性值的设置
            1. 用途：当Spring容器创建当Bean对象在进行具体操作时，如果需要容器的其他对象，此时可以将对象实现Aware接口，来满足当前的需要
        1. 创建对象的过程
            1. 实例化
            1. 在堆中开辟一块空间
            1. 对象的属性都是默认值
            1. 初始化
            1. 给属性设置值
            1. 填充属性
            1. [可选]设置Aware接口、Environment接口
            1. BeanPostProcessor.before(aop AbstractAutoProxyCreator继承了这个类)
            1. 执行初始化方法init-method
            1. BeanPostProcessor.after
            1. 完整对象创建完成，context.getBean()可以获取
        1. SpringBean
            1. 分类
                1. 普通对象(我们自定义的需要的对象) 
                1. 容器对象(容器内置的对象,Spring需要的对象 见refreshBeanFactory[创建工厂]、invokeBeanFactoryPostProcessors[aop容器也在这里创建的])
        1. 在不同的阶段要处理不同的工作，应该怎么办？
           
            **观察者模式：监听器、监听事件、多播器（广播器）**
    1. 在程序运行过程中，我们该如何改变bean的信息？
        1. BeanFactoryPostProcessor 后置处理增强器-增强BeanDefinition信息
        > 区别于BeanPostProcessor：增强bean信息
       
        1.eg. PlaceHolderConfigurerSupport : spEl表达式的解析。详见代码：02-spring-source-summary
1. **aop**