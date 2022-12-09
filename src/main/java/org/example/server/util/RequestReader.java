package org.example.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestReader {

    /**
     * Takes the input stream from a socket and
     * return the HTTP request as a string.
     *
     * @param in Input stream from socket
     * @return HTTP request as a string
     * @throws IOException
     */
    public static String read(BufferedReader in) throws IOException {
        StringBuilder builder = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null && !inputLine.equals("")) {
            builder.append(inputLine).append(System.lineSeparator());
        }

        String header = builder.toString();

        int contentLength = HttpRegex.findHeaderAsInt(header, "Content-Length");

        if (0 == contentLength) {
            return header;
        }

        char[] content = new char[contentLength];
        in.read(content, 0, content.length);

        return header + System.lineSeparator() + new String(content);
    }
}
