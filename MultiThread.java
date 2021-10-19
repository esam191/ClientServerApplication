import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MultiThread implements Runnable {
    public Socket clientSocket;

    public MultiThread(Socket s) {
    clientSocket = s;
    }

    public static BufferedReader br;
    public static BufferedReader br_in;
    public static PrintWriter dos;

    public static String bookName;
    public static int quantity;
    public static int selected;
    public static double tCost;
    public static boolean recommendBook = false;
    
    public void run(){
        try{
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dos = new PrintWriter(clientSocket.getOutputStream(), true);
            br_in = new BufferedReader(new InputStreamReader(System.in));
            displayWelcome();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public static void takeOrder(){
        try {
            System.out.println("Taking order... ");
            if(recommendBook == true) {
                bookName = useRecommended();
                dos.println("Recommended Top Seller: " + bookName);
            } else {
                dos.println("Enter the name of the book: ");
                bookName = br.readLine();
            }
            dos.println("Enter the quantity: ");
            quantity = Integer.parseInt(br.readLine());
            double cost = calcTotal(bookName, quantity);
            tCost += cost;
            dos.println("New Order Addded Succesfully!");
            System.out.println("Client Added Book!");
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }

    public static void displayWelcome(){
        try {
            while(selected != -1){
                dos.println("Please select one of the options: 1 - Give Book Order , 2 - View Total Cost, 3 - Best Seller Recommendation, - 1 - exit");
                selected = Integer.parseInt(br.readLine());
                System.out.println("Client Selected Option " + selected);
                if(selected == 1){
                    takeOrder();
                } else if(selected == 2){
                     displayTotal();
                } else if(selected == 3){
                    recommendBook = true;
                    takeOrder();
                } else if(selected == 4){
                    displayReceipt();
                }
            }
            selected = 0;
            tCost = 0; 
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }

     public static void displayTotal(){
         dos.println("Your total cost is: $" + tCost + " plus tax");   
     }

     public static void displayReceipt(){
         double tax = 0.08 * tCost;
         double total = tCost + tax;
         Date d = new Date();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
         String date = sdf.format(d);
         String timeStamp = "Receipt: Your total order for today including HST (ON - 8%): ";
         dos.println(timeStamp + total + " - Today's Date: " + date);
    }

    public static double calcTotal(String bookName, int quantity){
        File file = new File("book_store.txt");
        double total_cost = 0;
        try {
            final Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                final String bookLine = sc.nextLine();
                if(bookLine.contains(bookName)){
                    String[] parts = bookLine.split(",", 2);
                    int bookCost = Integer.parseInt(parts[1]);
                    total_cost = quantity * bookCost;
                }
            }
        sc.close();  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total_cost;
    }

    public static String useRecommended(){
        List<String> topSellers = Arrays.asList("To Kill A Mockingbird", "The Great Gatsby", "The Hunger Games", "The Fault in Our Stars", "Gone Girl");
        Random r = new Random();
        int random = r.nextInt(topSellers.size());
        String topSellingBook = topSellers.get(random);
        return topSellingBook;
    }

}
