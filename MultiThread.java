import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MultiThread implements Runnable {
    public Socket clientSocket;
    //constructor to pass in the socket
    public MultiThread(Socket s) {
    clientSocket = s;
    }
    //declaring all necessary variables
    public static BufferedReader br;
    public static BufferedReader br_in;
    public static PrintWriter dos;
    public static String bookName;
    public static int quantity;
    public static int selected;
    public static double tCost;
    //initializing to boolean variable for option 3 to false
    public static boolean recommendBook = false;
    
    public void run(){
        try{
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dos = new PrintWriter(clientSocket.getOutputStream(), true);
            br_in = new BufferedReader(new InputStreamReader(System.in));
            //starting page that is displayed on the client side
            displayWelcome();
            dos.close();
            br.close();
            clientSocket.close();
        } catch(IOException e){
            System.out.println(e);
        }
    }
    //the first option which adds new order
    //also handles option 3 to use recommended book
    public static void takeOrder(){
        try {
            System.out.println("Taking order... ");
            if(recommendBook == true) {
                //using recommended book from server
                bookName = useRecommended();
                dos.println("Recommended Top Seller: " + bookName);
            } else {
                dos.println("Enter the name of the book: ");
                bookName = br.readLine();
            }
            //else continues to get the quantity
            dos.println("Enter the quantity: ");
            quantity = Integer.parseInt(br.readLine());
            double cost = calcTotal(bookName, quantity);
            tCost += cost;
            //sends client order confirmation
            dos.println("New Order Addded Succesfully!");
            System.out.println("Client Added Book!");
            recommendBook = false;
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }
    //starting page that handles all options for client
    public static void displayWelcome(){
        try {
            //exits when user types in -1
            while(selected != -1){
                dos.println("Please select one of the options: 1 - Give New Book Order , 2 - View Total Cost, 3 - Best Seller Recommendation, 4 - View Receipt, -1 - exit");
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
            System.out.println("Client Exited!");
            selected = 0;
            tCost = 0; 
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }
    //method to display the client's total cost of order
    public static void displayTotal(){
        dos.println("Your total cost is: $" + tCost + " plus tax");
        System.out.println("Total Cost Displayed!");   
    }
    //displays receipt that includes tax calculation and time stamp
    public static void displayReceipt(){
        double tax = 0.08 * tCost;
        double total = tCost + tax;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        String date = sdf.format(d);
        String timeStamp = "Receipt: Your total order for today including HST (ON - 8%): ";
        dos.println(timeStamp + total + " - Today's Date: " + date);
        System.out.println("Receipt Displayed!");   
    }
    //calculates the total cost by finding book+cost in the text file
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
    //returns a random book from the list of best sellers
    public static String useRecommended(){
        System.out.println("Recommending Top Seller... ");
        List<String> topSellers = Arrays.asList("To Kill A Mockingbird", "The Great Gatsby", "The Hunger Games", "The Fault in Our Stars", "Gone Girl");
        Random r = new Random();
        int random = r.nextInt(topSellers.size());
        String topSellingBook = topSellers.get(random);
        return topSellingBook;
    }

}
