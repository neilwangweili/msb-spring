package org.springframework.details;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.MyClassPathXmlApplicationContext;
import org.springframework.beans.factory.BeanDefinitionStoreException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Neil Wang
 * @version 1.0.0
 * @date 2021/5/26 5:20 下午
 */
public class SpringTest {

    MyClassPathXmlApplicationContext ac;

    @BeforeEach
    void setUp() {
        ac = new MyClassPathXmlApplicationContext("application.xml");
    }

    @Test
    void should_throw_ex_when_replace_placeholder_one_file() {
        Throwable throwable = catchThrowable(() -> new MyClassPathXmlApplicationContext("spring-${user.name}.xml"));
        assertThat(throwable).isInstanceOf(BeanDefinitionStoreException.class)
                .hasMessage("IOException parsing XML document from class path resource [spring-wangweili.xml]; " +
                        "nested exception is java.io.FileNotFoundException: class path resource [spring-wangweili.xml] cannot be opened because it does not exist");
    }
}
