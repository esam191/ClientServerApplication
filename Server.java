import java.io.*;
import java.net.*;

public class Server {

    public static void main(String args[]) {
        
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }

        ServerSocket server;
        Socket client;
        int portNumber = Integer.parseInt(args[0]);

        try {
            server = new ServerSocket(portNumber);
            server.setReuseAddress(true);
            System.out.println("Server Listening on port " + portNumber + "....");
    
            while(true){
                client = server.accept();
                
                System.out.println("Client Connected! IP Address and Port Number: " 
                + client.getInetAddress().getHostAddress() + " " + client.getLocalPort());
                
                MultiThread clSocket = new MultiThread(client);
                new Thread(clSocket).start();
            }   
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber);
            System.out.println(e.getMessage());
        }
    }
}
