import java.io.*;
import java.net.*;

public class Client {

  public static Socket client;  

  public static BufferedReader br;
  public static BufferedReader br_ser;
  public static PrintWriter dos;
  public static String selected;
  public static boolean recommendBook = false;

  public static void main(String[] args){

    if (args.length != 2) {
      System.err.println("Usage: java Client <host name> <port number>");
      System.exit(1);
    }
    
    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);

    try {
      client = new Socket(hostName, portNumber);
      System.out.println("Connected");

      dos = new PrintWriter(client.getOutputStream(), true);
      br_ser = new BufferedReader(new InputStreamReader(client.getInputStream()));
      br = new BufferedReader(new InputStreamReader(System.in));
      
      selected = "0";
      while(Integer.parseInt(selected) != -1){
        System.out.println(br_ser.readLine());
        selected = br.readLine();
        dos.println(selected);
        dos.flush();
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
      client.close(); 
      
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

   public static void respondOne(){
     try {
        if(recommendBook == false){
          System.out.println(br_ser.readLine()); 
          String bookName = br.readLine();
          dos.println(bookName);
          dos.flush();
        } else {
            System.out.println(br_ser.readLine());
            System.out.println(br_ser.readLine());
            String quantity = br.readLine();
            dos.println(quantity);
            dos.flush();
            System.out.println("Server reply: \n" + br_ser.readLine());
         }
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
}

