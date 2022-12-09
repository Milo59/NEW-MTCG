package org.example.server;

import org.example.server.dto.Request;
import org.example.server.dto.Response;

public interface Application {

    Response handle(Request request);
}
