package org.springframework.circle;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Neil Wang
 * @version 1.0.0
 * @date 2021/5/9 8:19 下午
 */
public class AFactoryBean implements FactoryBean<A> {
    @Override
    public A getObject() throws Exception {
        return new A();
    }

    @Override
    public Class<?> getObjectType() {
        return A.class;
    }
}
