package edu.escuelaing.arem.ASE.app.WebServers;

import java.net.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.io.*;

public class HttpServer {

    private static HttpServer _instance = new HttpServer();
    private static String key = "&apikey=b5ed8d05";
    private static String url = "http://www.omdbapi.com/?t=";
    private static Map<String, WebService> services = new HashMap<String, WebService>();

    private HttpServer() {
    }

    public static HttpServer getInstance() {
        return _instance;
    }

    public void runServer(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String uriStr = "";
            String context = "";
            outputLine="";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (inputLine.startsWith("GET")) {
                    uriStr= inputLine.split(" ")[1];
                    break;
                }
                }
                try{
                    URI requestUri= new URI(uriStr);
                    String path = requestUri.getPath();
                    if(path.startsWith("/action")){
                        String webUri = path.replace("/action", "");
                        if(services.containsKey(webUri)){
                            String p = requestUri.getQuery();
                            outputLine = services.get(webUri).handle(p);
                    }
                }
                }catch(Exception e){
                    
                }
            try{
                //context = getHttpContext(uriStr);
            }catch(Exception e)
            {}
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
       

            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String httpError() {
        String outputLine = "HTTP/1.1 400 Not Found\r\n"
                + "Content-Type:text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Error Not found</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Error</h1>\n"
                + "    </body>\n";
        return outputLine;

    }

    static String getResource(String resource) {
        String res = "";
        String context = "";
        String query = getQuery(resource);
        System.out.println(query + "<----------- query");
        String header ="";
        try {
           context = getHttpContext(resource);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        header = generateHeader(context);
        res +=header;
        String body = generateBody(context,new File("Taller3-AREP\\target\\classes\\public\\"+ query));
        res += body;
        return res;
    }

    /**
     * Allows to split the GET or POST request just to get the query
     * 
     * @param text
     * @return
     */
    private static String getQuery(String text) {
        String[] deco2 = text.split("\\=");
        System.out.println(Arrays.toString(deco2));
        if (deco2.length >= 2) {
            String[] deco3 = deco2[1].split("\\#");
            return deco3[0];
        } else {
            return deco2[0];
        }
    }

    private static boolean IsItNew(String text) {
        String[] deco1 = text.split(" ");
        String[] deco2 = deco1[1].split("\\?");
        System.out.println(Arrays.toString(deco2));
        if (deco2.length >= 2) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * Permite recibir el Json que se esta buscando
     * 
     * @param request
     * @return
     */
    private static String getJson(String request) {
        String[] requests = request.split("=");
        String res = "";
        if (requests.length > 1) {
            String defurl = url + requests[1] + key;
            System.out.println(defurl);

            try {
                URL api = new URL(defurl);
                HttpURLConnection connection = (HttpURLConnection) api.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responsecode = connection.getResponseCode();
                System.out.println("CONNECTION STATUS" + "----->  " + responsecode);
                String inline = "";
                Scanner scanner = new Scanner(api.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                res = inline;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return res;
    }

    public static void get(String rute, WebService s) {
        services.put(rute, s);
    }

    private static String generateBody(String context, File filename){
        String res = "no funciona";
        System.out.println(context);
        try{
            if(context.equals(HttpContext.getHtml())){
                StringBuilder body = generateString(filename);
                res = "" + body;
            }

            if(context.equals(HttpContext.getImg())){
                byte[] bytes = Files.readAllBytes(filename.toPath());
                String base64 = Base64.getEncoder().encodeToString(bytes);
                    res = "<!DOCTYPE html>\r\n"
                        + "<html>\r\n"
                        + "    <head>\r\n"
                        + "        <title>Resultado</title>\r\n"
                        + "    </head>\r\n"
                        + "    <body>\r\n"
                        + "         <center><img src=\"data:image/jpeg;base64," + base64 + "\" alt=\"image\"></center>" + "\r\n"
                        + "    </body>\r\n"
                        + "</html>";
                }
            if(context.equals(HttpContext.getCss())){
                StringBuilder body = generateString(filename);
                return "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\r\n" + //
                        "<html>\r\n" + //
                        "    <head>\r\n" + //
                        "        <meta charset=\"UTF-8\">\r\n" + //
                        "    </head>\r\n" + //
                        "    <body>\r\n" + //
                        "        <pre>" + body + "</pre>\r\n" + //
                        "    </body>\r\n" + //
                        "</html>";
            } if(context.equals(HttpContext.getJs())){
                StringBuilder body = generateString(filename);
                res = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\r\n" + //
                        "<html>\r\n" + //
                        "    <head>\r\n" + //
                        "        <meta charset=\"UTF-8\">\r\n" + //
                        "    </head>\r\n" + //
                        "    <body>\r\n" + //
                        "        <pre>" + body + "</pre>\r\n" + //
                        "    </body>\r\n" + //
                        "</html>";
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            res = httpError();
        }
        System.out.println(res + "<---------- res");
        return res;
        }
    private static String getHttpContext(String uriStr) throws URISyntaxException{
        URI requestUri= new URI(uriStr);
        String path = requestUri.getPath();
        System.out.println(path + "<-------------- uriStr ");
        String context = HttpContext.getHtml();

        String[] urisp = uriStr.split("\\.");
        System.out.println(Arrays.toString(urisp));
        if(urisp.length >1){
            if(urisp[1].equals("jpg") || urisp[1].equals("webp")){
                 context = HttpContext.getImg();
            }else if(urisp[1].equals("css")){
                context = HttpContext.getCss();
            }else if(urisp[1].equals("js")){
                context = HttpContext.getJs();
            }
        }
        return context;
    }
    private static String generateHeader(String context){
        if(context.equals(HttpContext.getImg())) context = HttpContext.getHtml();

        String outputLine = "HTTP/1.1 200 OK\r\n"
        + "Content-Type:"+context + "\r\n";
        return outputLine;
    }
    public static StringBuilder generateString(File file) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line).append("\n");
        }
        reader.close();
        return body;
    }

}