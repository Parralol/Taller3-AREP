package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.WebServers.HttpServer;
import edu.escuelaing.arem.ASE.app.WebServers.WebService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    private HttpServer httpServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        httpServer = HttpServer.getInstance();
        HttpServer.get("/recieve", new WebService() {
            @Override
            public String handle(String parameter) {
                return "Mock response";
            }
        });
    }

    public void testHandleRequest() throws Exception {
        // Start the server (note: this is done here, not in a separate thread)
        try {
            httpServer.runServer(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Allow some time for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Act
        String response = sendHttpRequest("/action/recieve?name=xd.jpg");

        // Assert
        assertEquals("Mock response", response);
    }

    private String sendHttpRequest(String request) throws Exception {
        java.net.Socket socket = new java.net.Socket("localhost", 35000);
        java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true);
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

        // Send the request to the server
        out.println("GET " + request + " HTTP/1.1");
        out.println();

        // Read the response from the server
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }

        // Close the resources
        out.close();
        in.close();
        socket.close();

        return response.toString();
    }


}
