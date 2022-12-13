package org.example.server.util;

import org.example.server.dto.Request;
import org.example.server.exception.UnsupportedProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestBuilder {

    /**
     * Takes an HTTP request as a string and builds and
     * returns a Request object.
     *
     * @param requestString HTTP request as a string
     * @return HTTP request as an object
     */
    public static Request build(String requestString) throws UnsupportedProtocolException {
        Request request = new Request();
        request.setRequest(requestString);

        // Method
        request.setMethod(getMethod(requestString));

        // Path
        request.setPath(getPath(requestString));

        // Content Length
        request.setContentLength(HttpRegex.findHeaderAsInt(requestString, "Content-Length"));

        // TODO: Add additional information to the request
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")){
            request.setContent(getContext(requestString));
        }

        return request;
    }

    private static String getContext(String requestString) throws UnsupportedProtocolException {
        String CRLF = "\r\n";
        String content = requestString.substring(requestString.lastIndexOf(CRLF)).trim();
        return content;
    }

    private static String getMethod(String requestString) throws UnsupportedProtocolException {
        String method = HttpRegex.findMethod(requestString);

        if (null == method) {
            throw new UnsupportedProtocolException("No HTTP method in request");
        }

        return method;
    }

    private static String getPath(String requestString) throws UnsupportedProtocolException {
        String path = HttpRegex.findPath(requestString);

        if (null == path) {
            throw new UnsupportedProtocolException("No HTTP path in request");
        }

        return path;
    }
}
