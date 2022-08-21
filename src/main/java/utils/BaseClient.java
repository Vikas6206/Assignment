package utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    protected Response post(String url, RequestSpecification specification) {

        specification.header("Content-Type", "application/json").header("Accept-Language", PopcornProperties.language);

        return specification.post(url);
    }

    protected Response get(String url, RequestSpecification specification) {

        specification.header("Content-Type", "application/json").header("Accept-Language", PopcornProperties.language);

        return specification.get(url);
    }

    protected Response delete(String url, RequestSpecification specification) {

        specification.header("Content-Type", "application/json").header("Accept-Language", PopcornProperties.language);

        return specification.delete(url);
    }

    protected Response put(String url, RequestSpecification specification) {

        specification.header("Content-Type", "application/json").header("Accept-Language", PopcornProperties.language);

        return specification.put(url);
    }

    protected Response patch(String url, RequestSpecification specification) {

        specification.header("Content-Type", "application/json").header("Accept-Language", PopcornProperties.language);

        return specification.patch(url);
    }
}
