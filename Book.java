package model;
public class Book {
    private int id;
    private String title;
    private String author;
    private int year;
    private boolean available;
    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = true;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    @Override
    public String toString() {
        String status = available ? "available" : "borrowed";
        return "Book{id=" + id
                + ", title='" + title + "'"
                + ", author='" + author + "'"
                + ", year=" + year
                + ", status=" + status + "}";
    }
}
