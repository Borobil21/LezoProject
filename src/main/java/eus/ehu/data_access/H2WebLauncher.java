package eus.ehu.data_access;

import org.h2.tools.Server;

public class H2WebLauncher {
    private static final String WEB_PORT = "8084";
    private static final String TCP_PORT = "9094";
    private static Server webServer;
    private static Server tcpServer;

    public static synchronized void startServer() throws Exception {
        if (webServer == null) {
            webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", WEB_PORT).start();
            System.out.println("H2 Web Console server started.");
            System.out.println("URL: " + webServer.getURL());
        }

        if (tcpServer == null) {
            tcpServer = Server.createTcpServer(
                    "-tcp",
                    "-tcpAllowOthers",
                    "-tcpPort", TCP_PORT,
                    "-ifNotExists").start();
            System.out.println("H2 TCP server started on port " + TCP_PORT + ".");
        }
    }

    public static void main(String[] args) {
        try {
            startServer();
            System.out.println("Press Ctrl+C to stop the server...");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Error starting H2 server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}