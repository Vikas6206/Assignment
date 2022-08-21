package com.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import utils.Param;

import java.io.IOException;
import java.util.ArrayList;

public class KeyBase {
    protected ArrayList<Param> setHeader() {
        ArrayList<Param> headers = new ArrayList<>();
        return headers;
    }

    protected static Boolean isBoolean(JSONObject jsonObject, String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonObject.toString());
        return node.get(key).isBoolean();
    }

    protected static Boolean isString(JSONObject jsonObject, String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonObject.toString());
        return node.get(key).isTextual();
    }

}
