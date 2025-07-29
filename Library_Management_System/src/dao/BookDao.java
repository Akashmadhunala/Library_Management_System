package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import domain.Book;
import domain.BookStatus;
import domain.Category;
import domain.AvailabilityStatus;
import exceptions.ManagementException;
import util.DBUtil;

public class BookDao implements BookDaoInterface {
	
	
	public void addBook(Book book) throws ManagementException {
	    String sql = "INSERT INTO books (Title, Author, Category, Status, Availability, AddedBy) VALUES (?, ?, ?, ?, ?, ?)";
	    Connection conn = null;
	    try {
	        conn = DBUtil.getConnection();
	        conn.setAutoCommit(false);
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            int i = 1;
	            ps.setString(i++, book.getTitle());
	            ps.setString(i++, book.getAuthor());
	            ps.setString(i++, String.valueOf(book.getCategory().name()));
	            ps.setString(i++, String.valueOf(book.getStatus().name().charAt(0))); 
	            ps.setString(i++, String.valueOf(book.getAvailability().name().charAt(0)));
	            ps.setString(i++, book.getAddedBy());

	            ps.executeUpdate();
	        }
	        // Just for testing rollback:
	        // if(true) throw new RuntimeException("Test rollback");
	        conn.commit();
	        System.out.println("Book added successfully and transaction committed.");
	    } catch (Exception e) {
	        if (conn != null) {
	            try {
	                conn.rollback();
	                System.err.println("Transaction rolled back due to error: " + e.getMessage());
	            } catch (Exception rollbackEx) {
	                System.err.println("Rollback failed: " + rollbackEx.getMessage());
	            }
	        }
	        throw new ManagementException("Error adding book", e);
	    } finally {
	        if (conn != null) {
	            try {
	                conn.setAutoCommit(true);
	                conn.close();
	            } catch (Exception closeEx) {
	                System.err.println("Connection close failed: " + closeEx.getMessage());
	            }
	        }
	    }
	}
	

	
	public void updateBook(Book book) throws ManagementException {
	    String logSql = "INSERT INTO books_log (BookId, Title, Author, Category, Status, Availability, AddedBy) " +
	                    "SELECT BookId, Title, Author, Category, Status, Availability, AddedBy " +
	                    "FROM books WHERE BookId=?";
	    Connection conn = null;
	    try {
	        conn = DBUtil.getConnection();
	        conn.setAutoCommit(false);

	        // Log old record
	        try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
	            logPs.setInt(1, book.getBookId());
	            logPs.executeUpdate();
	        }

	        // Dynamic update query
	        StringBuilder updateSql = new StringBuilder("UPDATE books SET ");
	        List<Object> params = new ArrayList<>();

	        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
	            updateSql.append("Title=?, ");
	            params.add(book.getTitle());
	        }
	        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
	            updateSql.append("Author=?, ");
	            params.add(book.getAuthor());
	        }
	        if (book.getCategory() != null) {
	            updateSql.append("Category=?, ");
	            params.add(book.getCategory().name()); // DB stores enum name
	        }
	        if (book.getStatus() != null) {
	            updateSql.append("Status=?, ");
	            params.add(book.getStatus() == BookStatus.ACTIVE ? "A" : "I");
	        }

	        if (params.isEmpty()) {
	            throw new ManagementException("No fields provided to update.");
	        }

	        // Remove last comma
	        updateSql.setLength(updateSql.length() - 2);
	        updateSql.append(" WHERE BookId=?");
	        params.add(book.getBookId());

	        try (PreparedStatement updatePs = conn.prepareStatement(updateSql.toString())) {
	            for (int i = 0; i < params.size(); i++) {
	                updatePs.setObject(i + 1, params.get(i));
	            }
	            updatePs.executeUpdate();
	        }

	        conn.commit();
	    } catch (Exception e) {
	        if (conn != null) {
	            try { conn.rollback(); } catch (Exception ignored) {}
	        }
	        throw new ManagementException("Failed to update book", e);
	    } finally {
	        if (conn != null) {
	            try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
	        }
	    }
	}

	public void updateBookAvailability(int id, AvailabilityStatus avail) throws ManagementException {
	    String logSql = "INSERT INTO books_log (BookId, Title, Author, Category, Status, Availability, AddedBy) " +
	                    "SELECT BookId, Title, Author, Category, Status, Availability, AddedBy " +
	                    "FROM books WHERE BookId=?";
	    String updateSql = "UPDATE books SET Availability=? WHERE BookId=?";
	    Connection conn = null;

	    try {
	        conn = DBUtil.getConnection();
	        conn.setAutoCommit(false);

	        // Log old record
	        try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
	            logPs.setInt(1, id);
	            logPs.executeUpdate();
	        }

	        // Update availability
	        try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
	            updatePs.setString(1, avail == AvailabilityStatus.AVAILABLE ? "A" : "I");
	            updatePs.setInt(2, id);
	            updatePs.executeUpdate();
	        }

	        conn.commit();
	    } catch (Exception e) {
	        if (conn != null) {
	            try { conn.rollback(); } catch (Exception ignored) {}
	        }
	        throw new ManagementException("Failed to update availability", e);
	    } finally {
	        if (conn != null) {
	            try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
	        }
	    }
	}


    public List<Book> getAllBooks() throws ManagementException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT BookId, Title, Author, Category, Status, Availability FROM books";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();

                book.setBookId(rs.getInt("BookId"));
                book.setTitle(rs.getString("Title"));
                book.setAuthor(rs.getString("Author"));

                String categoryStr = rs.getString("Category");
                book.setCategory(Category.valueOf(categoryStr.toUpperCase()));

                String statusChar = rs.getString("Status");
                BookStatus status = "A".equalsIgnoreCase(statusChar)
                        ? BookStatus.ACTIVE
                        : BookStatus.INACTIVE;
                book.setStatus(status);

                char availChar = rs.getString("Availability").charAt(0);
                AvailabilityStatus availability = (availChar == 'A')
                        ? AvailabilityStatus.AVAILABLE
                        : AvailabilityStatus.ISSUED;
                book.setAvailability(availability);
                list.add(book);
            }

        } catch (Exception e) {
            throw new ManagementException("Error fetching books", e);
        }
        return list;
    }




    public boolean bookExists(Book book) throws ManagementException {
        StringBuilder sql = new StringBuilder("SELECT BookId FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (book.getBookId() > 0) {
            sql.append(" AND BookId = ?");
            params.add(book.getBookId());
        }
        else if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            sql.append(" AND Title = ?");
            params.add(book.getTitle().trim());
        }
        else if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            sql.append(" AND Author = ?");
            params.add(book.getAuthor().trim());
        }
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new ManagementException("Error checking if book exists", e);
        }
    }

}
