package service;

import dao.BookDao;
import dao.BookDaoInterface;
import domain.Book;
import domain.AvailabilityStatus;
import exceptions.ManagementException;
import util.ValidationUtil;

import java.util.List;

public class BookService implements BookServiceInterface{
    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void addBook(Book book) throws ManagementException {
    	if (bookDao.bookExists(book)) {
            throw new ManagementException("Book Already exist with given details.");
        }
        ValidationUtil.validateBookTitle(book.getTitle());
        ValidationUtil.validateNotEmpty("Author", book.getAuthor());
        ValidationUtil.validateNotEmpty("Category", book.getCategory().toString());
        bookDao.addBook(book);
    }

    public void updateBook(Book book) throws ManagementException {
        if (!bookDao.bookExists(book)) {
            throw new ManagementException("Book does not exist with given details.");
        }
        ValidationUtil.validateBookTitle(book.getTitle());
        bookDao.updateBook(book);
    }

    public void updateBookAvailability(int id, AvailabilityStatus status) throws ManagementException {
        Book book = new Book();
        book.setBookId(id);

        if (!bookDao.bookExists(book)) {
            throw new ManagementException("Book does not exist with ID: " + id);
        }
        bookDao.updateBookAvailability(id, status);
    }


    public List<Book> getAllBooks() throws ManagementException {
        return bookDao.getAllBooks();
    }

    public boolean bookExists(Book book) throws ManagementException {
        return bookDao.bookExists(book);
    }
}
