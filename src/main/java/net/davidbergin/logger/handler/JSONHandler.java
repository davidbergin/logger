/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.davidbergin.logger.format.ActivityWrapper;
import net.davidbergin.logger.mapping.BasicOutputMapper;
import net.davidbergin.logger.util.Trace;

/**
 * Implementation of Handler for basic JSON files.
 * Does not support validation, so this always succeeds.
 * Maps the input to output using BasicOutputMapper.
 */
public class JSONHandler implements Handler {

    @Override
    public boolean validate(String content) {
        return true; // JSON validation not supported
    }

    /**
     * Transforms the input JSON to the output format.
     * @param content the input JSON.
     * @return the transformed output JSON.
     */
    @Override
    public String transform(String content) {

        Trace.info(JSONHandler.class, "Transforming content - " + content);

        final Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
        final ActivityWrapper wrapper = gson.fromJson(content, ActivityWrapper.class);

        final String output = BasicOutputMapper.instance().map(wrapper.getActivity()).toJSONString();
        Trace.info(XMLHandler.class, "Transformed to - " + output);

        return output;

    }

}
