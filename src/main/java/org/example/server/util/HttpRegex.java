package org.example.server.util;

import org.example.server.exception.UnsupportedProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRegex {

    public static String findMethod(String request) {
        Pattern r = Pattern.compile("^([A-Z]+)", Pattern.MULTILINE);
        Matcher m = r.matcher(request);

        if (!m.find()) {
            return null;
        }

        return m.group(1);
    }

    public static String findPath(String request) {
        Pattern r = Pattern.compile("^[A-Z]+\\s(\\S+)", Pattern.MULTILINE);
        Matcher m = r.matcher(request);

        if (!m.find()) {
            return null;
        }

        return m.group(1);
    }

    public static String findContent(String request) {
        Pattern r = Pattern.compile("^.+[\\r\\n|\\r|\\n]{2}(.*)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = r.matcher(request);

        if (!m.find()) {
            return null;
        }

        return m.group(1);
    }

    /**
     * Finds the value of an HTTP header in an HTTP request string.
     * Returns the value as a string. If not found returns null;
     *
     * @param request HTTP request as a string
     * @param header HTTP header you want to know the value of
     * @return Value of the HTTP header as a string (default null)
     */
    public static String findHeaderAsString(String request, String header) {
        return findHeader(request, header);
    }

    /**
     * Finds the value of an HTTP header in an HTTP request string.
     * Returns the value as an integer. If not found returns 0;
     *
     * @param request HTTP request as a string
     * @param header HTTP header you want to know the value of
     * @return Value of the HTTP header as an integer (default 0)
     */
    public static int findHeaderAsInt(String request, String header) {
        String value = findHeader(request, header);

        return null == value ? 0 : Integer.parseInt(value);
    }

    private static String findHeader(String request, String header) {
        Pattern r = Pattern.compile("^" + header + ":\\s(.+)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(request);

        if (!m.find()) {
            return null;
        }

        return m.group(1);
    }
}
