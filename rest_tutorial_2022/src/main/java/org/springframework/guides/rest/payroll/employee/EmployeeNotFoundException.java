package org.springframework.guides.rest.payroll.employee;

import org.springframework.guides.rest.exceptions.NotFoundException;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException(Long id) {
        super("Employee", id);
    }

}
