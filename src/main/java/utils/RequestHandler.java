package utils;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Reporter;


import static io.restassured.RestAssured.given;

public class RequestHandler {

    private boolean urlEncodingEnabled = true;

    public Response processRequest(IServiceEndpoint iServiceEndpoint) {
        Response response = processIServiceEndpoint(iServiceEndpoint);
        String endpointName = iServiceEndpoint.getClass().getSimpleName().replaceAll("Endpoint", "");
        String noOfRetries = System.getProperty("noOfRetries");
        int retries = (noOfRetries == null || noOfRetries.isEmpty()) ? 0 : Integer.parseInt(noOfRetries);
        for (int i = 0; i < retries && isResponse5xx(response); i++) {
            Reporter.log(String.format("\n%s Response Status Code --- %d --> Retrying again --> Retry no - %d", endpointName, response.getStatusCode(), i + 1), true);
            TestHelper.wait(2000);
            response = processIServiceEndpoint(iServiceEndpoint);
        }
        return response;
    }

    private Response processIServiceEndpoint(IServiceEndpoint iServiceEndpoint) {
        RestAssured.registerParser("text/plain", Parser.JSON);
//        RestAssured.registerParser("application/grpc", Parser.JSON);
        RestAssured.registerParser("text/html", Parser.JSON);

        String endpointName = iServiceEndpoint.getClass().getSimpleName();
        String url = iServiceEndpoint.url();
        HttpMethod httpMethod = iServiceEndpoint.httpMethod();
        RequestBody body = iServiceEndpoint.body();

        RequestSpecification requestSpecification = formRequestSpecification(iServiceEndpoint);

        logRequestDetailsWithCurl(iServiceEndpoint, endpointName, url, httpMethod, body);
        Response response = makeAPIRequestAsPerHTTPMethod(url, httpMethod, requestSpecification);
        printResponseDetails(iServiceEndpoint, endpointName, response);


        return response;
    }

    private void logRequestDetailsWithCurl(IServiceEndpoint iServiceEndpoint, String endpointName, String url, HttpMethod httpMethod, RequestBody body) {
        Reporter.log(String.format("\n" + endpointName + " URL --- %s %s", httpMethod.toString(), url), true);

        if (iServiceEndpoint.headers() != null) {
            for (Param p : iServiceEndpoint.headers()) {
                Reporter.log(String.format(endpointName + " Header --- [ %s : %s ]", p.getKey(), p.getValue()), true);
            }
        }

        if (body != null)
            Reporter.log(String.format(endpointName + " Request --- %s", body.getBodyString()), true);

        Reporter.log(String.format(endpointName + " Curl --- %s", new CurlBuilder(iServiceEndpoint).getCurlString()), true);
    }

    private void printResponseDetails(IServiceEndpoint iServiceEndpoint, String endpointName, Response response) {
        Reporter.log(String.format(endpointName + " Response Status Code --- (%s)", response.getStatusCode()), true);

        Reporter.log(String.format(endpointName + " Response --- %s", response.asString()), true);

        printResponseHeaders(endpointName, response);
    }


    private void printResponseHeaders(String endpointName, Response response) {
        String responseHeaders = response.headers().toString();
        Reporter.log(String.format("%s Response headers --- \n%s", endpointName, responseHeaders), true);
    }

    private Response makeAPIRequestAsPerHTTPMethod(String url, HttpMethod httpMethod, RequestSpecification requestSpecification) {
        Response response = null;
        switch (httpMethod) {
            case GET:
                response = requestSpecification.get(url);
                break;
            case POST:
                response = requestSpecification.post(url);
                break;
            case PUT:
                response = requestSpecification.put(url);
                break;
            case PATCH:
                response = requestSpecification.patch(url);
                break;
            case DELETE:
                response = requestSpecification.delete(url);
        }

        return response;
    }

    private RequestSpecification formRequestSpecification(IServiceEndpoint iServiceEndpoint) {
        RestAssuredConfig config = RestAssured.config()
                .encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        RequestSpecification request = given().config(config);

        if (iServiceEndpoint.headers() != null) {
            iServiceEndpoint.headers().forEach(h -> request.header(h.getKey(), h.getValue()));
        }

        if (iServiceEndpoint.queryParameters() != null) {
            iServiceEndpoint.queryParameters().forEach(q -> request.queryParam(q.getKey(), q.getValue()));
        }

        if (iServiceEndpoint.pathParameters() != null) {
            iServiceEndpoint.pathParameters().forEach(p -> request.pathParam(p.getKey(), p.getValue()));
        }

        if (iServiceEndpoint.formParam() != null)
            iServiceEndpoint.formParam().forEach(p -> request.formParam(p.getKey(), p.getValue()));

        if (iServiceEndpoint.body() != null)
            request.body(iServiceEndpoint.body().getBodyString());

        if (!urlEncodingEnabled)
            request.urlEncodingEnabled(urlEncodingEnabled);

        return request;
    }


    private boolean isResponse5xx(Response response) {
        return (response.getStatusCode() >= 500) && (response.getStatusCode() < 505);
    }

}
