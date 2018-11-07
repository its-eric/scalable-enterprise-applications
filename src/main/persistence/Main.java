package main.persistence;

import entities.Book;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        withDbTransaction("NewPersistenceUnit", em -> {
            deleteRecords(em);

            insertRecords(em);
            selectRecords(em);

            // Clear the entity cache to force re-select next time
            em.clear();

            updateRecords(em);
            selectRecords(em);
        });
    }

    private static void withDbTransaction(String puName, Consumer<EntityManager> transaction) {
        // Get an entity manager factory by persistence unit name
        // The database table will be automatically created by eclipselink
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(puName);

        // Create an entity manager
        EntityManager em = emf.createEntityManager();

        // Begin transaction
        em.getTransaction().begin();

        // The caller has specified what to do, run those steps now...
        transaction.accept(em);

        // Commit the transaction and close the entity manager
        em.getTransaction().commit();
        em.close();
    }

    private static void deleteRecords(EntityManager em) {
        // Bulk delete with criteria API
        Query query = em.createQuery("DELETE FROM books");
        int removedItems = query.executeUpdate();

        if (removedItems > 0) {
            System.out.println(removedItems + " items removed.");
        }
    }

    private static void insertRecords(EntityManager em) {
        for(int i = 0; i < 5; ++i) {
            insertRecord(em, i);
        }
    }

    private static void insertRecord(EntityManager em, int i) {
        Book book = makeBook(em, i);
        em.persist(book);
    }

    private static Book makeBook(EntityManager em, int i) {
        Book book = new Book();
        book.setTitle("Wonderful Works Of Some Author Volume #" + i);
        book.setAuthor("Some Author");
        book.setPrice(new BigDecimal(i * 1.41f));
        return book;
    }

    private static void selectRecords(EntityManager em) {
        // Select with JPQL and print out entities
        TypedQuery<Book> q = em.createQuery("SELECT b FROM books AS b ORDER BY b.title", Book.class);
        List<Book> books = q.getResultList();

        System.out.println();

        for(Book b : books) {
            System.out.println(b);
        }

        System.out.println();
    }

    private static void updateRecords(EntityManager em) {
        System.out.println("Updating items with price 0 to 100...");

        // Update with criteria API
        Query query =
                em.createQuery("UPDATE books AS b SET b.price = :originalPrice WHERE b.price = :newPrice")
                        .setParameter("originalPrice", 100.0)
                        .setParameter("newPrice", 0.0);
        int updatedItems = query.executeUpdate();

        if (updatedItems > 0) {
            System.out.println(updatedItems + " items updated.");
        }
    }
}
