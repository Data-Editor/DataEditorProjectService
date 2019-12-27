package com.niek125.projectproducer.handlers;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.projectproducer.models.DataHeader;

public class DataHandler {

    public boolean validate(String data) {
        final DocumentContext doc = JsonPath.parse(data);
        if(!((doc.read("$.headers", DataHeader[].class).length > 0) &&
                (doc.read("$.items", Object[].class).length > 0))){
            return false;
        }
        return true;
    }
}
