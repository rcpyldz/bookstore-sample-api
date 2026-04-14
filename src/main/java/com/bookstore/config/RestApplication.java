package com.bookstore.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Register resource classes
        classes.add(com.bookstore.resource.BookResource.class);
        classes.add(com.bookstore.resource.AuthorResource.class);
        classes.add(com.bookstore.resource.CustomerResource.class);
        classes.add(com.bookstore.resource.CartResource.class);
        classes.add(com.bookstore.resource.OrderResource.class);
        // Register exception mappers
        classes.add(com.bookstore.exception.BookNotFoundExceptionMapper.class);
        classes.add(com.bookstore.exception.AuthorNotFoundExceptionMapper.class);
        classes.add(com.bookstore.exception.CustomerNotFoundExceptionMapper.class);
        classes.add(com.bookstore.exception.InvalidInputExceptionMapper.class);
        classes.add(com.bookstore.exception.OutOfStockExceptionMapper.class);
        classes.add(com.bookstore.exception.CartNotFoundExceptionMapper.class);
        return classes;
    }
}