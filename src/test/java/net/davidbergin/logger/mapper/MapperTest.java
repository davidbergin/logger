package net.davidbergin.logger.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.davidbergin.logger.format.Activity;
import net.davidbergin.logger.format.BasicOutputFormat;
import net.davidbergin.logger.mapping.BasicOutputMapper;

@SpringBootTest
public class MapperTest {

    @Test
    public void shouldRespectSingletonPattern() {
        BasicOutputMapper one = BasicOutputMapper.instance();
        BasicOutputMapper two = BasicOutputMapper.instance();
        assertEquals(one, two);
    }

    @Test
    public void mapperShouldProduceExpectedJSONFormat() {
        BasicOutputMapper one = BasicOutputMapper.instance();
        Activity activity = new Activity("dmb", "github.com", "Viewing", 0, new Date(121, 01, 18), 37);
        String json = one.map(activity).toJSONString();
        String expected = "{\"user\":\"dmb\",\"website\":\"github.com\",\"activityTypeDescription\":\"Viewing\",\"signedInTime\":\"2021-02-18 00:00:00\"}";
        assertEquals(expected, json);
    }

    @Test
    public void mapperShouldProduceDifferentOutputInstancesEachTime() {
        BasicOutputMapper one = BasicOutputMapper.instance();
        Activity activity = new Activity("dmb", "github.com", "Viewing", 0, new Date(121, 01, 18), 37);
        BasicOutputFormat output = one.map(activity);
        BasicOutputFormat other = one.map(activity);
        assertNotEquals(output, other);
    }
    
}
