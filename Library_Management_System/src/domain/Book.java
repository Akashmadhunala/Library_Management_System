package domain;


import java.time.LocalDateTime;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private Category category;
    private BookStatus status;
    private AvailabilityStatus availability;
    private String addedBy;
    private LocalDateTime OperationDate;

    public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Book() {}

    public Book(int bookId, String title, String author, Category category, BookStatus status, AvailabilityStatus availability, LocalDateTime dateAdded) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.availability = availability;
        this.OperationDate = dateAdded;
    }

    public Book(String title, String author, Category category, BookStatus status, AvailabilityStatus availability) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.availability = availability;
        this.OperationDate = LocalDateTime.now();
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public AvailabilityStatus getAvailability() { return availability; }
    public void setAvailability(AvailabilityStatus availability) { this.availability = availability; }

    public LocalDateTime getOperationDate() { return OperationDate; }
    public void setOperationDate(LocalDateTime OperationDate) { this.OperationDate = OperationDate; }

}
