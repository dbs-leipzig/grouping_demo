package org.gradoop.demo.server;

import com.google.gson.Gson;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static org.gradoop.demo.server.Constants.DATASOURCES_NAME_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestHandlerTest {

    @Test
    public void toJson() throws JSONException {
        Set<String> names = Constants.BUNDLED_DATABASE_NAMES;
        JSONObject result = RequestHandler.toJson(names);
        JSONArray array = (JSONArray)result.get(DATASOURCES_NAME_KEY);
        assertEquals("size is 3", 3, array.length());
    }
}