package org.springframework.guides.rest.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String entity, Long id){
        super(entity + " " + id + " not Found");
    }
}
