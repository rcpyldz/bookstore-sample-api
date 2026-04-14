package com.bookstore.resource;

import com.bookstore.exception.*;
import com.bookstore.model.Book;
import com.bookstore.util.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final DataStore dataStore = DataStore.getInstance();

    @POST
    public Response createBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidInputException("Title is required.");
        }
        if (dataStore.getAuthorById(book.getAuthorId()) == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        Book created = dataStore.addBook(book);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Book> getAllBooks() {
        return dataStore.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Book getBookById(@PathParam("id") int id) {
        Book book = dataStore.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book book) {
        book.setId(id);
        if (dataStore.getBookById(id) == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidInputException("Title is required.");
        }
        if (dataStore.getAuthorById(book.getAuthorId()) == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        return dataStore.updateBook(book);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        if (!dataStore.deleteBook(id)) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}