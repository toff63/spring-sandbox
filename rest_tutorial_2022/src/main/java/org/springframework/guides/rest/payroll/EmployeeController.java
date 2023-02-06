package org.springframework.guides.rest.payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;


    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate Root

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all(){
        List<Employee> employees = repository.findAll();
        return assembler.toCollectionModel(employees);
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee){
        return repository.save(newEmployee);
    }

    // Single Item

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id){
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
        return repository.findById(id).map(employee -> {
            employee.setName(newEmployee.name);
            employee.setRole(newEmployee.role);
            return repository.save(employee);
        }
        ).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
