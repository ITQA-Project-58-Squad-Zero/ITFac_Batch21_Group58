package api.context;

import io.restassured.response.Response;

public class ApiResponseContext {
    private static final ThreadLocal<Response> RESPONSE = new ThreadLocal<>();

    public static void setResponse(Response response) {
        RESPONSE.set(response);
    }

    public static Response getResponse() {
        return RESPONSE.get();
    }

    public static void clear() {
        RESPONSE.remove();
    }
}
