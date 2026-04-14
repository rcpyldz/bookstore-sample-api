package com.bookstore.resource;

import com.bookstore.exception.*;
import com.bookstore.model.Customer;
import com.bookstore.util.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private final DataStore dataStore = DataStore.getInstance();

    @POST
    public Response createCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InvalidInputException("Customer name is required.");
        }
        if (customer.getEmail() == null || !customer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputException("Valid email is required.");
        }
        if (customer.getPassword() == null || customer.getPassword().length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters.");
        }
        Customer created = dataStore.addCustomer(customer);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Customer> getAllCustomers() {
        return dataStore.getAllCustomers();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomerById(@PathParam("id") int id) {
        Customer customer = dataStore.getCustomerById(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer customer) {
        customer.setId(id);
        if (dataStore.getCustomerById(id) == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InvalidInputException("Customer name is required.");
        }
        if (customer.getEmail() == null || !customer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputException("Valid email is required.");
        }
        return dataStore.updateCustomer(customer);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        if (!dataStore.deleteCustomer(id)) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}