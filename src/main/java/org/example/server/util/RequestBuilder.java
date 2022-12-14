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

        // Content
        request.setContent(HttpRegex.findContent(requestString));

        request.setToken(getToken(requestString));

        // TODO: Add additional information to the request
        //request.setContent(getContent(requestString));
     /*   if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")){ //如果方法是post或是put 那么增加用户？
            request.setContent(getContext(requestString));
        }*/

        return request;
    }

    private static String getToken(String requestString) {
        String token = HttpRegex.findHeaderAsString(requestString, "Authorization");
        if (null != token) {
            String[] s = token.split(" ");
            if (s.length > 1) {
                token = s[1];
            }
        }
        return token;
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

    // TODO : getContent...
   /* private static String getContext(String requestString) throws UnsupportedProtocolException{ //
        String CRLF = "\r\n"; //引号内是什么意思？
        String content = requestString.substring(requestString.lastIndexOf(CRLF)).trim();
        return content;
        }*/

}
