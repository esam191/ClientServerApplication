import java.io.*;
import java.net.*;

public class Client {

  //declaring necessary variables
  public static Socket client;  
  public static BufferedReader br;
  public static BufferedReader br_ser;
  public static PrintWriter dos;
  public static String selected;
  //initializing boolean variable to false
  public static boolean recommendBook = false;

  public static void main(String[] args){
    //usage on running the client
    if (args.length != 2) {
      System.err.println("Usage: java Client <host name> <port number>");
      System.exit(1);
    }
    //taking arguments to connect 
    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);

    try {
      //connecting client 
      client = new Socket(hostName, portNumber);
      System.out.println("Connected");

      dos = new PrintWriter(client.getOutputStream(), true);
      br_ser = new BufferedReader(new InputStreamReader(client.getInputStream()));
      br = new BufferedReader(new InputStreamReader(System.in));
      
      selected = "0";
      //user types -1 to exit
      while(Integer.parseInt(selected) != -1){
        System.out.println(br_ser.readLine());
        selected = br.readLine();
        dos.println(selected);
        dos.flush();
        //options selection menu for client
        if(Integer.parseInt(selected) == 1){
          respondOne();
        } else if(Integer.parseInt(selected) == 2){
            System.out.println("Server reply: \n" + br_ser.readLine());
          } else if(Integer.parseInt(selected) == 3){
              recommendBook = true;
              respondOne();
          } else if(Integer.parseInt(selected) == 4){
            System.out.println("Server reply: \n" + br_ser.readLine());
          }
      }
      //client.close(); 
    } catch (IOException e) {
      e.printStackTrace();
      //System.exit(1);
    }
  }
  //method handles the first option
  public static void respondOne(){
    try {
      //if user picks option 3 
      if(recommendBook == true){
        System.out.println(br_ser.readLine());
      } else {
        System.out.println(br_ser.readLine()); 
        String bookName = br.readLine();
        //sending to server
        dos.println(bookName);
        dos.flush();
      }
      //else it continues here to get quantity
      System.out.println(br_ser.readLine());
      String quantity = br.readLine();
      //sending to server
      dos.println(quantity);
      dos.flush();
      System.out.println("Server reply: " + br_ser.readLine());
      recommendBook = false;
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}

