package net.davidbergin.logger.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HandlerTest {

    @Test
    public void shouldGetCorrectHandlerForXML() {
        Handler one = HandlerFactory.getInstance("one.xml");
        assertEquals(XMLHandler.class, one.getClass());
    }

    @Test
    public void shouldGetCorrectHandlerForJSON() {
        Handler one = HandlerFactory.getInstance("one.json");
        assertEquals(JSONHandler.class, one.getClass());
    }

    @Test
    public void shouldGetSameHandlerForSameFileType() {
        Handler one = HandlerFactory.getInstance("one.xml");
        Handler two = HandlerFactory.getInstance("TWO.XML");
        assertEquals(one, two);
    }

    @Test
    public void shouldGetNullForUnsupportedFile() {
        Handler one = HandlerFactory.getInstance("one.yml");
        assertNull(one);
    }

    @Test
    public void shouldGetNullForBlankFile() {
        Handler one = HandlerFactory.getInstance("");
        assertNull(one);
    }

    @Test
    public void shouldGetNullForFileWithNoExtension() {
        Handler one = HandlerFactory.getInstance("hello");
        assertNull(one);
    }
    
}
