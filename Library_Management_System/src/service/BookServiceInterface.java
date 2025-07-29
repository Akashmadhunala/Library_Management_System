package service;

import domain.Book;
import domain.AvailabilityStatus;
import exceptions.ManagementException;
import java.util.List;

public interface BookServiceInterface {
    public void addBook(Book book) throws ManagementException ;
    public void updateBook(Book book) throws ManagementException;
    public void updateBookAvailability(int id, AvailabilityStatus status) throws ManagementException ;
    public List<Book> getAllBooks() throws ManagementException ;
    public boolean bookExists(Book book) throws ManagementException;
}
