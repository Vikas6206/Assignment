package utils;

import io.restassured.response.Response;
import org.testng.Reporter;
import popcorn.GetPromotionCodeEndPointLink;

public class ServiceClient extends BaseClient {

    public Response getPromotionCodeWithKey(String key) {
        GetPromotionCodeEndPointLink getPromotionCodeEndPointLink = new GetPromotionCodeEndPointLink(key);
        Response response = new RequestHandler().processRequest(getPromotionCodeEndPointLink);
        Reporter.log(String.format("Get popcorn api promotion with key  Response --- GET (%s)", response.getStatusCode()), true);
        return response;
    }

}
