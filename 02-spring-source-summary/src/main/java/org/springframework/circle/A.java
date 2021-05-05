/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.circle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * A demo for test xml DI.
 *
 * @author Neil Wang
 * @version 1.0.0
 */
public class A implements ApplicationContextAware, EnvironmentAware, BeanNameAware {
    private String name;
    private ApplicationContext context;
    private Environment environment;
    private String beanName;

    public void setName(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public ApplicationContext context() {
        return context;
    }

    public Environment environment() {
        return environment;
    }

    public void init() {
        System.out.println("init-method");
    }

    @Override
    public String toString() {
        return "A{" + "name='" + this.name + '\'' + '}';
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public String beanName() {
        return beanName;
    }
}
