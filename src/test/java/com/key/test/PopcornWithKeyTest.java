package com.key.test;

import com.base.KeyBase;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ServiceClient;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class PopcornWithKeyTest extends KeyBase {
    ServiceClient serviceClient;

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        serviceClient = new ServiceClient();
    }


    @Test
    public void successfulPromotionWithKeyTest() throws IOException {
        Response response = serviceClient.getPromotionCodeWithKey("webB2BGDMSTGExy0sVDlZMzNDdUyZ");
        Assert.assertEquals(200,response.getStatusCode(),String.format("Expected 200 but got %s", response.getStatusCode()));
        String responseString = response.getBody().asString();

        JSONObject jsonObject = new JSONObject(responseString);
        Assert.assertTrue(jsonObject.has("promotions"));

        JSONArray promotions = jsonObject.getJSONArray("promotions");
        for(int i=0; i<promotions.length() ;i++) {
            Assert.assertTrue(promotions.getJSONObject(i).has("promotionId"));
            Assert.assertTrue(promotions.getJSONObject(i).has("orderId"));
            Assert.assertTrue(promotions.getJSONObject(i).has("promoArea"));
            Assert.assertTrue(promotions.getJSONObject(i).has("promoType"));
            Assert.assertTrue(isBoolean(promotions.getJSONObject(i),"showPrice"));
            Assert.assertTrue(isBoolean(promotions.getJSONObject(i),"showText"));

            System.out.println(promotions.getJSONObject(i).has("localizedTexts"));
            if(promotions.getJSONObject(i).has("localizedTexts")) {
                JSONObject lt = promotions.getJSONObject(i).getJSONObject("localizedTexts");
                System.out.println(lt.has("ar"));
                System.out.println(lt.has("en"));
            }
            Assert.assertTrue(isString(promotions.getJSONObject(i),"promotionId"));

            if(promotions.getJSONObject(i).has("properties")) {
                JSONArray properties = promotions.getJSONObject(i).getJSONArray("properties");


                for(int j=0;j<properties.length();j++){
                    if(properties.getJSONObject(j).has("programType")) {
                        String programTypeValue = properties.getJSONObject(j).getString("programType");
                        Set<String> hashSet = new HashSet<String>();
                        hashSet.add("movie");
                        hashSet.add("episode");
                        hashSet.add("series");
                        hashSet.add("season");
                        Assert.assertTrue(hashSet.contains(programTypeValue));
                    }
                }
            }
        }
    }

    @Test
    public void failPromotionWithKeyTest() throws IOException {
        Response response = serviceClient.getPromotionCodeWithKey("wrongKey");
        Assert.assertEquals(403, response.getStatusCode(), String.format("Expected 403 but got %s", response.getStatusCode()));
        String responseString = response.getBody().asString();

        JSONObject jsonObject = new JSONObject(responseString);
        JSONObject error =jsonObject.getJSONObject("error");
        Assert.assertTrue(error.getString("requestId")!=null);
        Assert.assertEquals(8001, error.getInt("code"),  String.format("Expected 8001 but got %s", error.get("code")));
        Assert.assertEquals("invalid api key", error.getString("message"),
                String.format("Expected invalid api key but got %s",error.getString("message")));
    }

}
