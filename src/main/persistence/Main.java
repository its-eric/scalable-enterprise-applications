package main.persistence;

import entities.Book;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BookStore bookStore = new BookStore("NewPersistenceUnit");

        for (int i = 0; i < 5; i++) {
            Book book = makeBook(i);
            bookStore.addBook(book);
        }

        List<Book> allBooks = bookStore.getAllBooks();
        for (Book book: allBooks) {
            System.out.println(book);
        }

        bookStore.removeBookById(2);
        bookStore.removeBookById(11);

    }

    private static Book makeBook(int i) {
        Book book = new Book();
        book.setTitle("Wonderful Works Of Some Author Volume #" + i);
        book.setAuthor("Some Author");
        book.setPrice(new BigDecimal(i * 1.41f));
        return book;
    }

    private static void updateRecords(EntityManager em) {
        System.out.println("Updating items with price 0 to 100...");

        // Update with criteria API
        Query query =
                em.createQuery("UPDATE BOOKS AS b SET b.price = :originalPrice WHERE b.price = :newPrice")
                        .setParameter("originalPrice", 100.0)
                        .setParameter("newPrice", 0.0);
        int updatedItems = query.executeUpdate();

        if (updatedItems > 0) {
            System.out.println(updatedItems + " items updated.");
        }
    }
}
