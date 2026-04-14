package com.bookstore.resource;

import com.bookstore.exception.*;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.util.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final DataStore dataStore = DataStore.getInstance();

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, Map<String, Integer> item) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        int bookId = item.getOrDefault("bookId", -1);
        int quantity = item.getOrDefault("quantity", 0);
        if (bookId <= 0) {
            throw new InvalidInputException("Valid book ID is required.");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be positive.");
        }
        Book book = dataStore.getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }
        if (book.getStock() < quantity) {
            throw new OutOfStockException("Book with ID " + bookId + " is out of stock.");
        }
        Cart cart = dataStore.getCartByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(customerId);
            dataStore.addCart(cart);
        }
        cart.getItems().merge(bookId, quantity, Integer::sum);
        book.setStock(book.getStock() - quantity);
        dataStore.updateBook(book);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = dataStore.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateCartItem(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, Map<String, Integer> item) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        int quantity = item.getOrDefault("quantity", 0);
        if (quantity < 0) {
            throw new InvalidInputException("Quantity cannot be negative.");
        }
        Book book = dataStore.getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }
        Cart cart = dataStore.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }
        Integer currentQuantity = cart.getItems().getOrDefault(bookId, 0);
        int stockAdjustment = quantity - currentQuantity;
        if (stockAdjustment > book.getStock()) {
            throw new OutOfStockException("Book with ID " + bookId + " does not have enough stock.");
        }
        if (quantity == 0) {
            cart.getItems().remove(bookId);
        } else {
            cart.getItems().put(bookId, quantity);
        }
        book.setStock(book.getStock() - stockAdjustment);
        dataStore.updateBook(book);
        return dataStore.updateCart(cart);
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Book book = dataStore.getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }
        Cart cart = dataStore.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }
        Integer quantity = cart.getItems().remove(bookId);
        if (quantity != null) {
            book.setStock(book.getStock() + quantity);
            dataStore.updateBook(book);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}