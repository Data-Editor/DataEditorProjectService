package com.niek125.projectproducer.validators;

public interface Validator<T> {
    boolean validate(T object);
}
