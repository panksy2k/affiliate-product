package com.product.affiliation.exceptions;

public class ProductRepositoryException extends RuntimeException {

    public ProductRepositoryException() {
        super();
    }

    public ProductRepositoryException(String message) {
        super(message);
    }
}
