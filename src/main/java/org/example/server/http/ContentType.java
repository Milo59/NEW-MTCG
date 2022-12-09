package org.example.server.http;

public enum ContentType {
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    VIDEO_MPEG("video/mpeg"),
    VIDEO_MP4("video/mp4"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_JSON("application/json"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    ;


    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String contentType;
}
