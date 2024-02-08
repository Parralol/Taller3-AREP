package edu.escuelaing.arem.ASE.app.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.escuelaing.arem.ASE.app.HttpContext;

public class HTMLService implements RespService{

    @Override
    public String getPostHeader() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostHeader'");
    }

    @Override
    public String getPostResponse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostResponse'");
    }

    @Override
    public String getGetHeader() {
        return "HTTP/1.1 200 OK\r\n" +
        "Content-type:" + HttpContext.getHtml() 
        + "\r\n";
    }

    @Override
    public String getGetResponse() {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("Taller3-AREP/src/main/resources/cliente.html")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }
    
}
