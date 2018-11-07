package main.persistence;

import entities.Book;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

public class BookStore {

    private EntityManager em;

    // Constructor, gets the name of a persistence unit, and creates an appropriate `EntityManager` for it.
    public BookStore(String persistenceUnitName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        this.em = emf.createEntityManager();
    }

    private void withDbTransaction(Consumer<EntityManager> steps) {
        // Begin transaction
        this.em.getTransaction().begin();

        // The caller has specified what to do, run those steps now...
        steps.accept(this.em);

        // Commit the transaction and close the entity manager
        this.em.getTransaction().commit();
    }

    // Adds a new book into the database, and returns its identifier.
    public int addBook(Book newBook) {
        withDbTransaction(em -> {
            this.em.persist(newBook);
            this.em.flush();
        });
        return newBook.getId();
    }

    // Deletes the book with the given id (if it exists in the database).
    public void removeBookById(int bookId) {
        withDbTransaction(em -> {
            Query query = em.createQuery("DELETE FROM BOOKS WHERE id = :bookId")
                    .setParameter("bookId", bookId);
            int removedItems = query.executeUpdate();

            if (removedItems > 0) {
                System.out.println(removedItems + " items removed.");
            } else {
                System.out.println("Couldn't find a book with this id.");
            }
        });
    }

    // Gets all books present in the database.
    public List<Book> getAllBooks() {
        TypedQuery<Book> q = this.em.createQuery("SELECT b FROM BOOKS AS b ORDER BY b.title", Book.class);
        List<Book> books = q.getResultList();
        this.em.clear();
        return books;
    }
}
