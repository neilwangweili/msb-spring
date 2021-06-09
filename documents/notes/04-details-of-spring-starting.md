# details-of-spring-starting

## 启动

1. ClassPathXmlApplicationContext的构造方法调用了父类构造方法->
    1. AbstractApplicationContext的构造方法
        1. 创建日志
        1. 分配唯一标识Id值(后面使用的id就是该id)
        1. 创建beanFactoryPostProcessors集合
        1. 创建容器active激活/未激活状态标识
        1. 创建容器closed是否关闭标识
        1. 创建refresh()方法上加的同步监视器锁
        1. this.resourcePatternResolver = getResourcePatternResolver()创建资源模式处理器,用来解析当前系统需要运行的资源
            1. 日志
            1. private PathMatcher pathMatcher = new AntPathMather()
                1. 创建ant方式的路径匹配器
               > ant:一种表达式的匹配方式<br/>
               /app/*.x<br/>
               app/p?ttern<br/>
               / **/example<br/>
               aop运用的表达式就是ant
            1. 继承于ResourceLoader 资源加载器
                1. 可以根据前缀解析、匹配
        1. 判断其父容器是否为空，若否则将父容器中的环境变量继承下来
        1. private boolean validating = true;
            1. 设置xml文件的验证方式，默认为true
    1. setConfigLocations(configLocations)
        1. 将xml文件设置到成员对象里，方便后续直接调用
        1. resolvePath对当前路径进行处理工作
            1. 检查是否存在
            1. 获得环境，按照espl表达式替换(创建placeHolderHelper进行递归替换)
                1. customizePropertySources(this.propertySources)
                    1. 个性化属性资源
                        1. spring.getenv.ignore
                        1. spring.getenv.active
                        1. spring.getenv.default
                        1. default
                1. 将systemEnvironment和systemProperties封装为List
                1. springmvc、springboot对其进行了增强属性配置
            1. springboot中有spring.active.profile设置 原因如上
    1. 根据refresh标识判断是否需要refresh 默认为true
    1. refresh方法
        1. 同步监视器
        1. prepareRefresh()
            1. initPropertySources
                1. 初始化任何资源属性，留给子类覆盖
                1. 可以扩展 - MyClassPathXmlApplicationContext
                1. WebApplicationContextUtils#initServletPropertySources()进行了扩展操作
        1. obtainFreshBeanFactory()
            1. refreshBeanFactory()
                1. 如果有bean工厂，则销毁掉
                1. 创建一个新的bean工厂
                1. AbstractAutowireCapableBeanFactory#ignoreDependencyInterface
                    1. 注册入缓存，过后统一处理
            1. customizeBeanFactory
                1. 如果属性allowBeanDefinitionOverriding不为空，设置给beanFactory对象相应属性，是否允许覆盖同名称的不同定义的对象
                1. 如果属性allowCircularReferences不为空，设置给beanFactory，是否允许bean之间存在循环依赖(重写来修改规则)
## doLoadBeanDefinitions(beanFactory)
1. bean定义信息一开始是存储在xml中的，将工厂类当作参数传入，设置环境参数、Xsd解析器(读一个资源处理器(bean dtd处理器 xsd处理器))
1. 运用设计模式:适配器模式XmlBeanDefinitionReader()并设置
   1. PluggableSchemaResolver的schemaMappings不在创建时加载，而是懒加载，在toString时将值加载，所以debug时可以看到该值
1. 将beanFactory read入beanDefinition(后续工作)
    1. getConfigLocations -> AbstractRefreshableConfigApplicationContext#getConfigLocations