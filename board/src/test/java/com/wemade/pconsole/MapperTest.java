package com.wemade.pconsole;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

import com.wemade.board.mapper.SampleMapper;

/**
 * mapper 테스트
 *
 * @author youngrok.kim
 *
 */
//@Slf4j
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@MybatisTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class MapperTest {

    @Autowired
    private SampleMapper sampleMapper;

    @Test
    void test() {
        //test3();

        // System.err.println("\n\n\n\n\n");

        // test2();
    }

}
