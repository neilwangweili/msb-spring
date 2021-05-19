# 03-debug-spring-refresh

## 详解AbstractApplicationContext#refresh()方法

### 细节：父类一定要关注

### prepareRefresh()

> **前戏**,做容器刷新前的准备工作
> 1. 设置容器启动时间
> 1. 设置活跃状态为true
> 1. 设置关闭状态为false(原子操作标志位)
> 1. 获取Environment对象，并加载当前系统的属性值到Environment对象中
> 1. 准备监听器和事件的集合对象，默认为空集合

1. 一些最基本的准备工作
    1. 创建开启时间
    1. 设置一些标志-关闭状态设置为false，活跃状态设置为true
    1. 开启日志
    1. 初始化属性资源(默认不用管，可扩展)
    1. 获取环境(getEnvironment())
        1. new StandardEnvironment() 调用父类方法AbstractEnvironment#Construction()
            1. customizePropertySources() 定制化属性资源，调用子类方法"propertySources.addLast(...)
    1. 验证获取的环境资源
    1. Store pre-refresh Application Listeners
        1. 存储预刷新的监听器 - 创建监听器、事件集合
        1. 为了方便做扩展实现的。（springBoot就扩展了）

### ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory()

> 创建容器对象DefaultListableBeanFactory
> 加载xml配置文件的属性值至工厂中，最重要的就是BeanDefinition
> 创建并获得当前的工厂需要了解三个Factory
> 1. ListableBeanFactory
> 1. ConfigurableBeanFactory
> 1. AutowireCapableBeanFactory
     > 自定义BeanFactory只需要实现HierarchicalBeanFactory & ListableBeanFactory

1. 若有工厂，销毁并关闭。然后创建工厂
1. AbstractRefreshableApplicationContent#createBeanFactory()创建DefaultListableBeanFactory对象工厂
1. 对BeanFactory设置序列化id
1. 定制化BeanFactory
    1. 设置属性值
        - lookup-method
        - replace-method
        - 是否允许被覆盖和重写
        - ...
1. loadBeanDefinitions(beanFactory)
    1. 加载BeanDefinitions
        1. AbstractXmlApplicationContext#loadBeanDefinitions(reader) -> loadBeanDefinitions(configLocations)
            1. configLocations:
                1. 创建ApplicationContext时设置这个值(ClassPathXmlApplicationContext#setConfigLocations())
            1. for() -> AbstractBeanDefinitionReaders.loadBanDefinitions
                1. 因为是循环读取，所以要走不同的各种重载方法，很复杂
    1. 有几个配置文件的Bean，就创建几次,数量=beanDefinitionMap、beanDefinitionNames
   > RootBeanDefinition#GenericBeanDefinition中涉及到Bean的合并，以后再说
   > getMergedBeanDefinition()
   > 这个方法过于复杂，以后再详细概述
    3. 在BeanDefinition定义信息中封装了需要的属性值
1. this.beanFactory = beanFactory;
1. 构造完成

### prepareBeanFactory(beanFactory)

> **前戏** , 对当前对Bean工厂设置某些初始化环境的值

1. beanFactory.setBeanClassLoader 类加载器
1. beanFactory.setBeanExpressionResolver spEl表达式解析器
1. beanFactory.addPropertyEditorRegistrar Bean属性编辑器
1. beanFactory.addBeanPostProcessor Bean增强器
1. beanFactory.ignoreDependencyInterface 不解析特定的Aware接口，忽略对应接口的实现
1. beanFactory.registerResolvableDependency 处理添加一些特定的依赖
1. beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this)) 添加Bean增强器
1. ... 依次逐步注册值

### postProcessBeanFactory(beanFactory)

> 注册后置处理器

1. BeanFactoryPostProcessor#postProcessBeanFactory(beanFactory)
    - 没有什么实现类，可以由用户自定义拓展

### invokeBeanFactoryPostProcessors(beanFactory)

> 调用、执行BeanFactoryPostProcessor(可以随意修改Bean定义信息)

1. AbstractApplicationContext#invokeBeanFactoryPostProcessors
    1. 实例化并执行所有的BeanFactoryPostProcessor
    1. 必在单例对象创建前执行(不在创建前执行，修改Bean定义信息就没什么意义了)
    1. 处理PriorityOrdered.class
    1. 处理Ordered.class
    1. 执行排序的BeanFactoryPostProcessors
    1. 执行未排序的BeanFactoryPostProcessors(@Ordered相关)

### registerBeanPostProcessors(beanFactory)

> 注册Bean监听处理器

1. AbstractApplicationContext#registerBeanPostProcessors(beanFactory)
    1. 实例化并注册所有的BPP实例，由用户进行扩展

### initMessageSource()

> 初始化国际化

### initApplicationEventMulticaster()

> 初始化容器广播器

### onRefresh()

> 即将注册监听器，留给子类做相关拓展工作，例如springBoot就在这里启动了tomcat

1. AbstractApplicationContext#onRefresh() 空方法

### registerListeners()

> 注册监听器

1. AbstractApplicationContext#registerListeners()
    1. 获取所有监听器并注册至Factory

### finishBeanFactoryInitialization(beanFactory)

> 实例化所有剩下的非懒加载的单例对象

1. 将类型转换操作器注册至容器
1. 将beanFactory中内置的值处理器(例如${})注册至容器
1. 将支路Aware注册至容器
1. 设置临时ClassLoader
1. 暂时暂停配置，如果某些Bean配置不允许修改了，可以放进该方法
1. 实例化所有剩下的非懒加载的单例对象
    1. 获取所有bean名称循环操作：
        1. RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName)
            1. 如果当前bean有父类关联，则同时注册父类的bean
            1. 例如abstract属性指定抽象Bean
        1. 判断非抽象、单例、非懒加载的bean进行处理
        1. 如果是Bean工厂，则窗前该工厂想要创建的Bean的对象
            1. doGetBean()
                1. getSingleton
                    1. 判断一级缓存中是否有该对象，有则直接获得单例bean
                    1. 当对象都是单例的时候会尝试解决循环依赖问题，但是原型模式下若有循环依赖问题，抛出异常(原型对象无法解决循环依赖问题)
                    1. 创建对象并将其标志为正在创建中
                    1. 获取并检测BeanDefinition
                    1. 检测是否有其他依赖Bean，有则递归创建
                    1. 注册各个Bean的依赖关系，方便进行销毁
                    1. 创建bean的实例对象
                        1. createBean(beanName, mbd, args)
                            1. 从一级缓存中拿
                            1. 开始bean对象的创建 singleTonFactory.getObject()
                            1. doCreateBean()
                                1. 核心方法
                                1. createBeanInstance
                                    1. 获得构造器
                                    1. instantiateBean
                                        1. 获取实例化策略(Cglib、jdk)
                                        1. instantiate
                                            1. instantiateClass
                                                1. 通过反射、实例化策略调用构造器进行实例化
                                        1. 创建完成后，对象属性都是默认值
                                1. 解决当前循环依赖问题(循环依赖是初始化的时候产生的，所以实例化、初始化需要分开)
                                    1. 提前暴露(暴露只完成实例化但未完成初始化的操作)
                                        1. 向三级缓存中放入循环依赖的问题对象
                                       > 为什么要使用三级缓存？ 解决循环依赖问题。
                                       > 当对象需要代理的时候必须要使用三级缓存
                                       > getEarlyBeanReference() 向问题对象放入默认属性值
                                1. populateBean
                                    1. 填充属性值
                                1. initializeBean
                                    1. invokeAwareMethods()
                                        1. 设置aware接口的属性
                                1. applyBeanPostProcessorsBeforeInitialization
                                    1. 循环调用所有BPP的before方法
                                1. invokeInitMethods
                                    1. 执行init-method方法
                                1. 同Before方法，执行BPP的after方法
                                1. 注册对象对销毁方法
                                1. 获得对象并返回
                            1. 移除缓存中对该bean的创建记录
                        1. getObjectForBeanInstance
                            1. 对FactoryBean进行特殊处理
                            1. getObjetFromFactoryBean->doGetObjectFromFactoryBean
                                1. object = factory.getObject()返回具体FactoryBean中的对象
                       > 如果对象实现FactoryBean，会创建两个Bean
                1. 实例化完成
### finishRefresh()
> 完成整体刷新
1. 移除context-level缓存
1. 初始化生命周期处理器
1. 执行生命周期处理器方法
1. 发布事件、注册应用程序信息
1. 重置其余的缓存
### 创建完成