package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import domain.Book;
import domain.BookStatus;
import domain.AvailabilityStatus;
import exceptions.DatabaseException;
import util.DBUtil;

public class BookDao {

    public void addBook(Book book) throws DatabaseException {
        String sql = "INSERT INTO books (Title, Author, Category, Status, Availability, DateAdded) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setString(4, book.getStatus().name());
            ps.setString(5, book.getAvailability().name());
            ps.setDate(6, java.sql.Date.valueOf(book.getDateAdded()));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Error adding book", e);
        }
    }

    public void updateBook(Book book) throws DatabaseException {
        String logSql = "INSERT INTO books_log (BookId, Title, Author, Category, Status, Availability, Operation) SELECT BookId, Title, Author, Category, Status, Availability, 'UPDATE' FROM books WHERE BookId=?";
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
                logPs.setInt(1, book.getBookId());
                logPs.executeUpdate();
            }

            StringBuilder updateSql = new StringBuilder("UPDATE books SET ");
            List<Object> params = new ArrayList<>();

            if (book.getTitle() != null) {
                updateSql.append("Title=?, ");
                params.add(book.getTitle());
            }
            if (book.getAuthor() != null) {
                updateSql.append("Author=?, ");
                params.add(book.getAuthor());
            }
            if (book.getCategory() != null) {
                updateSql.append("Category=?, ");
                params.add(book.getCategory());
            }
            if (book.getStatus() != null) {
                updateSql.append("Status=?, ");
                params.add(book.getStatus().name());
            }

            if (params.isEmpty()) {
                throw new DatabaseException("No fields provided to update.");
            }

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
            throw new DatabaseException("Failed to update book", e);
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
        }
    }

    public void updateBookAvailability(int id, AvailabilityStatus avail) throws DatabaseException {
        String logSql = "INSERT INTO books_log (BookId, Title, Author, Category, Status, Availability, Operation) SELECT BookId, Title, Author, Category, Status, Availability, 'UPDATE_AVAILABILITY' FROM books WHERE BookId=?";
        String updateSql = "UPDATE books SET Availability=? WHERE BookId=?";
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
                logPs.setInt(1, id);
                logPs.executeUpdate();
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, avail.name());
                updatePs.setInt(2, id);
                updatePs.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
            throw new DatabaseException("Failed to update availability", e);
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
        }
    }

    public List<Book> getAllBooks() throws DatabaseException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("BookId"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Category"),
                        BookStatus.valueOf(rs.getString("Status")),
                        AvailabilityStatus.valueOf(rs.getString("Availability")),
                        rs.getDate("DateAdded").toLocalDate()
                ));
            }
        } catch (Exception e) {
            throw new DatabaseException("Error fetching books", e);
        }
        return list;
    }

    public boolean bookExists(int id) throws DatabaseException {
        String sql = "SELECT BookId FROM books WHERE BookId = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new DatabaseException("Error checking if book exists", e);
        }
    }
}
