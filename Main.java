package main;
import model.Book;
import model.Reader;
import service.LibraryService;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static final LibraryService service = new LibraryService();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Library system");
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1": addBook();            break;
                case "2": addReader();          break;
                case "3": borrowBook();         break;
                case "4": returnBook();         break;
                case "5": showReaderBooks();    break;
                case "6": searchBook();         break;
                case "7": showAvailableBooks(); break;
                case "8": analyticsMenu();      break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Unknown option. Enter a number from the menu");
            }
            System.out.println();
        }
    }
    private static void printMenu() {
        System.out.println(" 1. Add book");
        System.out.println(" 2. Add reader");
        System.out.println(" 3. Borrow book");
        System.out.println(" 4. Return book");
        System.out.println(" 5. Reader's books");
        System.out.println(" 6. Search book");
        System.out.println(" 7. All available books");
        System.out.println(" 8. Analytics");
        System.out.println(" 0. Exit");
        System.out.print("Choose: ");
    }
    private static void addBook() {
        int id = readInt("Book ID: ");
        if (id < 0) return;
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) { System.out.println("Error: title cannot be empty"); return; }
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) { System.out.println("Error: author cannot be empty"); return; }
        int year = readInt("Year: ");
        if (year < 0) return;
        Book book = new Book(id, title, author, year);
        if (service.addBook(book)) {
            System.out.println("Added: " + book);
        }
    }
    private static void addReader() {
        int id = readInt("Reader ID: ");
        if (id < 0) return;
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Error: name cannot be empty"); return; }
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) { System.out.println("Error: phone cannot be empty"); return; }
        Reader reader = new Reader(id, name, phone);
        if (service.addReader(reader)) {
            System.out.println("Registered: " + reader);
        }
    }
    private static void borrowBook() {
        int readerId = readInt("Reader ID: ");
        if (readerId < 0) return;
        int bookId = readInt("Book ID: ");
        if (bookId < 0) return;
        if (service.borrowBook(readerId, bookId)) {
            Book book = service.findBookById(bookId);
            Reader reader = service.findReaderById(readerId);
            System.out.println("Book '" + book.getTitle() + "' issued to " + reader.getName() + ".");
        }
    }
    private static void returnBook() {
        int readerId = readInt("Reader ID: ");
        if (readerId < 0) return;

        int bookId = readInt("Book ID: ");
        if (bookId < 0) return;

        if (service.returnBook(readerId, bookId)) {
            Book book = service.findBookById(bookId);
            System.out.println("Book '" + book.getTitle() + "' returned successfully");
        }
    }
    private static void showReaderBooks() {
        int readerId = readInt("Reader ID: ");
        if (readerId < 0) return;
        Reader reader = service.findReaderById(readerId);
        if (reader == null) {
            System.out.println("Error: reader with ID " + readerId + " not found");
            return;
        }
        List<Book> books = service.getReaderBooks(readerId);
        if (books.isEmpty()) {
            System.out.println(reader.getName() + " has no books on hand");
        } else {
            System.out.println("Books held by " + reader.getName() + " (" + books.size() + "):");
            for (Book b : books) {
                System.out.println("  " + b);
            }
        }
    }
    private static void searchBook() {
        System.out.print("Search (title or author): ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("Error: keyword cannot be empty");
            return;
        }
        List<Book> books = service.searchBooks(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found matching \"" + keyword + "\".");
        } else {
            System.out.println("Found " + books.size() + " book(s) matching \"" + keyword + "\":");
            for (Book b : books) {
                System.out.println("  " + b);
            }
        }
    }
    private static void showAvailableBooks() {
        List<Book> books = service.getAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No available books at the moment.");
        } else {
            System.out.println("Available books (" + books.size() + "):");
            for (Book b : books) {
                System.out.println("  " + b);
            }
        }
    }
    private static void analyticsMenu() {
        while (true) {
            System.out.println("Analytics");
            System.out.println(" 1. Readers with books on hand");
            System.out.println(" 2. Top-3 readers by total borrows");
            System.out.println(" 0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1": showReadersWithBooks(); break;
                case "2": showTop3Readers();      break;
                case "0": return;
                default: System.out.println("Unknown option");
            }
            System.out.println();
        }
    }
    private static void showReadersWithBooks() {
        List<Reader> readers = service.getReadersWithBooks();
        if (readers.isEmpty()) {
            System.out.println("No readers currently have books");
        } else {
            System.out.println("Readers with books on hand (" + readers.size() + "):");
            for (Reader r : readers) {
                int count = service.getReaderBooks(r.getId()).size();
                System.out.printf("  %-25s → %d book(s)%n", r.getName(), count);
            }
        }
    }
    private static void showTop3Readers() {
        List<Reader> top3 = service.getTop3Readers();
        if (top3.isEmpty()) {
            System.out.println("No readers registered yet");
        } else {
            System.out.println("Top-3 readers by total borrows:");
            int rank = 1;
            for (Reader r : top3) {
                int count = service.getBorrowCount(r.getId());
                System.out.printf("  %d. %-25s → %d borrow(s)%n", rank++, r.getName(), count);
            }
        }
    }
    private static int readInt(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Error: please enter a valid integer.");
            return -1;
        }
    }
}
