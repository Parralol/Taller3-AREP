package edu.escuelaing.arem.ASE.app;

import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;

 
public class HttpServer {
    private static String key = "&apikey=b5ed8d05";
    private static String url = "http://www.omdbapi.com/?t=";
    private static Map<String, String> cache = new HashMap<String, String>();
    public static void main(String[] args) throws IOException {
        boolean newSearch= true;
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
            String inputLine;
            
            boolean firstLine = true;
            String uriStr = "";
            int count = 0;
            String request="";

                
            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                if(count == 0){
                    request = inputLine;
                    newSearch=IsItNew(request);
                    request = getQuery(request);
                    count +=1;
                    System.out.println(request+"<-------------- request");
                    if(request.equals("")) newSearch=true;
                }
                uriStr = uriStr.split("\\?"+request)[0];
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            System.out.println(uriStr);
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
            String res = "HTTP/1.1 200 OK\r\n"
            + "Content-Type:" + context +"\r\n";
            OutputStream output = clientSocket.getOutputStream();
            byte[] webpContent = {};
            FileInputStream fin = null;
            BufferedInputStream bin= null;
            BufferedOutputStream bout= null;
            if(!uriStr.equals("/favicon.ico")){
                
            try{
                String filename ="Taller3-AREP/src/main/resources/public"+ uriStr;
                System.out.println(filename);
                if(context.equals(HttpContext.getHtml())){
                    BufferedReader reader = new BufferedReader(new FileReader(filename));
                    String linea = null;
                    while((linea = reader.readLine()) != null ){
                        res += linea;
                    }
                    reader.close();
                }else{
                   fin = new FileInputStream(filename);
                   bin = new BufferedInputStream(fin);
                   bout = new BufferedOutputStream(output);
                }
            }catch(Exception e){
                System.out.println("An error occurred.");
                e.printStackTrace();
                BufferedReader reader = new BufferedReader(new FileReader("Taller3-AREP\\src\\main\\resources\\public\\error.html"));
                String linea = null;
                while((linea = reader.readLine()) != null ){
                    res += linea;
                }
                reader.close();
            }
        }
           
            if(!uriStr.equals("/favicon.ico") && context.equals(HttpContext.getHtml())){
                    try (BufferedOutputStream bos = new BufferedOutputStream(output)) {
                        
                        if(!newSearch){
                            String inline = "";
                            if(cache.containsKey(request)){
                                inline = cache.get(request);
                            }else{
                                inline = getJson(request);
                            }
                            cache.put(request, inline);
                            System.out.println(inline);
                            res = ( "HTTP/1.1 200 OK\r\n"
                            + "Content-Type:" + context +"\r\n"+inline+"");
                        }else{
                            newSearch=false;
                        }
                        bos.write(res.getBytes());
                        bos.write(webpContent);
                        bos.flush();
                    }
            }else{
                if(!uriStr.equals("/favicon.ico")){
                out.println(res);
                int ch =0; ;  
                while((ch=bin.read())!=-1)  
                {  
                bout.write(ch);  
                }  
                  
                bin.close();  
                fin.close();  
                bout.close(); 
                }
            }
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Allows to split the GET or POST request just to get the query
     * @param text
     * @return
     */
    private static String getQuery(String text){
        String[] deco1 = text.split(" ");
        String[] deco2 = deco1[1].split("\\?");
        System.out.println(Arrays.toString(deco2));
        if(deco2.length >=2){
            String[] deco3 = deco2[1].split("\\#");
            return deco3[0];
        }else{
            return deco2[0];
        }
    }

    private static boolean IsItNew(String text){
        String[] deco1 = text.split(" ");
        String[] deco2 = deco1[1].split("\\?");
        System.out.println(Arrays.toString(deco2));
        if(deco2.length >=2){
            return false;
        }else{
            return true;
        }

    }

    /**
     * Permite recibir el Json que se esta buscando
     * @param request
     * @return
     */
    private static String getJson(String request){
        String[] requests = request.split("=");
        String res ="";
        if(requests.length > 1){
            String defurl = url + requests[1]+key;
            System.out.println(defurl);
            
            try{
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
            }catch(IOException e){
                        System.out.println(e.getMessage());
            }
        }
        return res;
    }
    
}