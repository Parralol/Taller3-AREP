package edu.escuelaing.arem.ASE.app.WebServers;

import java.io.IOException;

public class WebServices {
    
    public static void main(String[] args) throws IOException{
        HttpServer.get("/arep", (p) ->{ 
            
            String  resp = "HTTP/1.1 200 OK\r\n"
            + "Content-Type:text/html\r\n"
            + "\r\n" +"hello arep" +"\r\n" ;
            return resp;
    
    });
    HttpServer.get("/arsw", (p) ->{ 
            
        String  resp = "HTTP/1.1 200 OK\r\n"
        + "Content-Type:text/html\r\n"
        + "\r\n" +"hello arsw" +"\r\n" ;
        return resp;

});
    HttpServer.getInstance().runServer(args);
    }
}
