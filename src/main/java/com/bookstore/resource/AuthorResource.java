package com.bookstore.resource;

import com.bookstore.exception.*;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.util.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    public DataStore dataStore = DataStore.getInstance();

    @POST
    public Response createAuthor(Author author) {
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Author name is required.");
        }
        Author created = dataStore.addAuthor(author);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Author> getAllAuthors() {
        return dataStore.getAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") int id) {
        Author author = dataStore.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return author;
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author author) {
        author.setId(id);
        if (dataStore.getAuthorById(id) == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Author name is required.");
        }
        return dataStore.updateAuthor(author);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        if (!dataStore.deleteAuthor(id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int id) {
        if (dataStore.getAuthorById(id) == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return dataStore.getBooksByAuthor(id);
    }
}