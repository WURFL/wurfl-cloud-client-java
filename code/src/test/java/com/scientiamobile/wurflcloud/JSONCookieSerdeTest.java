package com.scientiamobile.wurflcloud;

import static com.jsoniter.JsonIterator.deserialize;
import static com.jsoniter.output.JsonStream.serialize;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import com.scientiamobile.wurflcloud.device.JsonCookie;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;


@Test(groups = "unit")
public class JSONCookieSerdeTest {

    @Test(groups = "unit")
    public void testSerializationDeserializationTest() {
        Map<String,Object> capabilities = new HashMap<>();
        capabilities.put("brand_name", "Apple");
        capabilities.put("model_name", "iPad Pro 11");

        JsonCookie cookie = new JsonCookie(capabilities, 163265896L, "apple_ipad_ver1_sub13_7_subhwpro3110");
        String jsonString = serialize(cookie);
        assertNotNull(jsonString);

        JsonCookie deserializedCookie = deserialize(jsonString, JsonCookie.class);
        assertNotNull(deserializedCookie);
        assertEquals(cookie.getDate_set(), deserializedCookie.getDate_set());
        assertEquals(cookie.getCapabilities().size(), deserializedCookie.getCapabilities().size());
        assertEquals(cookie.getId(), deserializedCookie.getId());
    }
}
