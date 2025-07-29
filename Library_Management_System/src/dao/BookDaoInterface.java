package dao;

import java.util.List;

import domain.Book;
import domain.AvailabilityStatus;
import exceptions.ManagementException;

public interface BookDaoInterface {

	public void addBook(Book book) throws ManagementException ;
    public void updateBook(Book book) throws ManagementException ;
    public void updateBookAvailability(int id, AvailabilityStatus avail) throws ManagementException ;
    public List<Book> getAllBooks() throws ManagementException ;
    public boolean bookExists(Book book) throws ManagementException ;

}
