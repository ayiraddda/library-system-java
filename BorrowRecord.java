package model;
public class BorrowRecord {
    private int bookId;
    private int readerId;
    private String borrowDate;
    private String returnDate;   
    public BorrowRecord(int bookId, int readerId, String borrowDate) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.returnDate = "";
    }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }
    public String getBorrowDate() { return borrowDate; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public boolean isActive() {
        return returnDate.isEmpty();
    }
    @Override
    public String toString() {
        String returned = returnDate.isEmpty() ? "not returned" : "returned " + returnDate;
        return "BorrowRecord{bookId=" + bookId
                + ", readerId=" + readerId
                + ", borrowed=" + borrowDate
                + ", " + returned + "}";
    }
}