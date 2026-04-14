package com.bookstore.util;

import com.bookstore.model.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataStore {
    private static final DataStore instance = new DataStore(); // Make final
    private final List<Book> books = new ArrayList<>(); // Make final
    private final List<Author> authors = new ArrayList<>(); // Make final
    private final List<Customer> customers = new ArrayList<>(); // Make final
    private final List<Cart> carts = new ArrayList<>(); // Make final
    private final List<Order> orders = new ArrayList<>(); // Make final
    private final AtomicInteger bookIdCounter = new AtomicInteger(1); // Make final
    private final AtomicInteger authorIdCounter = new AtomicInteger(1); // Make final
    private final AtomicInteger customerIdCounter = new AtomicInteger(1); // Make final
    private final AtomicInteger orderIdCounter = new AtomicInteger(1); // Make final

    private DataStore() {}

    public static DataStore getInstance() {
        return instance;
    }

    // Book methods
    public List<Book> getAllBooks() { return new ArrayList<>(books); }

    public Book getBookById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    public Book addBook(Book book) {
        book.setId(bookIdCounter.getAndIncrement());
        books.add(book);
        return book;
    }

    public Book updateBook(Book book) {
        // Find the book to update
        Book existingBook = getBookById(book.getId());
        if (existingBook == null) {
            return null; // Book not found
        }

        // Update the existing book with new values
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthorId(book.getAuthorId());
        existingBook.setPublicationYear(book.getPublicationYear());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setPrice(book.getPrice());
        existingBook.setStock(book.getStock());
        return existingBook;
    }

    public boolean deleteBook(int id) {
        return books.removeIf(b -> b.getId() == id);
    }

    // Author methods
    public List<Author> getAllAuthors() { return new ArrayList<>(authors); }

    public Author getAuthorById(int id) {
        return authors.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public Author addAuthor(Author author) {
        author.setId(authorIdCounter.getAndIncrement());
        authors.add(author);
        return author;
    }

    public Author updateAuthor(Author author) {
    Author existingAuthor = getAuthorById(author.getId());
    if (existingAuthor == null) {
        return null;
    }
    existingAuthor.setName(author.getName());
    existingAuthor.setBiography(author.getBiography()); // Add this line to update biography
    return existingAuthor;
}

    public boolean deleteAuthor(int id) {
        return authors.removeIf(a -> a.getId() == id);
    }

    public List<Book> getBooksByAuthor(int authorId) {
        return books.stream().filter(b -> b.getAuthorId() == authorId).toList();
    }

    // Customer methods
    public List<Customer> getAllCustomers() { return new ArrayList<>(customers); }

    public Customer getCustomerById(int id) {
        return customers.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public Customer addCustomer(Customer customer) {
        customer.setId(customerIdCounter.getAndIncrement());
        customers.add(customer);
        return customer;
    }

    public Customer updateCustomer(Customer customer) {
        Customer existingCustomer = getCustomerById(customer.getId());
        if (existingCustomer == null) {
            return null; // Customer not found
        }

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        // Update other customer fields as needed
        return existingCustomer;
    }

    public boolean deleteCustomer(int id) {
        return customers.removeIf(c -> c.getId() == id);
    }

    // Cart methods
    public Cart getCartByCustomerId(int customerId) {
        return carts.stream().filter(c -> c.getCustomerId() == customerId).findFirst().orElse(null);
    }

    public Cart addCart(Cart cart) {
        carts.add(cart);
        return cart;
    }

    public Cart updateCart(Cart cart) {
        Cart existingCart = getCartByCustomerId(cart.getCustomerId());
        if (existingCart == null) {
            return null; // Cart not found
        }

        existingCart.setItems(cart.getItems());
        return existingCart;
    }

    // Order methods
    public List<Order> getOrdersByCustomerId(int customerId) {
        return orders.stream().filter(o -> o.getCustomerId() == customerId).toList();
    }

    public Order getOrderById(int customerId, int orderId) {
        return orders.stream()
                     .filter(o -> o.getCustomerId() == customerId && o.getId() == orderId)
                     .findFirst()
                     .orElse(null);
    }

    public Order addOrder(Order order) {
        order.setId(orderIdCounter.getAndIncrement());
        orders.add(order);
        return order;
    }
}