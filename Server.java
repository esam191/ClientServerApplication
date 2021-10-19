import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) {
        //usage on running the Server file
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }
        //declaring necessary variables
        ServerSocket server = null;
        Socket client;
        //port number passed as an argument
        int portNumber = Integer.parseInt(args[0]);

        try {
            //starting server side
            server = new ServerSocket(portNumber);
            server.setReuseAddress(true);
            System.out.println("Server Listening on port " + portNumber + "....");
            
            while(true){
                //accepting client
                client = server.accept();
                //to indicate when new client connects to the server
                System.out.println("New Client Connected! IP Address and Port Number: " 
                + client.getInetAddress().getHostAddress() + " " + client.getLocalPort());
                //multithread server to handle multiple clients
                MultiThread clSocket = new MultiThread(client);
                
                //thread to handle when new client connects
                new Thread(clSocket).start();
            }   
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber);
            System.out.println(e.getMessage());
        }
    }
}
