package org.springframework.details;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Neil Wang
 * @version 1.0.0
 * @date 2021/5/26 5:20 下午
 */
public class SpringTest {

    @BeforeEach
    void setUp() {
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring-${username}.xml");
    }
}
