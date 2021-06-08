package org.springframework;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Neil Wang
 * @version 1.0.0
 * @date 2021/6/8 11:28 上午
 */
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    public MyClassPathXmlApplicationContext(String... configLocations) {
        super(configLocations);
    }

    @Override
    protected void initPropertySources() {
        System.out.println("扩展initPropertySource");
        //The following properties were declared as required but could not be resolved: [abc]
        //getEnvironment().setRequiredProperties("abc");
        getEnvironment().setRequiredProperties("user.name");
        super.initPropertySources();
    }
}
