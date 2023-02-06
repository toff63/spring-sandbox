package org.springframework.guides.rest.payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ResponseEntity<EntityModel<Employee>> newEmployee(@RequestBody Employee newEmployee){
        EntityModel<Employee> employee =  assembler.toModel(repository.save(newEmployee));
        return ResponseEntity.created(employee.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(employee);
    }

    // Single Item

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id){
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<EntityModel<Employee>> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
        EntityModel<Employee> employeeEntityModel = assembler.toModel(repository.findById(id).map(employee -> {
            employee.setName(newEmployee.name);
            employee.setRole(newEmployee.role);
            return repository.save(employee);
        }
        ).orElseThrow(() -> new EmployeeNotFoundException(id)));
        return ResponseEntity.ok(employeeEntityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
