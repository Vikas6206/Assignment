package popcorn;

import utils.*;

import java.util.ArrayList;
import java.util.List;

public class GetPromotionCodeEndPointLink implements IServiceEndpoint {
    String apiKey;

    public GetPromotionCodeEndPointLink(String apiKey) {
        this.apiKey = apiKey;
    }


    @Override
    public String url() {
        return PopcornProperties.hostLink + "popcorn-api-rs-7.9.17/v1/promotions";
    }

    @Override
    public HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public List<Param> queryParameters() {
        List<Param> queryParam = new ArrayList<>();
        queryParam.add(new Param("apikey",this.apiKey));
        return queryParam;
    }

    @Override
    public List<Param> pathParameters() {
        return null;
    }

    @Override
    public List<Param> headers() {
        return null;
    }

    @Override
    public RequestBody body() {
        return null;
    }
}
