package edu.escuelaing.arem.ASE.app.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.management.RuntimeErrorException;

import edu.escuelaing.arem.ASE.app.HttpContext;

public class ImgService implements RespService{

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
        "Content-type:" + HttpContext.getImg()  +
        "\r\n";
    }

    @Override
    public String getGetResponse() {
        String content = "";
        try{
            content = new String(Files.readAllBytes(Paths.get("Taller3-AREP/src/main/resources/prueba.html")));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return content;
    }
    
}
