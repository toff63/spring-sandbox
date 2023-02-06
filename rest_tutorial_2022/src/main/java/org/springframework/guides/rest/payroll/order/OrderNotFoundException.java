package org.springframework.guides.rest.payroll.order;

import org.springframework.guides.rest.exceptions.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(Long id) {
        super("Order", id);
    }

}
