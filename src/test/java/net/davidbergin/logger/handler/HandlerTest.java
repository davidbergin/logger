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

    @Test
    public void jsonHandlingIsAsExpected() {
        String json = "{\"activity\" : {\"userName\" : \"Sam\",\"websiteName\" : \"abc.com\",\"activityTypeDescription\" : \"Viewed\",\"signedInTime\" : \"01/13/2020\"}}";
        Handler jsonHandler = HandlerFactory.getInstance("test.json");
        String result = jsonHandler.handle(json);
        String expected = "{\"user\":\"Sam\",\"website\":\"abc.com\",\"activityTypeDescription\":\"Viewed\",\"signedInTime\":\"2020-01-13 00:00:00\"}";
        assertEquals(expected, result);
    }


    @Test
    public void xmlHandlingIsCorrectForValidXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><activity><userName>Williamson</userName><websiteName>xyz.com</websiteName><activityTypeCode>002</activityTypeCode><loggedInTime>2020-01-13</loggedInTime><number_of_views>10</number_of_views></activity>";
        Handler xmlHandler = HandlerFactory.getInstance("test.xml");
        String result = xmlHandler.handle(xml);
        String expected = "{\"user\":\"Williamson\",\"website\":\"xyz.com\",\"activityTypeDescription\":\"Purchased\",\"signedInTime\":\"2020-01-13 00:00:00\"}";
        assertEquals(expected, result);
    }

    @Test
    public void xmlHandlingIsCorrectForInValidXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><activity><websiteName>xyz.com</websiteName><activityTypeCode>002</activityTypeCode><loggedInTime>2020-01-13</loggedInTime><number_of_views>10</number_of_views></activity>";
        Handler xmlHandler = HandlerFactory.getInstance("test.xml");
        String result = xmlHandler.handle(xml);
        assertNull(result);
    }
    
}
