package com.test.test.guie01;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String id) {
        super("Employee not found with id: " + id);
    }
}
