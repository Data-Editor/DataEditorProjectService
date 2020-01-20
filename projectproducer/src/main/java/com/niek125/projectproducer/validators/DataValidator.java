package com.niek125.projectproducer.validators;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.projectproducer.models.DataHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataValidator implements Validator<String> {
    private final Logger logger = LoggerFactory.getLogger(DataValidator.class);

    public boolean validate(String data) {
        final DocumentContext doc = JsonPath.parse(data);
        if(!((doc.read("$.headers", DataHeader[].class).length > 0) &&
                (doc.read("$.items.length()", Integer.class) > 0))){
            logger.info("data has incorrect format");
            return false;
        }
        return true;
    }
}
