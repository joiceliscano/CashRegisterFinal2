/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.joice.cashregisterfinal;

/**
 *
 * @author Eliza Joice Liscano
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CashRegisterFinal {
    


    static String[] prodNames = {"Iphone 16", "Iphone 16 Pro", "Iphone 16 Pro Max", "Apple Watch", "Iphone X Max", "Iphone 14", "Iphone 13"};
    static double[] prodPrices = {549, 699, 840, 620, 320, 430, 500};
    static ArrayList<Product> cart = new ArrayList<>();
    static ArrayList<Users> users = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static String currentUser = "";

    static class Product {
        String name;
        int quantity;
        double price;

        public Product(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public double getTotalPrice() {
            return quantity * price;
        }

        public String toString() {
            return quantity + " x " + name + " @ $" + price + " each";
        }
    }

    static class Users {
        String username;
        String password;

        public Users(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static void SignUp() {
        System.out.println("Sign Up");
        String username = ValidUsername();
        String password = ValidPassword();
        users.add(new Users(username, password));
        System.out.println("Sign Up Successful!");
    }

    public static void LogIn() {
        System.out.println("Log In");
        while (true) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            for (Users user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = username;
                    System.out.println("Log In Successful!");
                    return;
                }
            }
            System.out.println("Incorrect username or password.");
        }
    }

    public static String ValidUsername() {
        while (true) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            if (username.matches("^[a-zA-Z0-9]{5,15}$")) {
                return username;
            } else {
                System.out.println("Invalid Username. Use 5-15 letters/numbers only.");
            }
        }
    }

    public static String ValidPassword() {
        while (true) {
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            if (password.length() >= 8 && password.length() <= 20 &&
                password.matches(".*[A-Z].*") && password.matches(".*[0-9].*")) {
                return password;
            } else {
                System.out.println("Invalid Password. Must be 8-20 characters with at least one uppercase and one number.");
            }
        }
    }

    public static void displayProducts() {
        System.out.println("Available Products:");
        for (int i = 0; i < prodNames.length; i++) {
            System.out.println((i + 1) + ". " + prodNames[i] + " - $" + prodPrices[i]);
        }
    }

    public static void displayCart() {
        System.out.println("Products in cart:");
        for (int i = 0; i < cart.size(); i++) {
            System.out.println((i + 1) + ". " + cart.get(i));
        }
    }

    public static double calculateTotal() {
        double total = 0;
        for (Product product : cart) {
            total += product.getTotalPrice();
        }
        return total;
    }

    public static void logTransaction(String username, ArrayList<Product> items, double total) {
        try (FileWriter writer = new FileWriter("transactions.txt", true)) {
            String dateTime = new Date().toString();

            writer.write("========================================\n");
            writer.write("Date/Time: " + dateTime + "\n");
            writer.write("User: " + username + "\n");
            for (Product p : items) {
                writer.write(p.toString() + "\n");
            }
            writer.write("Total: $" + String.format("%.2f", total) + "\n");
            writer.write("========================================\n\n");
        } catch (IOException e) {
            System.out.println("Error writing transaction.");
        }
    }

    public static void main(String[] args) {
        boolean authenticated = false;

        while (!authenticated) {
            System.out.println("[1] Sign Up\n[2] Log In");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                SignUp();
                LogIn();
                authenticated = true;
            } else if (choice.equals("2")) {
                if (users.isEmpty()) {
                    System.out.println("No users. Please sign up first.");
                } else {
                    LogIn();
                    authenticated = true;
                }
            } else {
                System.out.println("Invalid choice.");
            }
        }

        boolean continueTransaction = true;
        while (continueTransaction) {
            cart.clear();
            boolean addMore = true;

            while (addMore) {
                displayProducts();
                int productIndex = -1;

                try {
                    System.out.print("Select product number: ");
                    productIndex = Integer.parseInt(sc.nextLine());
                    if (productIndex < 1 || productIndex > prodNames.length) {
                        System.out.println("Invalid product number.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                    continue;
                }

                int quantity = 0;
                try {
                    System.out.print("Enter quantity: ");
                    quantity = Integer.parseInt(sc.nextLine());
                    if (quantity <= 0) {
                        System.out.println("Quantity must be positive.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid quantity.");
                    continue;
                }

                cart.add(new Product(prodNames[productIndex - 1], quantity, prodPrices[productIndex - 1]));

                System.out.print("Remove a product? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    displayCart();
                    System.out.print("Enter product number to remove: ");
                    try {
                        int removeIndex = Integer.parseInt(sc.nextLine());
                        if (removeIndex >= 1 && removeIndex <= cart.size()) {
                            cart.remove(removeIndex - 1);
                            System.out.println("Product removed.");
                        } else {
                            System.out.println("Invalid product number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }

                System.out.print("Add another product? (y/n): ");
                if (!sc.nextLine().equalsIgnoreCase("y")) {
                    addMore = false;
                }
            }

            displayCart();
            double total = calculateTotal();
            System.out.printf("Total: $%.2f%n", total);

            double payment = 0;
            while (true) {
                System.out.print("Enter payment: ");
                try {
                    payment = Double.parseDouble(sc.nextLine());
                    if (payment < total) {
                        System.out.println("Insufficient payment.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                }
            }

            System.out.printf("Change: $%.2f%n", payment - total);
            logTransaction(currentUser, new ArrayList<>(cart), total);

            System.out.print("Perform another transaction? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                continueTransaction = false;
                System.out.println("Thank you for shopping!");
            }
        }
    }
}







    

