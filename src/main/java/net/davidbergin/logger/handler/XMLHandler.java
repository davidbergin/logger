/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.handler;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

import org.xml.sax.SAXException;

import net.davidbergin.logger.Processor;
import net.davidbergin.logger.format.Activity;
import net.davidbergin.logger.mapping.BasicOutputMapper;
import net.davidbergin.logger.util.Config;
import net.davidbergin.logger.util.Trace;

/**
 * Implementation of Handler for basic XML files. Validates against an XSD
 * schema, if configured. Maps the input to output using BasicOutputMapper.
 */
public class XMLHandler implements Handler {

    private static final String SCHEMA_PROPERTY_NAME = "logger.schema.xml";
    private final Schema schema;

    public XMLHandler() throws SAXException {

        // Cache the schema grammar, as it is immutable and will provide
        String schemaFile = Config.instance().getString(SCHEMA_PROPERTY_NAME);
        if (schemaFile != null && schemaFile.length() > 0) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = factory.newSchema(Processor.class.getClassLoader().getResource(schemaFile));
        } else {
            schema = null;
            Trace.warn(XMLHandler.class, "No schemaFile was defined.");
        }
    }

    /**
     * Validates the input content against a schema.
     * 
     * @param content the input format.
     * @return true if the content passed validation, false otherwise.
     * @throws Exception if an error occurred.
     */
    @Override
    public boolean validate(String content) throws SAXException, IOException {

        if (schema != null) {
            Validator validator = schema.newValidator();// not thread safe so must always create a new one
            validator.validate(new StreamSource(new StringReader(content)));
        }

        return true; // if no schema is configured, assume that file passes validation

    }

    /**
     * Transforms the input XML to the output format.
     * 
     * @param content the input XML.
     * @return the transformed output JSON.
     */
    @Override
    public String transform(final String content) {

        Trace.info(XMLHandler.class, "Transforming content - " + content);

        final XStream xstream = new XStream();
        xstream.alias("activity", Activity.class);
        xstream.aliasField("loggedInTime", Activity.class, "signedInTime");
        xstream.registerConverter(new DateConverter("yyyy-MM-dd", null));
        final Activity activity = (Activity) xstream.fromXML(content);

        final String output = BasicOutputMapper.instance().map(activity).toJSONString();
        Trace.info(XMLHandler.class, "Transformed to - " + output);

        return output;

    }

}
