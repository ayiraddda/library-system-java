package service;
import model.Book;
import model.BorrowRecord;
import model.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
public class LibraryService {

    private final ArrayList<Book> bookList             = new ArrayList<>();
    private final ArrayList<Reader> readerList         = new ArrayList<>();
    private final ArrayList<BorrowRecord> borrowRecordList = new ArrayList<>();

    private final HashMap<Integer, Book> bookMap     = new HashMap<>();
    private final HashMap<Integer, Reader> readerMap = new HashMap<>();
    public boolean addBook(Book book) {
        if (bookMap.containsKey(book.getId())) {
            System.out.println("Error: book with ID " + book.getId() + " already exists");
            return false;
        }
        bookList.add(book);
        bookMap.put(book.getId(), book);
        return true;
    }
    public boolean addReader(Reader reader) {
        if (readerMap.containsKey(reader.getId())) {
            System.out.println("Error: reader with ID " + reader.getId() + " already exists");
            return false;
        }
        readerList.add(reader);
        readerMap.put(reader.getId(), reader);
        return true;
    }
    public boolean borrowBook(int readerId, int bookId) {
        Reader reader = readerMap.get(readerId);
        if (reader == null) {
            System.out.println("Error: reader with ID " + readerId + " not found");
            return false;
        }
        Book book = bookMap.get(bookId);
        if (book == null) {
            System.out.println("Error: book with ID " + bookId + " not found");
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("Error: book '" + book.getTitle() + "' is already borrowed");
            return false;
        }
        book.setAvailable(false);
        borrowRecordList.add(new BorrowRecord(bookId, readerId, LocalDate.now().toString()));
        return true;
    }
    public boolean returnBook(int readerId, int bookId) {
        Reader reader = readerMap.get(readerId);
        if (reader == null) {
            System.out.println("Error: reader with ID " + readerId + " not found");
            return false;
        }
        Book book = bookMap.get(bookId);
        if (book == null) {
            System.out.println("Error: book with ID " + bookId + " not found");
            return false;
        }
        BorrowRecord activeRecord = findActiveRecord(readerId, bookId);
        if (activeRecord == null) {
            System.out.println("Error: no active borrow record found for reader #"
                    + readerId + " and book #" + bookId + ".");
            return false;
        }
        book.setAvailable(true);
        activeRecord.setReturnDate(LocalDate.now().toString());
        return true;
    }
    public List<Book> getReaderBooks(int readerId) {
        if (!readerMap.containsKey(readerId)) {
            System.out.println("Error: reader with ID " + readerId + " not found");
            return new ArrayList<>();
        }
        List<Book> result = new ArrayList<>();
        for (BorrowRecord r : borrowRecordList) {
            if (r.getReaderId() == readerId && r.isActive()) {
                Book book = bookMap.get(r.getBookId());
                if (book != null) {
                    result.add(book);
                }
            }
        }
        return result;
    }
    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Book b : bookList) {
            if (b.getTitle().toLowerCase().contains(lowerKeyword)
                    || b.getAuthor().toLowerCase().contains(lowerKeyword)) {
                result.add(b);
            }
        }
        return result;
    }
    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book b : bookList) {
            if (b.isAvailable()) {
                result.add(b);
            }
        }
        return result;
    }
    public List<Reader> getReadersWithBooks() {
        Set<Integer> readerIdsWithBooks = new HashSet<>();
        for (BorrowRecord r : borrowRecordList) {
            if (r.isActive()) {
                readerIdsWithBooks.add(r.getReaderId());
            }
        }
        List<Reader> result = new ArrayList<>();
        for (Reader r : readerList) {
            if (readerIdsWithBooks.contains(r.getId())) {
                result.add(r);
            }
        }
        return result;
    }
    public List<Reader> getTop3Readers() {
        Map<Integer, Integer> borrowCountByReader = new HashMap<>();
        for (Reader r : readerList) {
            borrowCountByReader.put(r.getId(), 0);
        }
        for (BorrowRecord r : borrowRecordList) {
            int currentCount = borrowCountByReader.getOrDefault(r.getReaderId(), 0);
            borrowCountByReader.put(r.getReaderId(), currentCount + 1);
        }
        return readerList.stream()
                .sorted((a, b) -> Integer.compare(
                        borrowCountByReader.getOrDefault(b.getId(), 0),
                        borrowCountByReader.getOrDefault(a.getId(), 0)))
                .limit(3)
                .collect(Collectors.toList());
    }
    public Book findBookById(int id) { return bookMap.get(id); }
    public Reader findReaderById(int id) { return readerMap.get(id); }

    public int getBorrowCount(int readerId) {
        int count = 0;
        for (BorrowRecord r : borrowRecordList) {
            if (r.getReaderId() == readerId) count++;
        }
        return count;
    }
    private BorrowRecord findActiveRecord(int readerId, int bookId) {
        for (BorrowRecord r : borrowRecordList) {
            if (r.getReaderId() == readerId && r.getBookId() == bookId && r.isActive()) {
                return r;
            }
        }
        return null;
    }
}