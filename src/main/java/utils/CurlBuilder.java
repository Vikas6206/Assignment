package utils;

import org.testng.Reporter;


public class CurlBuilder {
    private IServiceEndpoint endpoint;
    private StringBuilder curlString;
    private String url;

    public CurlBuilder(IServiceEndpoint endpoint) {
        this.endpoint = endpoint;
        curlString = new StringBuilder();
        curlString.append("curl");

        try {
            appendHttpMethodType();
            getUrlCopy();
            appendPathParamsToUrl();
            appendQueryParamsToUrl();
            appendURLToCurlString();
            appendHeadersToCurlString();
            appendBodyStringToCurlString();
        } catch (Exception e) {
            Reporter.log(e.getMessage());
        }
    }

    public String getCurlString() {
        return curlString.toString();
    }

    private void appendHttpMethodType() {
        String method = endpoint.httpMethod().toString();
        curlString.append(" -X ");
        curlString.append(method).append(" ");
    }

    private void appendPathParamsToUrl() {
        if (endpoint.pathParameters() != null) {
            endpoint.pathParameters().forEach(param -> url = url.replace("{" + param.getKey() + "}", param.getValue()));
        }
    }

    private void getUrlCopy() {
        url = endpoint.url();
    }

    private void appendQueryParamsToUrl() {
        if (endpoint.queryParameters() != null) {
            url = url + "?";
            endpoint.queryParameters().forEach(param -> {
                url = url + param.getKey() + "=" + param.getValue() + "&";
            });

            url = url.substring(0, url.length() - 1);
        }
    }

    private void appendURLToCurlString() {
        curlString.append(url).append(" ");
    }

    private void appendHeadersToCurlString() {
        if (endpoint.headers() == null)
            return;
        endpoint.headers().forEach(param -> {
            curlString.append("-H ");
            curlString.append("'").append(param.getKey()).append(": ").append(param.getValue()).append("' ");
        });
    }

    private void appendBodyStringToCurlString() {
        if (endpoint.body() != null) {
            if (endpoint.body().getBodyString() == null)
                return;
            curlString.append("-d ");
            curlString.append("'").append(endpoint.body().getBodyString()).append("'");

        }
    }

}
