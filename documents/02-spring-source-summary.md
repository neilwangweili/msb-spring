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
            > xml、properties、yaml、注解-> BeanDefinition
        
1. **aop**
