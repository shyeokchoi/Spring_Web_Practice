package com.wemade.pconsole;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.wemade.board.common.utils.RestUtil;
import com.wemade.board.framework.config.RestTemplateConfig;

//@ActiveProfiles("local")
@SpringBootTest(classes = { RestTemplateConfig.class, RestUtil.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanTest {

    // @Test
    public void testLiveCount() {

    }

}
