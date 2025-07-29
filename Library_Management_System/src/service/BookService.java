package service;

import dao.BookDao;
import domain.Book;
import domain.AvailabilityStatus;
import exceptions.DatabaseException;
import util.ValidationUtil;

import java.util.List;

public class BookService {
    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void addBook(Book book) throws DatabaseException {
        ValidationUtil.validateBookTitle(book.getTitle());
        ValidationUtil.validateNotEmpty("Author", book.getAuthor());
        ValidationUtil.validateNotEmpty("Category", book.getCategory());
        bookDao.addBook(book);
    }

    public void updateBook(Book book) throws DatabaseException {
        if (!bookDao.bookExists(book.getBookId())) {
            throw new DatabaseException("Book does not exist with ID: " + book.getBookId());
        }
        bookDao.updateBook(book);
    }

    public void updateBookAvailability(int id, AvailabilityStatus status) throws DatabaseException {
        if (!bookDao.bookExists(id)) {
            throw new DatabaseException("Book does not exist with ID: " + id);
        }
        bookDao.updateBookAvailability(id, status);
    }

    public List<Book> getAllBooks() throws DatabaseException {
        return bookDao.getAllBooks();
    }

    public boolean bookExists(int id) throws DatabaseException {
        return bookDao.bookExists(id);
    }
}
