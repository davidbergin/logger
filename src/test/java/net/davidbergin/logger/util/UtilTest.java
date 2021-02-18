package net.davidbergin.logger.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilTest {

    @Test
    public void shouldRespectSingletonPattern() {
        Config one = Config.instance();
        Config two = Config.instance();
        assertEquals(one, two);
    }

    @Test
    public void shouldFindExpectedMapping() {
        String answer = Config.instance().getString("logger.handler.json");
        assertEquals("net.davidbergin.logger.handler.JSONHandler", answer);
    }

    @Test
    public void shouldNotFindUnexpectedMapping() {
        String answer = Config.instance().getString("sweet.old.world");
        assertNull(answer);
    }

    @Test
    public void shouldReturnStringDefaultWithUnexpectedMapping() {
        String answer = Config.instance().getString("sweet.old.world","this sweet old world");
        assertEquals("this sweet old world", answer);
    }

    @Test
    public void shouldFindExpectedMappingAndNotDefault() {
        String answer = Config.instance().getString("logger.handler.json","DefinitelyNot");
        assertEquals("net.davidbergin.logger.handler.JSONHandler", answer);
    }

    @Test
    public void shouldFindExpectedMappingAndNotIntegerDefault() {
        int answer = Config.instance().getInteger("logger.input.threads",123);
        assertEquals(10, answer);
    }    
}
