package edu.escuelaing.arem.ASE.app.SparkLike;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Response {
    private static Response _instance = new Response();
    private String context;
    private String path;

    public Response(){}
    public static Response getInstance() {return _instance;}

    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: "+ getContext() +"\r\n" +
                "\r\n";
    }

    public String getResponse() {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("Taller3-AREP/src/main/resources/" + getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getHeader() + content;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
