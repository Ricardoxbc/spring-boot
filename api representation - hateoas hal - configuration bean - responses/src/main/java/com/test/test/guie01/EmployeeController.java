package com.test.test.guie01;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/e")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @GetMapping("/ee")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> entities = repository.findAll()
                .stream()
                .map(Employee::toEntityModel)
                .collect(Collectors.toList());
        return CollectionModel.of(entities, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @GetMapping("/ee/{id}")
    public EntityModel<Employee> get(@PathVariable Long id) {
        Employee em = repository.findById(Long.valueOf(id)).orElseThrow(() -> new EmployeeNotFoundException(id + ""));
        return Employee.toEntityModel(em);
    }

    @PostMapping("/e")
    public Employee create(@RequestBody Employee entity) {
        // TODO: process POST request
        return repository.save(entity);
    }

    @PostMapping("/ee")
    ResponseEntity<?> create2(@RequestBody Employee entity) {
        // TODO: process POST request
        EntityModel<Employee> em = Employee.toEntityModel(repository.save(entity));
        return ResponseEntity.created(em.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(em);
    }

    @PutMapping("ee/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Employee entity) {
        // TODO: process PUT request
        if (repository.existsById(Long.valueOf(id))) {
            EntityModel<Employee> em = Employee.toEntityModel(repository.save(entity));
            return ResponseEntity.created(em.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(em);
        }
        throw new EmployeeNotFoundException(id);
    }

    @DeleteMapping("/ee")
    public ResponseEntity<?> delete(@RequestBody Employee employee) {
        if (!repository.existsById(employee.getId())){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(employee.getId());
        return ResponseEntity.noContent().build();
    }

}
