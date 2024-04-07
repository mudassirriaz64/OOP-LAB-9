package TASKS.LAB_TASKS;

import java.io.*;
import java.util.*;

class Book {
    private String title;
    private String author;
    private double price;
    private int quantity;

    public Book(String title, String author, double price, int quantity)
    {
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    // Method to return formatted price string with dollar sign
    public String getPriceString()
    {
        return String.format("$%.2f", price);
    }

    @Override
    public String toString()
    {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + getPriceString() + // Using formatted price string
                ", quantity=" + quantity +
                '}';
    }
}

public class BookInventorySystem
{
    private static final Map<String, Book> bookInventory = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "inventory.txt";

    public static void main(String[] args) {
        loadInventoryFromFile(); // Load inventory data from file during startup
        Runtime.getRuntime().addShutdownHook(new Thread(BookInventorySystem::saveInventoryToFile)); // Save inventory data to file when exiting

        while (true)
        {
            clearScreen(); // Clear screen before displaying menu options

            System.out.println("Welcome to Book Inventory System!");
            System.out.println("1. Add a new book");
            System.out.println("2. Remove a book");
            System.out.println("3. Update book details");
            System.out.println("4. Search for a book by title or author");
            System.out.println("5. Display all books in inventory");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice)
            {
                case 1:
                    clearScreen();
                    addBook();
                    break;
                case 2:
                    clearScreen();
                    removeBook();
                    break;
                case 3:
                    clearScreen();
                    updateBook();
                    break;
                case 4:
                    clearScreen();
                    searchBook();
                    break;
                case 5:
                    clearScreen();
                    displayAllBooks();
                    break;
                case 6:
                    clearScreen();
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void loadInventoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME)))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                String author = parts[1];
                double price = Double.parseDouble(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
                bookInventory.put(title, new Book(title, author, price, quantity));
            }
            System.out.println("Inventory loaded successfully!");
        }
        catch (IOException e)
        {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
    }

    private static void saveInventoryToFile()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book book : bookInventory.values())
            {
                writer.write(String.format("%s,%s,%.2f,%d\n", book.getTitle(), book.getAuthor(), book.getPrice(), book.getQuantity()));
            }
            System.out.println("Inventory saved to file successfully!");
        }
        catch (IOException e)
        {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }

    private static void addBook()
    {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter book quantity: ");
        int quantity = scanner.nextInt();

        Book book = new Book(title, author, price, quantity);
        bookInventory.put(title, book);
        System.out.println("Book added successfully!");
    }

    private static void removeBook()
    {
        System.out.print("Enter book title to remove: ");
        String title = scanner.nextLine();
        if (bookInventory.containsKey(title))
        {
            bookInventory.remove(title);
            System.out.println("Book removed successfully!");
        }
        else
        {
            System.out.println("Book not found in inventory.");
        }
    }

    private static void updateBook()
    {
        System.out.print("Enter book title to update: ");
        String title = scanner.nextLine();
        if (bookInventory.containsKey(title))
        {
            System.out.print("Enter new author name (leave blank to skip): ");
            String author = scanner.nextLine();
            if (!author.isEmpty())
            {
                bookInventory.get(title).setAuthor(author);
            }

            System.out.print("Enter new price (leave 0.0 to skip): ");
            double price = scanner.nextDouble();

            if (price != 0.0)
            {
                bookInventory.get(title).setPrice(price);
            }

            System.out.print("Enter new quantity (leave 0 to skip): ");
            int quantity = scanner.nextInt();

            if (quantity != 0)
            {
                bookInventory.get(title).setQuantity(quantity);
            }

            System.out.println("Book details updated successfully!");
        }
        else
        {
            System.out.println("Book not found in inventory.");
        }
    }

    private static void searchBook()
    {
        System.out.print("Enter search keyword (title or author): ");
        String keyword = scanner.nextLine();
        ArrayList<Book> searchResults = new ArrayList<>();

        for (Map.Entry<String, Book> entry : bookInventory.entrySet())
        {
            Book book = entry.getValue();
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) || book.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
            {
                searchResults.add(book);
            }
        }

        if (!searchResults.isEmpty())
        {
            System.out.println("Search results:");
            for (Book book : searchResults)
            {
                System.out.println(book);
            }
        } else
        {
            System.out.println("No matching books found.");
        }
    }

    private static void displayAllBooks()
    {
        clearScreen();
        System.out.println("Books in inventory:");
        for (Book book : bookInventory.values())
        {
            System.out.println(book);
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
