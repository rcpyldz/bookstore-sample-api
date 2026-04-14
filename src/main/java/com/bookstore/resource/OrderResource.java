package com.bookstore.resource;

import com.bookstore.exception.*;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.Order;
import com.bookstore.util.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final DataStore dataStore = DataStore.getInstance();

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = dataStore.getCartByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " is empty or does not exist.");
        }
        double totalPrice = 0.0;
        for (var entry : cart.getItems().entrySet()) {
            int bookId = entry.getKey();
            int quantity = entry.getValue();
            Book book = dataStore.getBookById(bookId);
            if (book == null) {
                throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
            }
            if (book.getStock() < quantity) {
                throw new OutOfStockException("Book with ID " + bookId + " is out of stock.");
            }
            totalPrice += book.getPrice() * quantity;
            book.setStock(book.getStock() - quantity);
            dataStore.updateBook(book);
        }
        Order order = new Order(0, customerId, cart.getItems(), totalPrice);
        Order created = dataStore.addOrder(order);
        cart.getItems().clear(); // Clear cart after order
        dataStore.updateCart(cart);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Order> getOrders(@PathParam("customerId") int customerId) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        return dataStore.getOrdersByCustomerId(customerId);
    }

    @GET
    @Path("/{orderId}")
    public Order getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        if (dataStore.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Order order = dataStore.getOrderById(customerId, orderId);
        if (order == null) {
            throw new InvalidInputException("Order with ID " + orderId + " does not exist for customer ID " + customerId);
        }
        return order;
    }
}